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
COUNT_NODES=true


if [[ $1 == "-h" || $1 == "--help" ]]
then
  echo "Usage: benchmark.sh [options]"
  echo "Options:"
  echo "  -s, --syntax <syntax>               Syntax used (default: $SYNTAX, alternatives: java18, java15, cobol)"
  echo "  -i, --test-set-id <path>            Test set under ~/test-sources/<syntax> (default: $TEST_SET)"
  echo "  -m, --min-file-size <size>          Minimum test file size (default: $MIN_FILE_SIZE)"
  echo "  -M, --max-file-size <size>          Maximum test file size (default: $MAX_FILE_SIZE)"
  echo "  -r, --max-recovery-attempts <limit> Maximum number of recovery attempts (default: $MAX_RECOVERY_ATTEMPTS)"
  echo "  -t, --max-recovery-tokens <limit>   Maximum number of recovery tokens (default: $MAX_RECOVERY_TOKENS)"
  echo "  -S, --sample-window <window>        Sample window (default: $SAMPLE_WINDOW)"
  echo "  -R, --random-seed <seed>            Random seed used to determine sample within sample window (default: $RANDOM_SEED)"
  echo "  -c, --count-nodes true|false        Count nodes in the parse tree (default: $COUNT_NODES)"
  echo "  -L, --log                           Show log output (using 'tail -f', default: $SHOW_LOG)"
  exit
fi

# Pull in JARFILE, LABEL, TIME, and VERSION variables
eval `cat $HOME/.recovery-benchmark.info`
RASCAL_JAR="$HOME/jars/$JARFILE"
if [ ! -f "$RASCAL_JAR" ]
then
  echo "Rascal jar not found at $RASCAL_JAR"
  exit 1
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
    -C|--count-nodes)
      COUNT_NODES="$2"
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

LOGDIR="$HOME/logs/$SYNTAX/$TEST_SET"
STATDIR="$HOME/stats/$SYNTAX/$TEST_SET"
mkdir -p $LOGDIR
mkdir -p $STATDIR

TESTTAG="max-recovery-attempts=$MAX_RECOVERY_ATTEMPTS,max-recovery-tokens=$MAX_RECOVERY_TOKENS,sample-window=$SAMPLE_WINDOW,seed=$RANDOM_SEED"
if [ $COUNT_NODES == "true" ] || [ $COUNT_NODES == "1" ]
then
  TESTTAG="$TESTTAG,count-nodes"
fi
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
echo "SAMPLE_WINDOW=$SAMPLE_WINDOW"
echo "RANDOM_SEED=$RANDOM_SEED"
echo "COUNT_NODES=$COUNT_NODES"
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
  java -Xmx1G -jar -Drascal.monitor.batch $RASCAL_JAR $MODULE syntax=$SYNTAX source-loc="$SOURCE_LOC" max-amb-depth=2 min-file-size=$MIN_FILE_SIZE max-file-size=$MAX_FILE_SIZE max-recovery-attempts=$MAX_RECOVERY_ATTEMPTS max-recovery-tokens=$MAX_RECOVERY_TOKENS sample-window=$SAMPLE_WINDOW random-seed=$RANDOM_SEED stat-file="$STAT_LOC" count-nodes="$COUNT_NODES" >& $LOG_FILE
fi
