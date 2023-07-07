@synopsis{Provides occasionally useful access to Rascal's testing framework}
@description{
Rascal's test framework can normally be accessed via UI and commandline interfaces:

  * Running as JUnit tests in IDEs
  * Running as JUnit tests from Maven
  * Executing the `:test` command in a Rascal REPL
  
This module provides a programmatic interface, and reports the test results
as values. It can be handy to construct more UI components which interact
with tests, but also to query larger volumes of failing tests.
}
module util::Test

extend Message;

data TestResult = \testResult(str name, bool success, loc src, str message = "", list[value] exceptions = []);

@synopsis{Run all tests for the given module name}
@description{
This function works under the assumption that the named module is available in the current execution environment.
}
@javaClass{org.rascalmpl.library.util.RunTests}
@reflect{
to access interpreter functionality to run tests
}
java list[TestResult] runTests(str moduleName);

private test bool testTest() = true;

