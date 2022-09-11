#!/usr/bin/env just --justfile

# Build project
build-project:
  ./mvnw clean install

# Build samples
build-samples:
  ./mvnw clean package -f samples/spring-boot-openmapi-with-test/pom.xml -X

# Build everything
build-all: build-project build-samples

# dependencies tree for compile
dependencies:
  mvn dependency:tree -Dscope=compile > dependencies.txt

# display updates
updates:
  mvn versions:display-dependency-updates > updates.txt    
