/*
 * @(#)IteratorWrapper.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util.collections.jdk11;

import java.util.Iterator;
import java.util.Enumeration;

/**
 * @author  Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class IteratorWrapper implements Iterator {
	private Enumeration myEnumeration;
	public IteratorWrapper(Enumeration enumeration) {
		myEnumeration = enumeration;
	}

	public boolean hasNext() {
		return myEnumeration.hasMoreElements();
	}

	public Object next() {
		return myEnumeration.nextElement();
	}

	public void remove() {
		// do nothing or throw exception
		//throw new UnsupportedOperationException();
	}
}
