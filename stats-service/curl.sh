#!/usr/bin/env bash

set -x

DATA='
{
    "values": [1, 2, 3, 1],
    "name": "name"
}
'


curl -iw "\n" \
    --header "Content-Type: application/json" \
    --request POST \
    --data "$DATA" \
    http://localhost:8084/stats
