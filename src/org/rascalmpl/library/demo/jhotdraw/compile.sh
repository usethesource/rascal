#!/bin/bash

# Set up JAVA_HOME and ANT_HOME as environment variables, 
# or they will use these hard-coded defaults
export JAVA_HOME=${JAVA_HOME:-/usr/j2sdk/}
export ANT_HOME=${ANT_HOME:-/usr/apache-ant/}

export PATH=$PATH:$ANT_HOME/bin

ant -buildfile build/BUILD.XML "$@"
