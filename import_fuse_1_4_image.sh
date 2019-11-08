#!/bin/sh

#import fuse image in the current namespace

oc import-image fuse-java-openshift:1.4  --from=registry.redhat.io/fuse7/fuse-java-openshift:1.4 --confirm



