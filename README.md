# jsr223-docker-compose
[![Build Status](http://jenkins.activeeon.com/buildStatus/icon?job=SE-docker)](http://jenkins.activeeon.com/job/SE-docker/)

Execute a docker compose script; invoke the local docker compose client through a java JSR 223
script engine.

## Build
Run ./gradlew to create a JAR.

## Usage
Add JAR to classpath; it will make the script engine discoverable with "docker-compose" as a
script engine name. More information [here](http://docs.oracle.com/javase/6/docs/technotes/guides/scripting/programmer_guide/index.html).

## How it works
The script engine takes a Reader or String which contains the docker-compose yaml file.
That yaml file will be written to disk and variables will be replaced. After that docker-compose will
be executed with the configuration file.

## Bindings
Bindings are used to have variables inside compose scripts.
