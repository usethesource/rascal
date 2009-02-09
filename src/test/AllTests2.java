package test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests2 {

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTestSuite(IOTests.class);
		suite.addTestSuite(ParsingTests.class);
		suite.addTestSuite(PatternTests.class);
		suite.addTestSuite(RegExpTests.class);
		suite.addTestSuite(RuleTests.class);

		return suite;
	}

	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}
}
