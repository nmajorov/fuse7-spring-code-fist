#!/bin/env bash


#create pod first
#sudo podman pod create -l sso --name sso73 -p 8080:8080 -p 8443:8443

#POD=sso
# USER_ID=26

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

DATA_DIR="$SCRIPT_DIR/database"

MYSQL_USER=fuse
MYSQL_PASSWORD=fuse
MYSQL_DATABASE=fuse

if [ ! -d "$DATA_DIR" ];then
     echo "create data directory"   
     mkdir -p $DATA_DIR
      setfacl -m u:$USER_ID:-wx $DATA_DIR 
fi

echo "current local  data dir for mysql  $DATA_DIR"

 
## get UID 
uid=$(id -u)

echo "run container as user $uid"

if [ -z "$1" ]
  then
    echo "No POD name as argument run standalone"
    podman run --rm -d -u $uid  --name mysql_database \
     -e MYSQL_USER=$MYSQL_USER -e MYSQL_PASSWORD=$MYSQL_PASSWORD \
     -e MYSQL_DATABASE=$MYSQL_DATABASE \
     -v $DATA_DIR:/var/lib/mysql/data:Z \
     -p 127.0.0.1:3306:3306 \
     registry.redhat.io/rhel8/mysql-80

    echo "wait database to start"
    sleep 10

    echo "check connection"
    podman exec mysql_database mysql -u "$MYSQL_USER" --password="$MYSQL_PASSWORD" --database="$MYSQL_DATABASE" --host=127.0.0.1 --execute=status
 
 else
    echo "pod name is to join  is $1"
    # run command to join the pod 
    podman run --rm -d -u $uid \
     --pod $1 \
     --name mysql_database \
     -e MYSQL_USER=$MYSQL_USER -e MYSQL_PASSWORD=$MYSQL_PASSWORD \
     -e MYSQL_DATABASE=$MYSQL_DATABASE \
     -v $DATA_DIR:/var/lib/mysql/data:Z \
     registry.redhat.io/rhel8/mysql-80
fi




#
