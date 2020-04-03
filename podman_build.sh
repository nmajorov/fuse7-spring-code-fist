#!/bin/env bash

echo "build using podman "

TAG="6.2.2-jdk8"
CONTAINER="docker.io/library/gradle:$TAG"
CACHE_DIR="/tmp/.gradle"

mkdir -p "$CACHE_DIR"

podman  run --rm -it  \
    -v "$CACHE_DIR":/home/gradle/.gradle:Z  \
    -v "$PWD":/home/gradle/project:Z -w /home/gradle/project $CONTAINER  $@

