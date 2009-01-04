package test;

import junit.framework.Test;
import junit.framework.TestSuite;
public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(AssignmentTests.class);
		suite.addTestSuite(CallTests.class);
		suite.addTestSuite(ComprehensionTests.class);
		suite.addTestSuite(DataDeclarationTests.class);
		suite.addTestSuite(DataTypeTests.class);
		suite.addTestSuite(ErrorTests.class);
		suite.addTestSuite(ImportTests.class);
		
		suite.addTestSuite(IOTests.class);
		suite.addTestSuite(ParsingTests.class);
		suite.addTestSuite(PatternTests.class);
		suite.addTestSuite(RegExpTests.class);
		suite.addTestSuite(RuleTests.class);
		suite.addTestSuite(StandardLibraryBooleanTests.class);
		suite.addTestSuite(StandardLibraryDoubleTests.class);
		suite.addTestSuite(StandardLibraryGraphTests.class);
		suite.addTestSuite(StandardLibraryIntegerTests.class);
		suite.addTestSuite(StandardLibraryListTests.class);
		suite.addTestSuite(StandardLibraryMapTests.class);
		suite.addTestSuite(StandardLibraryRelationTests.class);
		suite.addTestSuite(StandardLibrarySetTests.class);
		suite.addTestSuite(StandardLibraryStringTests.class);
		suite.addTestSuite(StandardLibraryTreeTests.class);
		suite.addTestSuite(SubscriptTests.class);
		suite.addTestSuite(StatementTests.class);
		suite.addTestSuite(TryCatchTests.class);
		suite.addTestSuite(VisitTests.class);
		
		return suite;
	  }

	  public static void main(String args[]) {
	    junit.textui.TestRunner.run(suite());
	 
	  }
}
