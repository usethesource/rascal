package test;

import junit.framework.Test;
import junit.framework.TestSuite;
public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestIO.class);
		suite.addTestSuite(TestParsing.class);
		suite.addTestSuite(DataTypeTests.class);
		suite.addTestSuite(DataDeclarationTests.class);
		suite.addTestSuite(RegExpTests.class);
		suite.addTestSuite(PatternTests.class);
		suite.addTestSuite(ComprehensionTests.class);
		suite.addTestSuite(StatementTests.class);
		suite.addTestSuite(CallTests.class);
		suite.addTestSuite(ErrorTests.class);

		suite.addTestSuite(ImportTests.class);
		suite.addTestSuite(StandardLibraryTests.class);
		
		return suite;
	  }

	  public static void main(String args[]) {
	    junit.textui.TestRunner.run(suite());
	 
	  }
}
