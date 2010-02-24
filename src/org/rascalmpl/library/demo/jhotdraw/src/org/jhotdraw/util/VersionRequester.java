/*
 * @(#)VersionRequester.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

/**
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public interface VersionRequester {
	/**
	 * Subclasses should override this method to specify to which versions of
	 * JHotDraw they are compatible. A string array is returned so it is possible
	 * to specify several version numbers of JHotDraw to which the application
	 * is compatible with.
	 *
	 * @return all versions number of JHotDraw the application is compatible with.
	 */
	public abstract String[] getRequiredVersions();
}