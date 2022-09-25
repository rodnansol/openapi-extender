#!/usr/bin/env just --justfile
set export

home_dir  := env_var('HOME')
JAVA_HOME := home_dir + "/.sdkman/candidates/java/current"

# Build project
build-project:
  ./mvnw clean install

# Build samples
build-samples:
  ./mvnw clean package -f samples/pom.xml

# Build everything
build-all: build-project build-samples

# dependencies tree for compile
dependencies:
  mvn dependency:tree -Dscope=compile > dependencies.txt

# display updates
updates:
  mvn versions:display-dependency-updates > updates.txt

deploy-to-ossrh:
  mvn -Prelease clean verify deploy

full-release:
  mvn release:prepare release:perform -Prelease -DreleaseVersion=0.1.1 -DdevelopmentVersion=999-SNAPSHOT

jrelease:
  mvn versions:set -DnewVersion=0.1.1.1
  mvn -Prelease jreleaser:release -N
  mvn versions:set -DnewVersion=999-SNAPSHOT
