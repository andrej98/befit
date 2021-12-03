#!/usr/bin/env bash

# here should be all important environment variables
# that don't make sense to be put into docker-compose.yml
# (such as actual passwords for email addresses, databases, etc.)

# email created specifically for befit
# for password, ask František
export BEFIT_EMAIL_USER=befit.notify@gmail.com
export BEFIT_EMAIL_PASSWORD=

docker-compose -f docker-compose.yml up

