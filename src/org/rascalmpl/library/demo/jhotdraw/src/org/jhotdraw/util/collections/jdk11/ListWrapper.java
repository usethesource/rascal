/*
 * @(#)ListWrapper.java
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
public class ListWrapper implements List {
	private Vector myDelegee;

	public ListWrapper() {
		myDelegee = new Vector();
	}

	public ListWrapper(int initialSize) {
		myDelegee = new Vector(initialSize);
	}

	public ListWrapper(Collection copyCollection) {
		myDelegee = new Vector(copyCollection);
	}

	public int size() {
		return myDelegee.size();
	}

	public boolean isEmpty() {
		return myDelegee.isEmpty();
	}

	public boolean contains(Object o) {
		return myDelegee.contains(o);
	}

	public Iterator iterator() {
		return new IteratorWrapper(myDelegee.elements());
	}

	public Object[] toArray() {
		return myDelegee.toArray();
	}

	public Object[] toArray(Object a[]) {
		return myDelegee.toArray(a);
	}

	public boolean add(Object o) {
		return myDelegee.add(o);
	}

	public boolean remove(Object o) {
		return myDelegee.removeElement(o);
	}

	public boolean containsAll(Collection c) {
		return myDelegee.containsAll(c);
	}

	public boolean addAll(Collection c) {
		return myDelegee.addAll(c);
	}

	public boolean addAll(int index, Collection c) {
		return myDelegee.addAll(index, c);
	}

	public boolean removeAll(Collection c) {
		return myDelegee.removeAll(c);
	}

	public boolean retainAll(Collection c) {
		return myDelegee.retainAll(c);
	}

	public void clear() {
		myDelegee.clear();
	}

	public Object get(int index) {
		return myDelegee.elementAt(index);
	}

	public Object set(int index, Object element) {
		return myDelegee.set(index, element) ;
	}

	public void add(int index, Object element) {
		myDelegee.add(index, element);
	}

	public Object remove(int index) {
		return myDelegee.remove(index);
	}

	public int indexOf(Object o) {
		return myDelegee.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return myDelegee.lastIndexOf(o);
	}

	public ListIterator listIterator() {
		return myDelegee.listIterator();
	}

	public ListIterator listIterator(int index) {
		return myDelegee.listIterator(index);
	}

	public List subList(int fromIndex, int toIndex) {
		return null;
	}
}
