package test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests4 {

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTestSuite(StandardLibraryMapTests.class);
		suite.addTestSuite(StandardLibraryNodeTests.class);
		suite.addTestSuite(StandardLibraryRelationTests.class);
		suite.addTestSuite(StandardLibrarySetTests.class);
		suite.addTestSuite(StandardLibraryStringTests.class);
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
