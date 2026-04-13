#!/bin/sh
# Attach to a remote screen
set -e
if [ "$#" -lt 1 ]; then
  echo "This script shows remote benchmark logs"
  echo "Usage: $0 <host> <benchmark-args>"
  exit 1
fi

host=$1
shift

ssh -t root@$host /tmp/benchmark.sh -L $@

