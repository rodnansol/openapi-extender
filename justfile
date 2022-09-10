#!/usr/bin/env just --justfile

# maven build without tests
build-all:
   mvn -DskipTests clean package

# dependencies tree for compile
dependencies:
  mvn dependency:tree -Dscope=compile > dependencies.txt

# display updates
updates:
  mvn versions:display-dependency-updates > updates.txt    
