#!/usr/bin/env bash
if [ ! -z "$TRAVIS_TAG" ]
then
    echo "on a tag -> set pom.xml <version> to $TRAVIS_TAG"
    mvn --settings "${TRAVIS_BUILD_DIR}/.travis/mvn-settings.xml" org.codehaus.mojo:versions-maven-plugin:2.14.2:set -DnewVersion=$TRAVIS_TAG
else
    echo "not on a tag -> keep snapshot version in pom.xml"
fi