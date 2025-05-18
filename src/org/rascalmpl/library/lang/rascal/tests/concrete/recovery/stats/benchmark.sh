#!/bin/bash
set -e

SYNTAX=rascal
SOURCE_LOC=
MIN_FILE_SIZE=0
MAX_FILE_SIZE=10240
SKIP_LIMIT=3
SKIP_WINDOW=2048
RECOV_LIMIT=50
SAMPLE_WINDOW=1
RANDOM_SEED=0
SHOW_LOG=false

# Pull in JARFILE, LABEL, TIME, and VERSION variables
eval `cat $HOME/.recovery-benchmark.info`
RASCAL_JAR="$HOME/jars/$JARFILE"

if [[ $1 == "-h" || $1 == "--help" ]]
then
  echo "Usage: benchmark.sh [options]"
  echo "Options:"
  echo "  -s, --syntax <syntax>        Specify the syntax (default: rascal, alternatives: java18, java15, cobol)"
  echo "  -l, --source-loc <loc>       Specify the source location (default: |home:///test-sources/<syntax>|)"
  echo "  -m, --min-file-size <size>   Specify the minimum file size (default: 0)"
  echo "  -M, --max-file-size <size>   Specify the maximum file size (default: 10240)"
  echo "  -s, --skip-limit <limit>     Specify the skip limit (default: 3)"
  echo "  -w, --skip-window <window>   Specify the skip window (default: 2048)"
  echo "  -r, --recov-limit <limit>    Specify the recovery limit (default: 50)"
  echo "  -S, --sample-window <window> Specify the sample window (default: 1)"
  echo "  -R, --random-seed <seed>     Specify the random seed (default: 0)"
  echo "  -L, --log                    Show log output (using 'tail -f')"
  exit
fi


ARGS=()

while [[ $# -gt 0 ]]; do
  case $1 in
    -s|--syntax)
      SYNTAX="$2"
      shift # past argument
      shift # past value
      ;;
    #-i|--testid)
    #  TESTID="$2"
    #  shift # past argument
    #  shift # past value
    #  ;;
    -l|--source-loc)
      SOURCE_LOC="$2"
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
    -s|--skip-limit)
      SKIP_LIMIT="$2"
      shift # past argument
      shift # past value
      ;;
    -w|--skip-window)
      SKIP_WINDOW="$2"
      shift # past argument
      shift # past value
      ;;
    -r|--recov-limit)
      RECOV_LIMIT="$2"
      shift # past argument
      shift # past value
      ;;
    -S|--sample-window)
      SAMPLE_WINDOW="$2"
      shift # past argument
      shift # past value
      ;;
    -R|--random-seed)
      RANDOM_SEED="$2"
      shift # past argument
      shift # past value
      ;;
    #-t|--test)
    #  setTestVars $2
    #  shift
    # shift
    #  ;;
    -L|--log)
      SHOW_LOG=true
      shift  
    ;;
    *)
      ARGS+=("$1") # save positional arg
      shift # past argument
      ;;
  esac
done

#if [ -z "${TESTID}" ]
#then
#	echo "Must specify --testid (or -i)"
#fi

# Rascal example:
# - Working directory is $HOME/rascal-test-sources, this script is on $PATH
# - Run this script: "benchmark.sh -i typepal -l '|home:///rascal-test-sources/typepal|'
# - Log will be written to $HOME/logs/rascal/typepal/log-$VERSION-0-10240:skip-limit=3,skip-window=2048,recov-limit=50.txt
# - Stats will be written to $HOME/stats/rascal/typepal/stats-$VERSION-0-10240-skip-limit=3,skip-window=2048,recov-limit=50,sample-window=1,seed=0.txt
#
#
# Java example:
# - Working directory is java-air directory, this script is on $PATH
# - Run this script: "benchmark.sh -s java18 -i m3 -l '|cwd:///src/lang/java/m3|'"
# - Log will be written to $HOME/logs/java18/m3/log-$VERSION-0-10240:skip-limit=3,skip-window=2048,recov-limit=50.txt
# - Stats will be written to $HOME/stats/java18/m3/stats-$VERSION-0-10240:skip-limit=3,skip-window=2048,recov-limit=50,sample-window=1,seed=0.txt
#

# These are passed on as environment variables
export RASCAL_RECOVERER_SKIP_LIMIT=$SKIP_LIMIT
export RASCAL_RECOVERER_SKIP_WINDOW=$SKIP_WINDOW
export RASCAL_RECOVERER_RECOVERY_LIMIT=$RECOV_LIMIT

LOGDIR="$HOME/logs/$SYNTAX"
STATDIR="$HOME/stats/$SYNTAX"
mkdir -p $LOGDIR
mkdir -p $STATDIR

TESTTAG="skip-limit=$RASCAL_RECOVERER_SKIP_LIMIT,skip-window=$RASCAL_RECOVERER_SKIP_WINDOW,recovery-limit=$RASCAL_RECOVERER_RECOVERY_LIMIT,sample-window=$SAMPLE_WINDOW,seed=$RANDOM_SEED"
RUNID="$LABEL-$VERSION-$MIN_FILE_SIZE-$MAX_FILE_SIZE:$TESTTAG"

if [ -z "${SOURCE_LOC}" ]
then
  SOURCE_LOC="|home:///test-sources/$SYNTAX|"
fi

LOG_FILE="$LOGDIR/log-$RUNID.txt"
STAT_LOC="|home:///stats/$SYNTAX/$TESTID/stats-$RUNID.txt|"

echo "SETTINGS USED:"
echo "SYNTAX=$SYNTAX"
echo "SOURCE_LOC=$SOURCE_LOC"
echo "MIN_FILE_SIZE=$MIN_FILE_SIZE"
echo "MAX_FILE_SIZE=$MAX_FILE_SIZE"
echo "SKIP_LIMIT=$SKIP_LIMIT"
echo "SKIP_WINDOW=$SKIP_WINDOW"
echo "RECOV_LIMIT=$RECOV_LIMIT"
echo "SAMPLE_WINDOW=$SAMPLE_WINDOW"
echo "RANDOM_SEED=$RANDOM_SEED"
echo "LOG_FILE=$LOG_FILE"
echo "STAT_LOC=$STAT_LOC"

if [ "$SYNTAX" = "java18" ] || [ "$SYNTAX" = "java15" ] 
then
  cd java-air
elif [ "$SYNTAX" = "cobol" ]
then
  cd cobol-air
fi

if [ "$SHOW_LOG" = "true" ]
then
  tail -n 1000 -f $LOG_FILE
else
  echo "Starting benchmark, logging to $LOG_FILE"
  MODULE=lang::rascal::tests::concrete::recovery::ErrorRecoveryBenchmark
  java -Xmx1G -jar -Drascal.monitor.batch $RASCAL_JAR $MODULE syntax=$SYNTAX source-loc="$SOURCE_LOC" max-amb-depth=2 min-file-size=$MIN_FILE_SIZE max-file-size=$MAX_FILE_SIZE sample-window=$SAMPLE_WINDOW random-seed=$RANDOM_SEED stat-file="$STAT_LOC" >& $LOG_FILE
fi

