#!/usr/bin/env runhaskell

{-# LANGUAGE DeriveGeneric, OverloadedStrings #-}


module Main where

-- standard imports
import Data.Maybe
import GHC.Generics
import Control.Monad
import Data.Functor ( (<&>) )
import Data.Char ( isSpace )
import Data.Text as T ( pack, Text, append )
import Control.Monad.IO.Class ( liftIO )
import Data.ByteString.Char8 as C8 ( pack )
import Data.ByteString.Lazy.Char8 as LC8 ( pack )
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BL
import qualified Data.ByteString.UTF8 as BSU
import qualified Data.ByteString.Lazy.UTF8 as BLS

-- other libraries
import Data.Aeson
import Graphics.Svg
import Happstack.Server ( toResponseBS, takeRequestBody, unBody, askRq,
                          Conf(..), simpleHTTP, dir, ok, badRequest, Response,
                          Request, ServerPart, ServerPartT, logMAccess,
                          notFound, toResponse, method, Method(GET) )


-- # Helper functions


sendJson :: ToJSON a => a -> String
sendJson = BLS.toString . encode


respJson :: ToJSON a => a -> Response
respJson = toResponseBS (C8.pack "application/json") . LC8.pack . sendJson


respSVG :: Element -> Response
respSVG = toResponseBS (C8.pack "image/svg+xml") . LC8.pack . show


getJsonBody :: Request -> ServerPart (Maybe String)
getJsonBody r = takeRequestBody r >>= \x -> pure $ process <$> x
    where process = trim . BLS.toString . unBody


askJson :: ServerPart (Maybe String)
askJson = askRq >>= getJsonBody


askJsonRaw :: ServerPart (Maybe BLS.ByteString)
askJsonRaw = askRq >>= (\r -> takeRequestBody r)
                   >>= pure . (unBody <$>)


trim :: String -> String
-- inefficient: maybe!
trim = reverse . dropWhile isSpace . reverse . dropWhile isSpace


-- # Main functions


serverConfig :: Conf
serverConfig = Conf {
    port = 8084
    , validator = Nothing
    , logAccess = Just logMAccess
    , timeout = 30
    , threadGroup = Nothing
    }


endpoints :: ServerPartT IO Response
endpoints = msum
    [
        dir ("ping" :: String) ping,
        stats,
        test,
        notFound $ toResponse ("not found" :: String)
    ]


main :: IO ()
main = do
    putStrLn "starting server"
    simpleHTTP serverConfig endpoints


ping :: ServerPart Response
ping = ok $ toResponse ("Pong!" :: String)


data Params = Params { values :: [Int]
                     , name :: String
                     }
    deriving (Generic, Show)

instance ToJSON Params where
    toEncoding = genericToEncoding defaultOptions

instance FromJSON Params


stats :: ServerPartT IO Response
stats = msum
    [
        dir ("stats" :: String) normal,
        dir ("stats" :: String) $ badRequest
                                $ respJson ("parameter `data` not provided"
                                            :: String)
    ]
    where
        normal = do
            method GET
            jsonData <- askJsonRaw
            case eitherDecode <$> jsonData :: Maybe (Either String Params) of
                Nothing -> badRequest
                    $ respJson ("JSON body is empty" :: String)
                Just (Left err) -> badRequest
                    $ respJson (("invalid JSON param", err) :: (String, String))
                Just (Right params) -> do
                    ok $ respSVG $ svg $ visualize (values params)
                                        <> addText (name params)


test :: ServerPartT IO Response
test = dir ("test" :: String) impl
    where
        impl = ok $ respSVG
                  $ svg
                  $ visualize [1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 4, 4, 3, 40]
                    <> (addText "test table")


pckShow :: Show a => a -> T.Text
pckShow = T.pack . show


graphSize :: (Int, Int)
graphSize = (400, 200)


bgColor, fgColor, txtColor :: T.Text
bgColor = "#CCCCCC"
fgColor = "#5c062d"
txtColor = "#333333"


getPercent :: (Fractional b, Integral a) => a -> a -> b
getPercent n mm = 2 * (fromIntegral n) * 90 / fromIntegral mm


getShift :: Fractional a => a -> a
getShift prt = 100 - (prt / 2)


percent :: T.Text -> T.Text
percent = (`T.append` T.pack "%")


addText :: String -> Element
addText txt = text_
            [ X_ <<- "5%"
            , Y_ <<- "10%"
            , Font_size_ <<- "20"
            , Text_anchor_ <<- "left"
            , Fill_opacity_ <<- "0.5"
            , Fill_ <<- txtColor
            ] (toElement $ T.pack txt)


columns :: (Integral a) => [a] -> Element
columns vals = foldr column mempty [0 .. len - 1]
    where
        largest = maximum vals
        lowest = minimum vals
        len = length vals
        colW = (fst graphSize + 1) `div` len
        column i = (<> rect_
                    [ Y_      <<- (percent $ pckShow $ getShift prt)
                    , X_      <<- (pckShow $ colW * i)
                    , Width_  <<- pckShow colW
                    , Height_ <<- (pckShow $ prt)
                    , Fill_   <<- fgColor ])
            where prt = getPercent (vals !! i) largest


svg :: Element -> Element
svg con = doctype <> with (svg11_ con)
                    [ Version_ <<- "1.1"
                    , Width_   <<- T.pack (show $ fst graphSize)
                    , Height_  <<- T.pack (show $ snd graphSize) ]


visualize :: (Integral a) => [a] -> Element
visualize = bg . columns
    where
        bg = (rect_ [
                    Width_    <<- "100%"
                    , Height_ <<- "100%"
                    , Fill_   <<- bgColor ] <>)
