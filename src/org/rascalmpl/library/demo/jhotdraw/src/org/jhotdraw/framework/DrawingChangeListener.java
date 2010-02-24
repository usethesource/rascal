/*
 * @(#)DrawingChangeListener.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.framework;

/**
 * Listener interested in Drawing changes.
 *
 * @version <$CURRENT_VERSION$>
 */
public interface DrawingChangeListener {

	/**
	 *  Sent when an area is invalid
	 */
	public void drawingInvalidated(DrawingChangeEvent e);

    /**
     *  Sent when the drawing Title has changed
     */
    public void drawingTitleChanged(DrawingChangeEvent e);
	/**
	 *  Sent when the drawing wants to be refreshed
	 */
	public void drawingRequestUpdate(DrawingChangeEvent e);
}
