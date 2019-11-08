#!/bin/bash

# minishift based on 3.11 is community release
# and requried to install templates

oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/7.3-GA/quickstarts/spring-boot-camel-template.json -n openshift

oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/7.3-GA/quickstarts/spring-boot-camel-rest-3scale-template.json -n openshift


oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/7.3-GA/quickstarts/spring-boot-camel-rest-sql-template.json  -n openshift





