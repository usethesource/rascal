
# Error recovery benchmark

Next to some basic integration tests, this directory contains a simple error recovery benchmark (see `ErrorRecoveryBenchmark.rsc`).
This benchmark is meant to provide feedback on the quality and speed of error recovery and to be able to track progress in these areas.

The benchmark tests error recovery for a number of languages by performing modifications on a valid input file and attempting to parse the result.
Currently only two types of modifications are implemented:

- Deletion of single characters
- Deletion until the next end-of-line character

We anticipate more types of modifications later, possibly based on the parse tree instead of the raw source file.

Note that a benchmark test is documented by a string of characters including:

- `.`: After modification the input still parses without errors
- '+': After modification error recovery is able to recover by skipping a reasonable amount of input
- '-': After modification error recovery is able to recover by skipping an excessive amount of input (>25% of the contents of the file)
- '?': After modification parsing results in an unrecoverable parse error
- A newline is printed after each line of the input file

Besides the ouptut, files with benchmark measurements can also be generated in CSV format with one measurement per line. To generate this file, specify its location as a `loc` in the environment variable `STATFILE`.
