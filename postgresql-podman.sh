#!/bin/env bash

# run POSTGRESQL DATABASE with podman as standalone or in the pod

#create pod first
# for example combination with RedHat sso
#sudo podman pod create -l sso --name sso73 -p 8080:8080 -p 8443:8443

#POD=sso
uid=$(id -u)
USER_ID=uid

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

DATA_DIR="$SCRIPT_DIR/pgdatabase"

POSTGRESQL_USER=fuse
POSTGRESQL_PASSWORD=fuse
POSTGRESQL_DATABASE=fuse

if [ ! -d "$DATA_DIR" ];then
     echo "create data directory"   
     mkdir -p $DATA_DIR
      setfacl -m u:$USER_ID:-wx $DATA_DIR 
fi

echo "current local  data dir for database storage  $DATA_DIR"

 
## get UID 
uid=$(id -u)

echo "run container as user $uid"

if [ -z "$1" ]
  then
    echo "No POD name as argument run standalone"
    podman run --rm -d -u $uid  --name  postgresql-database \
     -e POSTGRESQL_USER=$POSTGRESQL_USER -e POSTGRESQL_PASSWORD=$POSTGRESQL_PASSWORD \
     -e POSTGRESQL_DATABASE=$POSTGRESQL_DATABASE \
     -v $DATA_DIR:/var/lib/pgsql/data:Z \
     -p 127.0.0.1:5432:5432 \
     centos/postgresql-96-centos7

    echo "wait database to start"
    sleep 15

    echo "check connection"
    podman exec  postgresql-database pg_isready
 
 else
    echo "pod name is to join  is $1"
    # run command to join the pod 
    podman run --rm -d -u $uid \
     --pod $1 \
     --name  postgresql-database \
     -e POSTGRESQL_USER=$POSTGRESQL_USER -e POSTGRESQL_PASSWORD=$POSTGRESQL_PASSWORD \
     -e POSTGRESQL_DATABASE=$POSTGRESQL_DATABASE \
     -v $DATA_DIR:/var/lib/pgsql/data:Z \
     -p 127.0.0.1:5432:5432 \
     centos/postgresql-96-centos7
fi




#
