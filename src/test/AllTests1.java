package test;

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
	DataDeclarationTests.class,
	DataTypeTests.class,
	ImportTests.class
        })

public class AllTests1 extends TestFramework {

}
