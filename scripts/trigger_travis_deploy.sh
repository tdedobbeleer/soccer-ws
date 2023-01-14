#!/bin/bash

echo "TRAVIS_BRANCH=$TRAVIS_BRANCH TRAVIS_PULL_REQUEST=$TRAVIS_PULL_REQUEST TRAVIS_TAG=$TRAVIS_TAG"
if [[ -n "$TRAVIS_TAG" ]] ; then
  curl -LO --retry 3 https://raw.githubusercontent.com/plume-lib/trigger-travis/master/trigger-travis.sh
  sh trigger-travis.sh --pro tdedobbeleer soccer-ws-docker $TRAVIS_ACCESS_TOKEN
else
echo "trigger-travis.sh did not run"
fi