package org.rascalmpl.test_compiled;

import org.junit.runner.RunWith;
import org.rascalmpl.test.infrastructure.RascalJUnitParallelRecursiveCompiledTestRunner;
import org.rascalmpl.test.infrastructure.RecursiveRascalParallelTest;

@RunWith(RascalJUnitParallelRecursiveCompiledTestRunner.class)
@RecursiveRascalParallelTest({
    "lang::rascal::tests::basic", 
    "demo",
    "lang::rascal::tests::extends", 
    "lang::rascal::format", 
    "lang::rascal::tests::functionality", 
    "lang::rascal::grammar::tests",
    "lang::rascal::tests::imports",
    "lang::rascal::tests::library",
    "lang::rascal::meta", 
    "lang::rascal::syntax::tests"
    })
public class MediumSuiteParallel {
}
