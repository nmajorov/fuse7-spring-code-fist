#!/bin/env bash

echo "build using podman "

TAG="6.2.2-jdk8"
CONTAINER="docker.io/library/gradle:$TAG"

podman  run --rm  -v "$PWD":/home/gradle/project:Z -w /home/gradle/project $CONTAINER  gradle build

