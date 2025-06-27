#!/bin/sh
set -e
if [ "$#" -lt 2 ]; then
  echo "This script deploys the Rascal jar to a remote benchmarking server"
  echo "Usage:L= $0 <host> <label>"
  exit 1
fi
HOST=$1
LABEL=$2

# Change to the Rascal root directory
cd "$(dirname "$0")/../../../../../../../../../.."

RASCAL_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

echo "Building rascal version: $RASCAL_VERSION"
RASCAL_JAR=target/rascal-$RASCAL_VERSION.jar
mvn package -Drascal.compile.skip -Drascal.tutor.skip -DskipTests

TIME=`date "+%Y-%m-%d-%H-%M"`
JARFILE="rascal-$RASCAL_VERSION-$LABEL-$TIME.jar"
DEST="root@$HOST:~/jars/$JARFILE"
echo "Deploying to $DEST"
scp "$RASCAL_JAR" "$DEST"

TEMPFILE=`mktemp`
echo "JARFILE=$JARFILE" >> $TEMPFILE
echo "LABEL=$LABEL" >> $TEMPFILE
echo "TIME=$TIME" >> $TEMPFILE
echo "VERSION=$RASCAL_VERSION" >> $TEMPFILE

INFO="root@$HOST:~/.recovery-benchmark.info"
echo "Writing build info to $INFO"
cat "$TEMPFILE"
scp "$TEMPFILE" "$INFO"
rm "$TEMPFILE"
