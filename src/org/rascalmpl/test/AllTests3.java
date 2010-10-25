package org.rascalmpl.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.rascalmpl.test.library.BooleanTests;
import org.rascalmpl.test.library.GraphTests;
import org.rascalmpl.test.library.IntegerTests;
import org.rascalmpl.test.library.ListTests;
import org.rascalmpl.test.library.MapTests;


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

