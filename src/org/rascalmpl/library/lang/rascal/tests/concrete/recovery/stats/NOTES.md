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

This script takes two arguments, `host` and `tag`. The `tag` is a label to indicate what is special about this Rascal build. For example, if you are running a recovery experiment where you patched the way the recovery limit is implemented:
```deploy-rascal.sh testhost my-recov-limit```

A new Rascal jar will be build and copy to `$HOME/jars/rascal-my-recov-limit-<date>.jar`.

## benchmark.sh

The main benchmark script. It takes a number of arguments. Note that these arguments can also be supplied to the `start-benchmark.sh` script:

```text
  -s, --syntax <syntax>        Specify the syntax (default: rascal, alternatives: java18, java15, cobol)
  -l, --source-loc <loc>       Specify the source location (default: |home:///test-sources/<syntax>|)
  -m, --min-file-size <size>   Specify the minimum file size (default: 0)
  -M, --max-file-size <size>   Specify the maximum file size (default: 10240)
  -s, --skip-limit <limit>     Specify the skip limit (default: 3)
  -w, --skip-window <window>   Specify the skip window (default: 2048)
  -r, --recov-limit <limit>    Specify the recovery limit (default: 50)
  -S, --sample-window <window> Specify the sample window (default: 1)
  -R, --random-seed <seed>     Specify the random seed (default: 0)
  -L, --log                    Show log output using "tail -f"
```

## start-benchmark.sh

This script is used to start a remote benchmarking run from your local machine. The first two arguments are the hostname and the id of the screen session to create. The rest of the arguments are the same as with `benchmark.sh`. For example:
```start-benchmark.sh testhost recov-20 -s java18 -r 20 -S 10```

## attach-remote.sh

This script is mainly used for debugging these scripts. It can be used to attach to a remote screen to see what is going on in a benchmarking run:
```attach-remote.sh testhost recov-20```

## monitor-benchmarking.sh

The `benchmark.sh` script produces a lot of useful output during a run. To follow this output in real-time, this script can be used to do a `tail -f` on the remote log file. The first argument to this script is the host name. The rest of the arguments are the same as of the `benchmark.sh` script and are used to determine which log file to show:
```monitor-benchmarking.sh testhost -s java18 -r 20 -S 10```

# TODO

- Ignore files with parse errors
- Automatically determine screen id for simplicity?
- Test stats caching
- Implement memory stats caching
- Improve/consolidate graph script
- Test with COBOL
- Graph Rascal stats