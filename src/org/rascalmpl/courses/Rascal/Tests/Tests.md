---
title: Tests
---

#### Synopsis

A unit testing and regressing testing framework is builtin to the Rascal language

#### Description

To write a test in Rascal, use the `test` modifier on a parametrized ((Function)) declaration:

```rascal
test bool integerAdditionIsCommutative(int a, int b) { // <1>
    return a + b == b + a; // <2>
}

test bool integerMultiplicationIsCommutative(int a, int b)
    = a * b == b * a; // <3>
```

* <1> a function header defines test parameters for which a few hundred examples will be **randomly generated**. The name of the function services as the name of the test
   * You can call a test function like any other function
   * Test functions are not to be overloaded. Use unique names for unique tests. 
* <2> the function returns `true` or `false` as the "test", but you can also _not throw an exception_ for success and ((Statements-Throw)) one for failure.
* <3> all sorts of ((Declarations-Function)) are allowed.
* For complex parameter types like ((AlgebraicDataType))s also random inputs will be generated that satisfy the grammar.
* For functions with multiple parameters, sometimes the random code generator will provide structurally equal parameters for successive arguments if the types allow this, or even values shared in memory. 
* Also the "random" input generator may specialize towards patterns that are found in the bodies of the functions-under-test, to achieve coverage faster. In other words, neither coverage nor uniform randomness is guaranteed. Expect something in between.

#### Running Tests

This command will run all tested in the imported modules and also any locally defined tests:

```rascal
rascal>:test
```

Then there is the `RascalJUnitTestRunner` and `RascalJUnitParallelRecursiveTestRunner` Java classes which you
can use to annotate your own test class which will then load the given test modules:

```java
package org.rascalmpl.test.basic;

import org.junit.runner.RunWith;
import org.rascalmpl.test.infrastructure.RascalJUnitTestPrefix;
import org.rascalmpl.test.infrastructure.RascalJUnitTestRunner;

@RunWith(RascalJUnitTestRunner.class)                // <1>
@RascalJUnitTestPrefix("lang::rascal::tests::basic") // <2>
public class RunRascalTestModules { }
```

* <1> selects the test runner (parallel or not)
* <2> selects the root package to search for modules and tests
* Then use JUnit configuration in Eclipse, VScode or Maven to activate the tests at the right time.
* Reporting goes through standard JUnit reporting, so this integrates well with IDEs and continuous integration systems like Jenkins and GitHub Actions. 


