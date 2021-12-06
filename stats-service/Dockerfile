FROM ubuntu:20.04

ENV LANG='C'

RUN apt update \
    && apt install -y ghc cabal-install \
    && mkdir /deployments \
    && chown 1001 /deployments

RUN cabal update
RUN cabal --force-reinstalls install aeson
RUN cabal --force-reinstalls install svg-builder
RUN cabal install happstack-server

COPY --chown=1001 service.hs /deployments/service.hs

EXPOSE 8084
USER 1001

ENTRYPOINT [ "/deployments/service.hs" ]

