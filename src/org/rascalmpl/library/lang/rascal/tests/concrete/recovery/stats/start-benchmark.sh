#!/bin/sh
set -e

if [ "$#" -lt 2 ]; then
  echo "This script deploys the Rascal jar to a remote benchmarking server"
  echo "Usage:L= $0 <host> <screen-id> <benchmark-args>"
  exit 1
fi


# Start an error recovery benchmark run on a remote machine
host=$1
shift
args="$@"

# Find some characteristics in the arguments to base the screen name on
SYNTAX=rascal
MIN_FILE_SIZE=0
MAX_FILE_SIZE=10240
MAX_RECOVERY_ATTEMPTS=50
MAX_RECOVERY_TOKENS=3
SAMPLE_WINDOW=1

while [[ $# -gt 0 ]]; do
  case $1 in
    -s|--syntax)
      SYNTAX="$2"
      shift # past argument
      shift # past value
      ;;
    -m|--min-file-size)
      MIN_FILE_SIZE="$2"
      shift # past argument
      shift # past value
      ;;
    -M|--max-file-size)
      MAX_FILE_SIZE="$2"
      shift # past argument
      shift # past value
      ;;
    -r|--max-recovery-attempts)
      MAX_RECOVERY_ATTEMPTS="$2"
      shift # past argument
      shift # past value
      ;;
    -t|--max-recovery-tokens)
      MAX_RECOVERY_TOKENS="$2"
      shift # past argument
      shift # past value
      ;;
    -S|--sample-window)
      SAMPLE_WINDOW="$2"
      shift # past argument
      shift # past value
      ;;
    *)
      shift # past argument
      ;;

  esac
done

name="$SYNTAX-$MIN_FILE_SIZE-$MAX_FILE_SIZE-$MAX_RECOVERY_ATTEMPTS-$MAX_RECOVERY_TOKENS-$SAMPLE_WINDOW"

scp benchmark.sh root@$host:/tmp/benchmark.sh
ssh -t root@$host screen -L -Logfile "/tmp/screen-$name.log" -S $name "/tmp/benchmark.sh $args"
