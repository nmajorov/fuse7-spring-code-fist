#!/bin/sh

#add secret for redhat.io container registry

DOCKER_CONF=$HOME/.docker/config.json

 oc create secret generic rhpull-secret \
                --from-file=.dockerconfigjson=$DOCKER_CONF \
                --type=kubernetes.io/dockerconfigjson


oc secrets link builder rhpull-secret                                                                                                                                                           
                                                                                                                                                                                              
                                                                                                                                                                                              
oc secrets link default rhpull-secret --for=pull



