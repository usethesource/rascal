#!/bin/sh
# This script was used to generate "expected-path-list.txt"
# When run in this directory it uses the "pom.xml" in this directory which is a frozen rascal pom to have a solid anchor.
# We will only need to run this script again if we ever decide to change the "anchor" pom.
CLASSFILE=`mktemp`
# Use REPODEf if you want to do a run with an alternate (for instance empty) local repo 
#REPODEF="-Dmaven.repo.local=D:\\tmp\\repo2"
mvn --quiet dependency:build-classpath ${REPODEF} -DincludeScope=compile -Dmdep.outputFile=$CLASSFILE
REPO=`mvn help:evaluate $REPODEF -Dexpression=settings.localRepository -q -DforceStdout`
PREFIX=$((${#REPO} + 2))
tr ';' '\n' < $CLASSFILE | cut -c ${PREFIX}- | tr '\\' '/' | sort > expected-path-list.txt
rm $CLASSFILE
