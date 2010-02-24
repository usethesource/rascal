/*
 * @(#)ToolListener.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.framework;

import java.util.EventObject;

/**
 * @author Wolfram Kaiser
 * @version <$CURRENT_VERSION$>
 */
public interface ToolListener {
	public void toolEnabled(EventObject toolEvent);
	public void toolDisabled(EventObject toolEvent);
	public void toolUsable(EventObject toolEvent);
	public void toolUnusable(EventObject toolEvent);
	public void toolActivated(EventObject toolEvent);
	public void toolDeactivated(EventObject toolEvent);
}
