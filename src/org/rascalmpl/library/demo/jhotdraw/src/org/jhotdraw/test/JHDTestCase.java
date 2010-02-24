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

import junit.framework.TestCase;
import org.jhotdraw.application.DrawApplication;
import org.jhotdraw.samples.javadraw.JavaDrawApp;

public class JHDTestCase extends TestCase {
	public JavaDrawApp myDrawingEditor;
	
	public JHDTestCase(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		myDrawingEditor = new JavaDrawApp("TestApplication");
	}
	
	protected void tearDown() throws Exception {
		myDrawingEditor.setVisible(false);
		myDrawingEditor = null;
	}
	
	public DrawApplication getDrawingEditor() {
		return myDrawingEditor;
	}
	
	/**
	 * Some tests might want start from scratch with a new DrawingEditor
	 * (to avoid side-effects from previous test)
	 */
	public JavaDrawApp createNewDrawingEditor() {
		return new JavaDrawApp("TestApplication");
	}
}