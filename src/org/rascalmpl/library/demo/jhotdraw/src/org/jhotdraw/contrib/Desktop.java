/*
 * @(#)Desktop.java
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

/**
 * @author  C.L.Gilbert <dnoyeb@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public interface Desktop {
	public final static int PRIMARY = 0;
	public final static int SECONDARY = 1;
	public final static int TERTIARY = 2;

	/**
	 * For those absent minded components that were not paying attention to the
	 * listener events.
	 */
	public DrawingView getActiveDrawingView();
	public void addToDesktop(DrawingView dv, int location);
	public void removeFromDesktop(DrawingView dv, int location);
	public void removeAllFromDesktop(int location);
	public DrawingView[] getAllFromDesktop(int location);

	public void updateTitle(String newDrawingTitle);

	public void addDesktopListener(DesktopListener dpl);
	public void removeDesktopListener(DesktopListener dpl);
}