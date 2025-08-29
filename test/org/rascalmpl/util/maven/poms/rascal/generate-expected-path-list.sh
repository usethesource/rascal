#!/bin/sh
CLASSFILE=`mktemp`
mvn dependency:build-classpath -DincludeScope=compile -Dmdep.outputFile=$CLASSFILE
REPO=`mvn help:evaluate -Dexpression=settings.localRepository -q -DforceStdout`
PREFIX=$((${#REPO} + 2))
tr ';' '\n' < $CLASSFILE | cut -c ${PREFIX}- | sort > expected-path-list.txt
rm $CLASSFILE
