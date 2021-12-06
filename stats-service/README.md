# stats-service

## REST-API

- `http://localhost:8084/ping` - simple ping

- `http://localhost:8084/test` - show example graph

- `http://localhost:8084/stats`

    - show graph from given data

    - method: `POST`

    ```json
    {
        "values": [1, 2, 3, 1],
        "name": "name"
    }

    ```

## Dependencies

```bash
sudo apt install ghc \
    cabal-install

cabal update

cabal install happstack-server
cabal --force-reinstalls install aeson
cabal install svg-builder
```

