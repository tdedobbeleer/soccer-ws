#!/usr/bin/env bash

VERSION=$1-SNAPSHOT
mvn versions:set -DnewVersion=$VERSION
git commit -am "Update to version ${VERSION}"
git push origin master