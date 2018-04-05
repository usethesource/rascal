package org.rascalmpl.test_compiled;

import org.junit.runner.RunWith;
import org.rascalmpl.test.infrastructure.RascalJUnitParallelRecursiveCompiledTestRunner;
import org.rascalmpl.test.infrastructure.RecursiveRascalParallelTest;

@RunWith(RascalJUnitParallelRecursiveCompiledTestRunner.class)
@RecursiveRascalParallelTest({
    "lang::rascal::tests::basic", 
    "demo",
    "lang::rascal::tests::extends", 
    "lang::rascal::tests::functionality", 
    "lang::rascal::tests::imports",
    "lang::rascal::tests::library"
    })
public class SmallSuiteParallel {
}
