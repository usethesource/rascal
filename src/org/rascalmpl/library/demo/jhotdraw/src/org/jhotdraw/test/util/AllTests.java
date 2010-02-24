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
package org.jhotdraw.test.util;

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
		TestSuite suite = new TestSuite("Test for org.jhotdraw.test.util");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(BoundsTest.class));
		suite.addTest(new TestSuite(ClipboardTest.class));
		suite.addTest(new TestSuite(ColorMapTest.class));
		suite.addTest(new TestSuite(CommandButtonTest.class));
		suite.addTest(new TestSuite(CommandChoiceTest.class));
		suite.addTest(new TestSuite(CommandMenuTest.class));
		suite.addTest(new TestSuite(FillerTest.class));
		suite.addTest(new TestSuite(FloatingTextFieldTest.class));
		suite.addTest(new TestSuite(GeomTest.class));
		suite.addTest(new TestSuite(GraphLayoutTest.class));
		suite.addTest(new TestSuite(IconkitTest.class));
		suite.addTest(new TestSuite(PaletteIconTest.class));
		suite.addTest(new TestSuite(PaletteLayoutTest.class));
		suite.addTest(new TestSuite(RedoCommandTest.class));
		suite.addTest(new TestSuite(ReverseListEnumeratorTest.class));
		suite.addTest(new TestSuite(SerializationStorageFormatTest.class));
		suite.addTest(new TestSuite(StandardStorageFormatTest.class));
		suite.addTest(new TestSuite(StandardVersionControlStrategyTest.class));
		suite.addTest(new TestSuite(StorableInputTest.class));
		suite.addTest(new TestSuite(StorableOutputTest.class));
		suite.addTest(new TestSuite(StorageFormatManagerTest.class));
		suite.addTest(new TestSuite(UndoCommandTest.class));
		suite.addTest(new TestSuite(UndoManagerTest.class));
		suite.addTest(new TestSuite(UndoRedoActivityTest.class));
		suite.addTest(new TestSuite(UndoableAdapterTest.class));
		suite.addTest(new TestSuite(UndoableCommandTest.class));
		suite.addTest(new TestSuite(UndoableHandleTest.class));
		suite.addTest(new TestSuite(UndoableToolTest.class));
		suite.addTest(new TestSuite(VersionManagementTest.class));
		//$JUnit-END$
		return suite;
	}
}
