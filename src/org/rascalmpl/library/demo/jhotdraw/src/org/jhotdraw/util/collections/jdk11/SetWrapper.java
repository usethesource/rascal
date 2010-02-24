/*
 * @(#)SetWrapper.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.util.collections.jdk11;

import java.util.*;

/**
 * @author  Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class SetWrapper implements Set {
	private Hashtable myDelegee;

	public SetWrapper() {
		myDelegee = new Hashtable();
	}

	public SetWrapper(Set initSet) {
		myDelegee = new Hashtable();
		Iterator iter = initSet.iterator();
		while (iter.hasNext()) {
			add(iter.next());
		}
	}

	public int size() {
		return myDelegee.size();
	}

	public boolean isEmpty() {
		return myDelegee.isEmpty();
	}

	public boolean contains(Object o) {
		return myDelegee.containsKey(o);
	}

	public Iterator iterator() {
		return new IteratorWrapper(myDelegee.elements());
	}

	public Object[] toArray() {
		return new Object[0];
	}

	public Object[] toArray(Object a[]) {
		return new Object[0];
	}

	public boolean add(Object o) {
		return myDelegee.put(o, o) == null;
	}

	public boolean remove(Object o) {
		return myDelegee.remove(o) != null;
	}

	public boolean containsAll(Collection c) {
		return false;
	}

	public boolean addAll(Collection c) {
		return false;
	}

	public boolean retainAll(Collection c) {
		return false;
	}

	public boolean removeAll(Collection c) {
		return false;
	}

	public void clear() {
		myDelegee.clear();
	}
}
