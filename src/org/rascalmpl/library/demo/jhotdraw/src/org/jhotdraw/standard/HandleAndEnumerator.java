/*
 * @(#)HandleAndEnumerator.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import org.jhotdraw.framework.HandleEnumeration;
import org.jhotdraw.framework.Handle;

import java.util.List;

/**
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class HandleAndEnumerator implements HandleEnumeration {
	private HandleEnumeration myHE1;
	private HandleEnumeration myHE2;

	public HandleAndEnumerator(HandleEnumeration newHE1, HandleEnumeration newHE2) {
		myHE1 = newHE1;
		myHE2 = newHE2;
	}

	public Handle nextHandle() {
		if (myHE1.hasNextHandle()) {
			return myHE1.nextHandle();
		}
		else if (myHE2.hasNextHandle()) {
			return myHE2.nextHandle();
		}
		else {
			// todo: throw exception
			return null;
		}
	}

	public boolean hasNextHandle() {
		return myHE1.hasNextHandle() || myHE2.hasNextHandle();
	}

	public List toList() {
		List joinedList = myHE1.toList();
		joinedList.addAll(myHE2.toList());
		return joinedList;
	}

	public void reset() {
		myHE1.reset();
		myHE2.reset();
	}
}
