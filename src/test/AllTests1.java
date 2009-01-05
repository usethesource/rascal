package test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests1 {

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTestSuite(AssignmentTests.class);
		suite.addTestSuite(CallTests.class);
		suite.addTestSuite(ComprehensionTests.class);
		suite.addTestSuite(DataDeclarationTests.class);
		suite.addTestSuite(DataTypeTests.class);
		suite.addTestSuite(ErrorTests.class);
		suite.addTestSuite(ImportTests.class);

		return suite;
	}

	public void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}
}
