package test;

import junit.framework.Test;
import junit.framework.TestSuite;
public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(IOTests.class);
		suite.addTestSuite(ParsingTests.class);
		suite.addTestSuite(DataTypeTests.class);
		suite.addTestSuite(DataDeclarationTests.class);
		suite.addTestSuite(AssignmentTests.class);
		suite.addTestSuite(SubscriptTests.class);
		suite.addTestSuite(RegExpTests.class);
		suite.addTestSuite(PatternTests.class);
		suite.addTestSuite(ComprehensionTests.class);
		suite.addTestSuite(StatementTests.class);
		suite.addTestSuite(CallTests.class);
		suite.addTestSuite(ErrorTests.class);

		suite.addTestSuite(ImportTests.class);
		suite.addTestSuite(StandardLibraryBooleanTests.class);
		suite.addTestSuite(StandardLibraryIntegerTests.class);
		suite.addTestSuite(StandardLibraryDoubleTests.class);
		suite.addTestSuite(StandardLibraryStringTests.class);
		suite.addTestSuite(StandardLibraryListTests.class);
		suite.addTestSuite(StandardLibrarySetTests.class);
		suite.addTestSuite(StandardLibraryRelationTests.class);
		suite.addTestSuite(StandardLibraryGraphTests.class);
		
		return suite;
	  }

	  public static void main(String args[]) {
	    junit.textui.TestRunner.run(suite());
	 
	  }
}
