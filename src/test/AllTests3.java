package test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests3 {
	
	public static Test suite() {
		TestSuite suite = new TestSuite();
		
		suite.addTestSuite(StandardLibraryBooleanTests.class);
		suite.addTestSuite(StandardLibraryRealTests.class);
		suite.addTestSuite(StandardLibraryGraphTests.class);
		suite.addTestSuite(StandardLibraryIntegerTests.class);
		suite.addTestSuite(StandardLibraryListTests.class);
		
		return suite;
	}

	  public  void main(String args[]) {
	    junit.textui.TestRunner.run(suite());
	  }
}

