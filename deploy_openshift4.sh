#!/bin/sh
# maven fabric8 doesn't support openshift4 at the moment
# so using tempplate to deploy it

# view paramaters of template
#  oc process --parameters template/s2i-fuse73-spring-boot-camel  -n openshift


oc new-app --template s2i-fuse73-spring-boot-camel \
--param MAVEN_ARGS="-DskipTests" --param GIT_REPO="https://github.com/nmajorov/integration-workshop.git" \
--context-dir=code-first-approach



