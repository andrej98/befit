FROM ubuntu:21.04

ENV LANG='C'

# RUN add-apt-repository ppa:hvr/ghc

RUN apt update \
    && apt install -y ghc-9.0.1 cabal-install git \
    && mkdir /deployments \
    && chown 1001 /deployments

ENV PATH="$PATH:/opt/ghc/bin"
ENV PATH=~/.cabal/bin:$PATH

COPY --chown=1001 . /deployments/

RUN cabal update
RUN cabal --force-reinstalls install happstack-server
RUN cabal --force-reinstalls install aeson
RUN cabal install svg-builder

# RUN git clone https://github.com/Happstack/happstack-server.git

COPY --chown=1001 service.hs /deployments/service.hs

EXPOSE 8084
USER 1001

ENTRYPOINT [ "/deployments/service.hs" ]

