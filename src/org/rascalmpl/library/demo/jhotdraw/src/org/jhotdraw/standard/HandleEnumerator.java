/*
 * @(#)HandleEnumerator.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import org.jhotdraw.util.CollectionsFactory;
import org.jhotdraw.framework.HandleEnumeration;
import org.jhotdraw.framework.Handle;

import java.util.Iterator;
import java.util.Collection;
import java.util.List;

/**
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class HandleEnumerator implements HandleEnumeration {
	private Iterator myIterator;
	private Collection myInitialCollection;

	private static HandleEnumerator singletonEmptyEnumerator =
		new HandleEnumerator(CollectionsFactory.current().createList());

	public HandleEnumerator(Collection c) {
		myInitialCollection = c;
		reset();
	}

	/**
	 * Returns true if the enumeration contains more elements; false
	 * if its empty.
	 */
	public boolean hasNextHandle() {
		return myIterator.hasNext();
	}

	/**
	 * Returns the next element of the enumeration. Calls to this
	 * method will enumerate successive elements.
	 * @exception java.util.NoSuchElementException If no more elements exist.
	 */
	public Handle nextHandle() {
		return (Handle)myIterator.next();
	}

	/**
	 * Returns a list with all elements currently available in the enumeration.
	 * That means, elements retrieved already by calling nextHandle() are not
	 * contained. This method does not change the position of the enumeration.
	 * Warning: this method is not necessarily synchronized so this enumeration should not
	 * be modified at the same time!
	 *
	 * @return list with all elements currently available in the enumeration.
	 */
	public List toList() {
		List handles = CollectionsFactory.current().createList();
		while (hasNextHandle()) {
			handles.add(nextHandle());
		}
		// copy/reset iterator to original content
		myIterator = handles.iterator();
		return handles;
	}

	/**
	 * Reset the enumeration so it can be reused again. However, the
	 * underlying collection might have changed since the last usage
	 * so the elements and the order may vary when using an enumeration
	 * which has been reset.
	 */
	public void reset() {
		myIterator = myInitialCollection.iterator();
	}

	public static HandleEnumeration getEmptyEnumeration() {
		return singletonEmptyEnumerator;
	}
}
