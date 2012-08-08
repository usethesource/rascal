package org.rascalmpl.test;

import org.junit.runner.RunWith;
import org.rascalmpl.test.infrastructure.RecursiveTest;
import org.rascalmpl.test.infrastructure.RecursiveTestSuite;

@RunWith(RecursiveTestSuite.class)
@RecursiveTest({"functionality", "library", "parser", "demo", "benchmark"})
public class AllSuite {
}
