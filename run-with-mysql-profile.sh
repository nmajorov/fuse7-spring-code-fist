#!/bin/env bash


#get current script dir
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cd $SCRIPT_DIR

mvn  spring-boot:run -Dspring.profiles.active=mysql
