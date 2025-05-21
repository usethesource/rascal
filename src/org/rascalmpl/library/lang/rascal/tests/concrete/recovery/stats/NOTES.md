# Error recovery benchmarking

The scripts in this directory can be used to benchmark error recovery of different languages.
The scripts assume the benchmarks will be run on a remote server that can be accessed using "ssh root@<host>".

The following scripts are provided:

- deploy-rascal.sh: Build and deploy a fresh rascal jar on the remote machine.
- benchmark.sh: The main benchmark script that will be run on the remote server.
- start-benchmark.sh: Run locally to start a benchmarking session on a remote machine using "screen"
- attach-remote.sh: Attach to a remotely running screen instance
- monitor-benchmarking.sh: Run a "tail -f -n 1000" remotely on a benchmark log file

## deploy-rascal.sh

This script takes two arguments, `host` and `label`. The `labe` is used to indicate what is special about this Rascal build. For example, if you are running a recovery experiment where you patched the way the recovery limit is implemented:
```deploy-rascal.sh testhost my-recov-limit```

A new Rascal jar will be build and copy to `$HOME/jars/rascal-my-recov-limit-<date>.jar`.

## benchmark.sh

The main benchmark script. It can take a number of arguments to configure the benchmark run. Note that these arguments can also be supplied to the `start-benchmark.sh` script (except for `--log`):

```text
Usage: benchmark.sh [options]
Options:
  -s, --syntax <syntax>               Syntax used (default: rascal, alternatives: java18, java15, cobol)
  -i, --test-set-id <path>            Test set under ~/test-sources/<syntax> (default: main)
  -m, --min-file-size <size>          Minimum test file size (default: 0)
  -M, --max-file-size <size>          Maximum test file size (default: 10240)
  -r, --max-recovery-attempts <limit> Maximum number of recovery attempts (default: 50)
  -t, --max-recovery-tokens <limit>   Maximum number of recovery tokens (default: 3)
  -S, --sample-window <window>        Sample window (default: 1)
  -R, --random-seed <seed>            Random seed used to determine sample within sample window (default: 0)
  -c, --count-nodes true|false        Count nodes in the parse tree (default: false)
  -L, --log                           Show log output (using 'tail -f', default: false)
```

Test set sources are located in `$HOME/test-sources/<syntax>/<testId>` on the remote server. The supported file extensions of the
test set are hardcoded based on `<syntax>`: `.rsc` for Rascal, `.java` for Java and `.CBL` for Cobol.

## start-benchmark.sh

This script is used to start a remote benchmarking run from your local machine. The first argument is the hostname on which the benchmark should be run. The rest of the arguments are the same as with `benchmark.sh`. For example:
```start-benchmark.sh testhost -s java18 -r 20 -S 10```

## attach-remote.sh

This script is mainly used for debugging these scripts. It can be used to attach to a remote screen to see what is going on in a benchmarking run. The first argument is the hostname where the benchmark is run, the rest of the arguments determine which run to monitor:
```attach-remote.sh testhost -s java18 -r 20 -S 10```

## monitor-benchmarking.sh

The `benchmark.sh` script produces a lot of useful output during a run. To follow this output in real-time, this script can be used to do a `tail -f` on the remote log file. The first argument to this script is the host name. The rest of the arguments are the same as of the `benchmark.sh` script and are used to determine which log file to show:
```monitor-benchmarking.sh testhost -s java18 -r 20 -S 10```
