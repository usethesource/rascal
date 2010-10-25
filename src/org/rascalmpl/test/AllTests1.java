package org.rascalmpl.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	
	AliasTests.class,
	AnnotationTests.class,
	AssignmentTests.class,
	BackTrackingTests.class,
	
	CallTests.class,
	ComprehensionTests.class,
	ConcreteSyntaxTests.class,
	DataDeclarationTests.class,
	DataTypeTests.class,
	DeclarationTests.class,
	ImportTests.class,
	RangeTests.class
        })

public class AllTests1 extends TestFramework {
// Empty
}
