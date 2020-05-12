#!/bin/env bash


#get current script dir
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cd $SCRIPT_DIR

echo "run with mysql profile"
export MYSQL_SERVICE_NAME=127.0.0.1
export MYSQL_USERNAME=fuse
export MYSQL_PASSWORD=fuse
export MYSQL_DATABASE=fuse

echo $(env | grep MYSQL)

mvn  spring-boot:run -Dspring.profiles.active=mysql
