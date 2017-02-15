# Using JUnit with Compiled Rascal Code

__General__
* Make sure that your IDE is ready to run JUnit:
  (1) Do a `mvn clean install` in your Rascal project; or
  (2) You have done a bootstrap yourself (experts only).
* Junit is activated inside Eclipse by running any of the TestSuites described below as JUnitTest: mouse over the desired TestSuite, right click and select `Run As -> JUnit Test`.
* The Rascal test framework compiles test files only when needed. The first time you run a  specific TestSuite there can be a serious penalty: in particular `AllSuite` (first time: 20 min., otherwise: 6 min.) and `CompilerSuite`.


__Available TestSuites__
* `AllSuite`: runs all available tests -- over 6000 tests amounting to nearly 300.000 test executions (thanks to random generation of test arguments): _6 min_.
* `CompilerSuite`: tests for type checker and compiler: _1.5 min_.
* `MediumSuite`: a fast smoke test: _3.5 min_.
* `SmallSuite`: a faster smoke test: _1 min_. 
* `SyntaxSuite`: tests for syntax definitions, parser generation and parsing:  _2.5 min_.

__Miscellaneous__
* The file `TESTS.ignored` contains directory names to be ignored.
  Names may contain `/` or `::` as separator. 
  Each name should occur on a separate line and maybe followed by a comment (using `//`)
  and is used in a __substring__ check. A single name can thus suppress complete subdirectories
  therefore you can better make names as specific as possible/desired.

__Notes__
* All mentioned times measured on a 2013 MacBook Pro.