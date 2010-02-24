/*
 * @(#)StandardVersionControlStrategy.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import org.jhotdraw.framework.*;

/**
 * @author Wolfram Kaiser
 * @version <$CURRENT_VERSION$>
 */
public class StandardVersionControlStrategy implements VersionControlStrategy {
	private VersionRequester myVersionRequester;
	
	public StandardVersionControlStrategy(VersionRequester newVersionRequester) {
		setVersionRequester(newVersionRequester);
	}
	
	/**
	 * Define a strategy how to select those versions of JHotDraw
	 * with which they are compatible.
	 */
	public void assertCompatibleVersion() {
		String[] requiredVersions = getVersionRequester().getRequiredVersions();
		// version is compatible if no version was specified
		if (requiredVersions.length == 0) {
			return;
		}
		for (int i = 0; i < requiredVersions.length; i++) {
			if (isCompatibleVersion(requiredVersions[i])) {
				// a compatible version has been found
				return;
			}
		}
		// no matching version was found
		handleIncompatibleVersions();
	}

	/**
	 * This method is called in open() if an incompatible version has been
	 * encountered. Applications can override this method to provide customized
	 * exception handling for this case. In the default implementation, a
	 * JHotDrawRuntimeException is thrown.
	 */
	protected void handleIncompatibleVersions() {
		// collect version info
		String[] requiredVersions = getVersionRequester().getRequiredVersions();
		StringBuffer expectedVersions = new StringBuffer("[");
		for (int i = 0; i < requiredVersions.length - 1; i++) {
			expectedVersions.append(requiredVersions[i] + ", ");
		}
		if (requiredVersions.length > 0) {
			expectedVersions.append(requiredVersions[requiredVersions.length - 1]);
		}
		expectedVersions.append("]");

		// no compatible version has been found
		throw new JHotDrawRuntimeException("Incompatible version of JHotDraw found: "
			+  VersionManagement.getJHotDrawVersion() 
			+ " (expected: " + expectedVersions + ")");
	}

	/**
	 * Subclasses can override this method to specify an algorithms that determines
	 * how version strings are compared and which version strings can be regarded
	 * as compatible. For example, a subclass may choose that all versions 5.x of
	 * JHotDraw are compatible with the application, so only the first digit in
	 * the version number is considered significant. In the default implementation,
	 * all versions that are equal or greater than the expected version are compatible.
	 *
	 * @param compareVersionString application version to compare with JHotDraw's version
	 */
	protected boolean isCompatibleVersion(String compareVersionString) {
		return VersionManagement.isCompatibleVersion(compareVersionString);
	}
	
	private void setVersionRequester(VersionRequester newVersionRequester) {
		myVersionRequester = newVersionRequester;
	}
	
	protected VersionRequester getVersionRequester() {
		return myVersionRequester;
	}
}
