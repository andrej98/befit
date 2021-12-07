FROM haskell:9.0.1

RUN cabal update

RUN mkdir -p /root/.cabal
RUN mkdir -p /.cabal

WORKDIR /root/.cabal
WORKDIR /.cabal

RUN chown -R 1001:1001 "/root/.cabal/"
RUN chown -R 1001:1001 "/.cabal/"

COPY --chown=1001 * /app/app/

WORKDIR /app/app/

RUN cabal v2-install --install-method=copy --installdir=./ "stats-service"

EXPOSE 8084
USER 1001

ENTRYPOINT [ "./stats-service" ]
