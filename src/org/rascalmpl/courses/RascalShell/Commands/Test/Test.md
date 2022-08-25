# Test Command

.Synopsis
Run tests.

.Syntax
* `:test`

.Description

Run Rascal tests. The tests in all currently imported modules are executed and the results are reported
in the terminal.

.Examples

Execute the tests in an imported module:

[source,rascal-shell]
----
import demo::basic::Factorial;
test
----

Execute the tests in the `Integers` module in the Rascal test suite:
[source,rascal-shell]
----
test lang::rascal::tests::basic::Integers
----
