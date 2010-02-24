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
package org.jhotdraw.test.figures;

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
		TestSuite suite = new TestSuite("Test for org.jhotdraw.test.figures");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(ArrowTipTest.class));
		suite.addTest(new TestSuite(BorderDecoratorTest.class));
		suite.addTest(new TestSuite(BorderToolTest.class));
		suite.addTest(new TestSuite(ChopEllipseConnectorTest.class));
		suite.addTest(new TestSuite(ConnectedTextToolTest.class));
		suite.addTest(new TestSuite(ElbowConnectionTest.class));
		suite.addTest(new TestSuite(ElbowHandleTest.class));
		suite.addTest(new TestSuite(EllipseFigureTest.class));
		suite.addTest(new TestSuite(FigureAttributesTest.class));
		suite.addTest(new TestSuite(FontSizeHandleTest.class));
		suite.addTest(new TestSuite(GroupCommandTest.class));
		suite.addTest(new TestSuite(GroupFigureTest.class));
		suite.addTest(new TestSuite(ImageFigureTest.class));
		suite.addTest(new TestSuite(InsertImageCommandTest.class));
		suite.addTest(new TestSuite(LineConnectionTest.class));
		suite.addTest(new TestSuite(LineFigureTest.class));
		suite.addTest(new TestSuite(NullConnectorTest.class));
		suite.addTest(new TestSuite(NullFigureTest.class));
		suite.addTest(new TestSuite(NumberTextFigureTest.class));
		suite.addTest(new TestSuite(PolyLineConnectorTest.class));
		suite.addTest(new TestSuite(PolyLineFigureTest.class));
		suite.addTest(new TestSuite(PolyLineHandleTest.class));
		suite.addTest(new TestSuite(RectangleFigureTest.class));
		suite.addTest(new TestSuite(RoundRectangleFigureTest.class));
		suite.addTest(new TestSuite(ScribbleToolTest.class));
		suite.addTest(new TestSuite(ShortestDistanceConnectorTest.class));
		suite.addTest(new TestSuite(TextFigureTest.class));
		suite.addTest(new TestSuite(TextToolTest.class));
		suite.addTest(new TestSuite(UngroupCommandTest.class));
		//$JUnit-END$
		return suite;
	}
}
