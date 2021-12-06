
sudo apt install ghc

sudo apt install cabal-install

cabal update

cabal install happstack-server

git clone https://github.com/Happstack/happstack-server.git


cabal install json
cabal --force-reinstalls install aeson

cabal install svg-builder
