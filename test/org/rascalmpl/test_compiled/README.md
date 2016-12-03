# Using JUnit with Compiled Rascal Code

__General__
* Make sure that your IDE is ready to run JUnit:
  (1) Do a `mvn clean install` in your Rascal project;
  (2) You have done a bootstrap yourself (experts only).
* Junit is activated inside Eclipse by running any of the TestSuites described below as JUnitTest.
* The Rascal test framework compiles test files only when needed. The first time you run a  specific TestSuite there can be a serious penalty: in particular `AllSuite` (first time: 20 minutes, otherwise: )and `CompilerSuite`.


__Available TestSuites__
* `AllSuite`: runs all available tests (over 6000 tests amounting to nearly 300.000 test executions (thanks to random generation of test arguments).
* `CompilerSuite`: tests for type checker and compiler.
* `MediumSuite`: a fast smoke test.
* `SmallSuite`: an ultra fast smoke test.
* `SyntaxSuite`: tests for syntax definitions, parser generation and parsing.

__Miscellaneous__
* The file `IGNORED.config` contains module/directory names that will be ignored