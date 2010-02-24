/*
 * @(#)Test.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author <a href="mailto:mtnygard@charter.net">Michael T. Nygard</a>
 * @version $Revision: 1.3 $
 */
public class AllTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.jhotdraw.test");
		//$JUnit-BEGIN$
		
		suite.addTest(org.jhotdraw.test.contrib.AllTests.suite());
		suite.addTest(org.jhotdraw.test.figures.AllTests.suite());
		suite.addTest(org.jhotdraw.test.framework.AllTests.suite());
		suite.addTest(org.jhotdraw.test.samples.javadraw.AllTests.suite());
		suite.addTest(org.jhotdraw.test.samples.minimap.AllTests.suite());
		suite.addTest(org.jhotdraw.test.samples.net.AllTests.suite());
		suite.addTest(org.jhotdraw.test.samples.nothing.AllTests.suite());
		suite.addTest(org.jhotdraw.test.samples.pert.AllTests.suite());
		suite.addTest(org.jhotdraw.test.standard.AllTests.suite());
		suite.addTest(org.jhotdraw.test.util.AllTests.suite());
		suite.addTest(org.jhotdraw.test.util.collections.jdk11.AllTests.suite());
		suite.addTest(org.jhotdraw.test.util.collections.jdk12.AllTests.suite());
		

		//$JUnit-END$
		return suite;
	}
}
