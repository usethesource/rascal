#!/bin/bash
set -e

SYNTAX=rascal
TEST_SET=main
MIN_FILE_SIZE=0
MAX_FILE_SIZE=10240
MAX_RECOVERY_ATTEMPTS=50
MAX_RECOVERY_TOKENS=3
SKIP_WINDOW=2048
SAMPLE_WINDOW=1
RANDOM_SEED=0
SHOW_LOG=false

# Pull in JARFILE, LABEL, TIME, and VERSION variables
eval `cat $HOME/.recovery-benchmark.info`
RASCAL_JAR="$HOME/jars/$JARFILE"
if [ ! -f "$RASCAL_JAR" ]
then
  echo "Rascal jar not found at $RASCAL_JAR"
  exit 1
fi

if [[ $1 == "-h" || $1 == "--help" ]]
then
  echo "Usage: benchmark.sh [options]"
  echo "Options:"
  echo "  -s, --syntax <syntax>               Specify the syntax (default: rascal, alternatives: java18, java15, cobol)"
  echo "  -i, --test-set-id <path>            Specify the source set under ~/test-sources/<syntax> (default: main)"
  echo "  -m, --min-file-size <size>          Specify the minimum file size (default: 0)"
  echo "  -M, --max-file-size <size>          Specify the maximum file size (default: 10240)"
  echo "  -r, --max-recovery-attempts <limit> Specify the maximum number of recovery attempts (default: 50)"
  echo "  -t, --max-recovery-tokens <limit>   Specify the maximum number of recovery tokens (default: 3)"
  echo "  -w, --skip-window <window>          Specify the skip window (default: 2048)"
  echo "  -S, --sample-window <window>        Specify the sample window (default: 1)"
  echo "  -R, --random-seed <seed>            Specify the random seed (default: 0)"
  echo "  -L, --log                           Show log output (using 'tail -f')"
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
    -i|--test-set-id)
      TEST_SET="$2"
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
    -w|--skip-window)
      SKIP_WINDOW="$2"
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

SOURCE_LOC="|home:///test-sources/$SYNTAX/$TEST_SET|"

# Rascal example:
# - Working directory is $HOME/rascal-test-sources, this script is on $PATH
# - Run this script: "benchmark.sh -i typepal -l '|home:///rascal-test-sources/typepal|'
# - Log will be written to $HOME/logs/rascal/typepal/log-$VERSION-0-10240:max-recovery-tokens=3,skip-window=2048,recov-limit=50.txt
# - Stats will be written to $HOME/stats/rascal/typepal/stats-$VERSION-0-10240-max-recovery-tokens=3,skip-window=2048,recov-limit=50,sample-window=1,seed=0.txt
#
#
# Java example:
# - Working directory is java-air directory, this script is on $PATH
# - Run this script: "benchmark.sh -s java18 -i m3 -l '|cwd:///src/lang/java/m3|'"
# - Log will be written to $HOME/logs/java18/m3/log-$VERSION-0-10240:max-recovery-tokens=3,skip-window=2048,recov-limit=50.txt
# - Stats will be written to $HOME/stats/java18/m3/stats-$VERSION-0-10240:max-recovery-tokens=3,skip-window=2048,recov-limit=50,sample-window=1,seed=0.txt
#

# This is passed on as environment variable (only supported in debug builds)
export RASCAL_RECOVERER_SKIP_WINDOW=$SKIP_WINDOW

LOGDIR="$HOME/logs/$SYNTAX/$TEST_SET"
STATDIR="$HOME/stats/$SYNTAX/$TEST_SET"
mkdir -p $LOGDIR
mkdir -p $STATDIR

TESTTAG="max-recovery-attempts=$MAX_RECOVERY_ATTEMPTS,max-recovery-tokens=$MAX_RECOVERY_TOKENS,skip-window=$RASCAL_RECOVERER_SKIP_WINDOW,sample-window=$SAMPLE_WINDOW,seed=$RANDOM_SEED"
RUNID="$LABEL-$MIN_FILE_SIZE-$MAX_FILE_SIZE,$TESTTAG"

if [ -z "${SOURCE_LOC}" ]
then
  SOURCE_LOC="|home:///test-sources/$SYNTAX|"
fi

LOG_FILE="$LOGDIR/log-$RUNID.txt"
STAT_LOC="|home:///stats/$SYNTAX/$TEST_SET/stats-$RUNID.txt|"

echo "SETTINGS USED:"
echo "SYNTAX=$SYNTAX"
echo "SOURCE_LOC=$SOURCE_LOC"
echo "MIN_FILE_SIZE=$MIN_FILE_SIZE"
echo "MAX_FILE_SIZE=$MAX_FILE_SIZE"
echo "MAX_RECOVERY_ATTEMPTS=$MAX_RECOVERY_ATTEMPTS"
echo "MAX_RECOVERY_TOKENS=$MAX_RECOVERY_TOKENS"
echo "SKIP_WINDOW=$SKIP_WINDOW"
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
  java -Xmx1G -jar -Drascal.monitor.batch $RASCAL_JAR $MODULE syntax=$SYNTAX source-loc="$SOURCE_LOC" max-amb-depth=2 min-file-size=$MIN_FILE_SIZE max-file-size=$MAX_FILE_SIZE max-recovery-attempts=$MAX_RECOVERY_ATTEMPTS max-recovery-tokens=$MAX_RECOVERY_TOKENS sample-window=$SAMPLE_WINDOW random-seed=$RANDOM_SEED stat-file="$STAT_LOC" >& $LOG_FILE
fi
