package org.meta_environment.rascal.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.meta_environment.rascal.test.library.BooleanTests;
import org.meta_environment.rascal.test.library.GraphTests;
import org.meta_environment.rascal.test.library.IntegerTests;
import org.meta_environment.rascal.test.library.ListTests;
import org.meta_environment.rascal.test.library.MapTests;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	
	BooleanTests.class,
	GraphTests.class,
	IntegerTests.class,
	ListTests.class,
	MapTests.class
	
})

public class AllTests3 {
// Empty
}

