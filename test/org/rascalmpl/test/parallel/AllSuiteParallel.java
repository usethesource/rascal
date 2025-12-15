package org.rascalmpl.test.parallel;

import org.junit.runner.RunWith;
import org.rascalmpl.test.infrastructure.RascalJUnitParallelRecursiveTestRunner;
import org.rascalmpl.test.infrastructure.RecursiveRascalParallelTest;

@RunWith(RascalJUnitParallelRecursiveTestRunner.class)
@RecursiveRascalParallelTest({
    "lang::rascal::tests::basic",
    "lang::rascal::tests::concrete",
    "lang::rascal::tests::extends",
    "lang::rascal::tests::demo",
    "lang::rascal::tests::functionality", 
    "lang::rascal::tests::imports",
    "lang::rascal::tests::libraries"
    })
public class AllSuiteParallel {
}
