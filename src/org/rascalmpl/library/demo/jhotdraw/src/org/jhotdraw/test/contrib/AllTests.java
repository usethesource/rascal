/*
 * @(#)AllTests.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
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
package org.jhotdraw.test.contrib;

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
		TestSuite suite = new TestSuite("Test for org.jhotdraw.test.contrib");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(CTXCommandMenuTest.class));
		suite.addTest(new TestSuite(CTXWindowMenuTest.class));
		suite.addTest(new TestSuite(ChopDiamondConnectorTest.class));
		suite.addTest(new TestSuite(ChopPolygonConnectorTest.class));
		suite.addTest(new TestSuite(ClippingUpdateStrategyTest.class));
		suite.addTest(new TestSuite(CommandCheckBoxMenuItemTest.class));
		suite.addTest(new TestSuite(CommandMenuItemTest.class));
		suite.addTest(new TestSuite(ComponentFigureTest.class));
		suite.addTest(new TestSuite(CompositeFigureCreationToolTest.class));
		suite.addTest(new TestSuite(CustomSelectionToolTest.class));
		suite.addTest(new TestSuite(CustomToolBarTest.class));
		suite.addTest(new TestSuite(DesktopEventServiceTest.class));
		suite.addTest(new TestSuite(DiamondFigureTest.class));
		suite.addTest(new TestSuite(FloatingTextAreaTest.class));
		suite.addTest(new TestSuite(GraphicalCompositeFigureTest.class));
		suite.addTest(new TestSuite(HelperTest.class));
		suite.addTest(new TestSuite(JPanelDesktopTest.class));
		suite.addTest(new TestSuite(JScrollPaneDesktopTest.class));
		suite.addTest(new TestSuite(MDIDesktopPaneTest.class));
		suite.addTest(new TestSuite(MDI_DrawApplicationTest.class));
		suite.addTest(new TestSuite(MiniMapViewTest.class));
		suite.addTest(new TestSuite(NestedCreationToolTest.class));
		suite.addTest(new TestSuite(PolygonFigureTest.class));
		suite.addTest(new TestSuite(PolygonHandleTest.class));
		suite.addTest(new TestSuite(PolygonToolTest.class));
		suite.addTest(new TestSuite(SVGDrawAppTest.class));
		suite.addTest(new TestSuite(SVGStorageFormatTest.class));
		suite.addTest(new TestSuite(SimpleLayouterTest.class));
		suite.addTest(new TestSuite(SplitConnectionToolTest.class));
		suite.addTest(new TestSuite(StandardLayouterTest.class));
		suite.addTest(new TestSuite(TextAreaFigureTest.class));
		suite.addTest(new TestSuite(TextAreaToolTest.class));
		suite.addTest(new TestSuite(TriangleFigureTest.class));
		suite.addTest(new TestSuite(WindowMenuTest.class));
		//$JUnit-END$
		return suite;
	}
}
