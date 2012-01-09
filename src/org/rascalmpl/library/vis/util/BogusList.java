package org.rascalmpl.library.vis.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/* A Bogus list, a class that implements the list interface but throws all added elements into /dev/null */

public class BogusList<T> implements List<T>{

	@SuppressWarnings("rawtypes")
	public static final BogusList instance = new BogusList();
	
	@Override
	public boolean add(T arg0) {
		return true;
	}

	@Override
	public void add(int arg0, T arg1) {
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		return true;
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		return true;
	}

	@Override
	public void clear() {
		
	}

	@Override
	public boolean contains(Object arg0) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return false;
	}

	@Override
	public T get(int arg0) {
		return null;
	}

	@Override
	public int indexOf(Object arg0) {
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public Iterator<T> iterator() {
		return null;
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return -1;
	}

	@Override
	public ListIterator<T> listIterator() {
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int arg0) {
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		return true;
	}

	@Override
	public T remove(int arg0) {
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return false;
	}

	@Override
	public T set(int arg0, T arg1) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		return null;
	}

	@Override
	public Object[] toArray() {
		return null;
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] arg0) {
		return null;
	}

}
