package org.meta_environment.rascal.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.meta_environment.rascal.test.StandardLibraryTests.BooleanTests;
import org.meta_environment.rascal.test.StandardLibraryTests.GraphTests;
import org.meta_environment.rascal.test.StandardLibraryTests.IntegerTests;
import org.meta_environment.rascal.test.StandardLibraryTests.ListTests;
import org.meta_environment.rascal.test.StandardLibraryTests.MapTests;


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

