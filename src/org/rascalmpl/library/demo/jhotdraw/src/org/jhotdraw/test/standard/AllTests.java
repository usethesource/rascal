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
package org.jhotdraw.test.standard;

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
		TestSuite suite = new TestSuite("Test for org.jhotdraw.test.standard");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(AlignCommandTest.class));
		suite.addTest(new TestSuite(BoxHandleKitTest.class));
		suite.addTest(new TestSuite(BringToFrontCommandTest.class));
		suite.addTest(new TestSuite(BufferedUpdateStrategyTest.class));
		suite.addTest(new TestSuite(ChangeAttributeCommandTest.class));
		suite.addTest(new TestSuite(ChangeConnectionEndHandleTest.class));
		suite.addTest(new TestSuite(ChangeConnectionStartHandleTest.class));
		suite.addTest(new TestSuite(ChopBoxConnectorTest.class));
		suite.addTest(new TestSuite(ConnectionHandleTest.class));
		suite.addTest(new TestSuite(ConnectionToolTest.class));
		suite.addTest(new TestSuite(CopyCommandTest.class));
		suite.addTest(new TestSuite(CreationToolTest.class));
		suite.addTest(new TestSuite(CutCommandTest.class));
		suite.addTest(new TestSuite(DeleteCommandTest.class));
		suite.addTest(new TestSuite(DeleteFromDrawingVisitorTest.class));
		suite.addTest(new TestSuite(DragTrackerTest.class));
		suite.addTest(new TestSuite(DuplicateCommandTest.class));
		suite.addTest(new TestSuite(FastBufferedUpdateStrategyTest.class));
		suite.addTest(new TestSuite(FigureAndEnumeratorTest.class));
		suite.addTest(new TestSuite(FigureChangeAdapterTest.class));
		suite.addTest(new TestSuite(FigureChangeEventMulticasterTest.class));
		suite.addTest(new TestSuite(FigureEnumeratorTest.class));
		suite.addTest(new TestSuite(GridConstrainerTest.class));
		suite.addTest(new TestSuite(HandleAndEnumeratorTest.class));
		suite.addTest(new TestSuite(HandleEnumeratorTest.class));
		suite.addTest(new TestSuite(HandleTrackerTest.class));
		suite.addTest(new TestSuite(InsertIntoDrawingVisitorTest.class));
		suite.addTest(new TestSuite(LocatorConnectorTest.class));
		suite.addTest(new TestSuite(LocatorHandleTest.class));
		suite.addTest(new TestSuite(NullDrawingViewTest.class));
		suite.addTest(new TestSuite(NullHandleTest.class));
		suite.addTest(new TestSuite(NullToolTest.class));
		suite.addTest(new TestSuite(OffsetLocatorTest.class));
		suite.addTest(new TestSuite(PasteCommandTest.class));
		suite.addTest(new TestSuite(PeripheralLocatorTest.class));
		suite.addTest(new TestSuite(RelativeLocatorTest.class));
		suite.addTest(new TestSuite(ReverseFigureEnumeratorTest.class));
		suite.addTest(new TestSuite(SelectAllCommandTest.class));
		suite.addTest(new TestSuite(SelectAreaTrackerTest.class));
		suite.addTest(new TestSuite(SelectionToolTest.class));
		suite.addTest(new TestSuite(SendToBackCommandTest.class));
		suite.addTest(new TestSuite(SimpleUpdateStrategyTest.class));
		suite.addTest(new TestSuite(SingleFigureEnumeratorTest.class));
		suite.addTest(new TestSuite(StandardDrawingTest.class));
		suite.addTest(new TestSuite(StandardDrawingViewTest.class));
		suite.addTest(new TestSuite(StandardFigureSelectionTest.class));
		suite.addTest(new TestSuite(ToggleGridCommandTest.class));
		suite.addTest(new TestSuite(ToolButtonTest.class));
		//$JUnit-END$
		return suite;
	}
}
