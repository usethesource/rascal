package test;

import junit.framework.Test;
import junit.framework.TestSuite;
public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(DataTypeTests.class);
		suite.addTestSuite(RegExpTests.class);
		suite.addTestSuite(ComprehensionTests.class);
		return suite;
	  }

	  public static void main(String args[]) {
	    junit.textui.TestRunner.run(suite());
	 
	  }
}
