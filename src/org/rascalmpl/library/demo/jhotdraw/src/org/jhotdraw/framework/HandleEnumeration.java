/*
 * @(#)HandleEnumeration.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.framework;

import java.util.List;

/**
 * Interface for Enumerations that access Handles.
 * It provides a method nextHandle, that hides the down casting
 * from client code.
 *
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public interface HandleEnumeration {
	/**
	 * Returns the next element of the enumeration. Calls to this
	 * method will enumerate successive elements.
	 * @exception java.util.NoSuchElementException If no more elements exist.
	 */
	public Handle nextHandle();
	public boolean hasNextHandle();

	/**
	 * Returns a list with all elements currently available in the enumeration.
	 * That means, elements retrieved already by calling nextHandle() are not
	 * contained. This method does not change the position of the enumeration.
	 * Warning: this method is not necessarily synchronized so this enumeration should not
	 * be modified at the same time!
	 *
	 * @return list with all elements currently available in the enumeration.
	 */
	public List toList();

	/**
	 * Reset the enumeration so it can be reused again. However, the
	 * underlying collection might have changed since the last usage
	 * so the elements and the order may vary when using an enumeration
	 * which has been reset.
	 */
	public void reset();
}
