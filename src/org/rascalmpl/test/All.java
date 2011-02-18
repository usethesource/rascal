package org.rascalmpl.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AllTests1.class,
	AllTests2.class,
	AllTests3.class,
	AllTests4.class,
	ConcreteSyntaxTests.class,
	AllBenchmarks.class,
	AllDemoTests.class
        })
public class All {
// Empty
}
