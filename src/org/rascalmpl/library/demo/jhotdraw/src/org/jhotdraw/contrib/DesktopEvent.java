/*
 * @(#)DesktopEvent.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import org.jhotdraw.framework.DrawingView;
import java.util.EventObject;

/**
 * @author  C.L.Gilbert <dnoyeb@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class DesktopEvent extends EventObject {
	private DrawingView myDrawingView;

	/**
	 * Some events require the previous DrawingView (e.g. when a new DrawingView
	 * is selected).
	 */	
	private DrawingView myPreviousDrawingView;

	public DesktopEvent(Desktop newSource, DrawingView newDrawingView) {
		this(newSource, newDrawingView, null);
	}

	public DesktopEvent(Desktop newSource, DrawingView newDrawingView, DrawingView newPreviousDV) {
		super(newSource);
		setDrawingView(newDrawingView);
		setPreviousDrawingView(newPreviousDV);
	}

	private void setDrawingView(DrawingView newDrawingView) {
		myDrawingView = newDrawingView;
	}
	
	public DrawingView getDrawingView() {
	    return myDrawingView;
	}

	private void setPreviousDrawingView(DrawingView newPreviousDrawingView) {
		myPreviousDrawingView = newPreviousDrawingView;
	}
	
	public DrawingView getPreviousDrawingView() {
		return myPreviousDrawingView;
	}
}