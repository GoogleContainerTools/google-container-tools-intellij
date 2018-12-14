#!/bin/bash
# Copyright 2018, Google Inc.
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
#     * Redistributions of source code must retain the above copyright
# notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above
# copyright notice, this list of conditions and the following disclaimer
# in the documentation and/or other materials provided with the
# distribution.
#     * Neither the name of Google Inc. nor the names of its
# contributors may be used to endorse or promote products derived from
# this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
# "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
# LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
# A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
# OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
# SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
# LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
# DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
# THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
# (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
# OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

## Release script that publishes our plugins to Github and Jetbrains plugin repository

## Export required environment variables
ANALYTICS_ID_PATH=${KOKORO_KEYSTORE_DIR}/72743_analytics_id
GITHUB_TOKEN_PATH=${KOKORO_KEYSTORE_DIR}/72743_paflynn_github_token
IJ_REPO_USERNAME_PATH=${KOKORO_KEYSTORE_DIR}/72743_ij_repo_username
IJ_REPO_PASSWORD_PATH=${KOKORO_KEYSTORE_DIR}/72743_ij_repo_password
export ANALYTICS_ID=$(cat ANALYTICS_ID_PATH)
export GITHUB_TOKEN=$(cat GITHUB_TOKEN_PATH)
export IJ_REPO_USERNAME=$(cat IJ_REPO_USERNAME_PATH)
export IJ_REPO_PASSWORD=$(cat IJ_REPO_PASSWORD_PATH)

cd github/google-container-tools-intellij

export SOURCE_VERSION=$(sed -n 's/version[ tab]*=[ tab]*\(.*\)/\1/p' gradle.properties)
[ -z "$SOURCE_VERSION" ] && \
    echo "ERROR: gradle.properties could not be parsed. Perhaps we're not in the project root?"\
     "$(pwd)" && exit 1;
export GIT_TAG_NAME=$(git describe)
[ -z "$GIT_TAG_NAME" ] && \
    echo "ERROR: 'git describe' failed. We're probably not in a git workspace."\
     && exit 1;
export TAG_VERSION=${GIT_TAG_NAME:1}

if [ "$SOURCE_VERSION" != "$TAG_VERSION" ]
    then
        echo "ERROR: The version ($SOURCE_VERSION) configured in gradle.properties does not match"\
         "the version ($TAG_VERSION) in the git tag."
        echo "Failing release.."
        exit 1 # terminate and indicate error
fi

echo "Installing itchio/gothub.."
sudo /usr/local/go/bin/go get github.com/itchio/gothub
echo "Building plugin"
./gradlew buildPlugin

echo "Creating Github release for tag: $GIT_TAG_NAME"

## GITHUB_USER and GITHUB_REPO are used by gothub command.
export GITHUB_USER=GoogleContainerTools
export GITHUB_REPO=google-container-tools-intellij

if [[ $GIT_TAG_NAME =~ RC[0-9]+$ ]]
    then
        # Release candidates are marked as pre-releases.
        gothub release --tag $GIT_TAG_NAME --pre-release
    else
        gothub release --tag $GIT_TAG_NAME
fi

echo "Uploading Google Cloud Tools Plugin artifact to release $GIT_TAG_NAME"
gothub upload --tag $GIT_TAG_NAME --file \
 build/distributions/google-container-tools-intellij-${VERSION}.zip \
 --name google-container-tools-intellij-${VERSION}.zip
echo "Upload complete."

echo "Publishing plugin to Jetbrains plugin repository"
./gradlew :publishPlugin
