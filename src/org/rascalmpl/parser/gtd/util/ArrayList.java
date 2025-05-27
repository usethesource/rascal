/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.util;

public class ArrayList<E> {
	private final static int DEFAULT_SIZE = 8;
	
	private E[] data;
	private int size;
	
	@SuppressWarnings("unchecked")
	public ArrayList(){
		super();
		
		data = (E[]) new Object[DEFAULT_SIZE];
		size = 0;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList(int initialSize){
		super();
		
		data = (E[]) new Object[initialSize];
		size = 0;
	}
	
	@SuppressWarnings("unchecked")
	public void enlarge(){
		E[] oldData = data;
		data = (E[]) new Object[size << 1];
		System.arraycopy(oldData, 0, data, 0, size);
	}
	
	public void add(E object){
		while(size >= data.length){
			enlarge();
		}
		
		data[size++] = object;
	}
	
	public E get(int index){
		return data[index];
	}
	
	public boolean contains(E object){
		for(int i = size - 1; i >= 0; --i){
			if(data[i].equals(object)) return true;
		}
		return false;
	}
	
	public int findIndex(E object){
		for(int i = size - 1; i >= 0; --i){
			if(data[i].equals(object)) return i;
		}
		return -1;
	}
	
	public E remove(int index){
		E object = data[index];
		if(index != --size){
			System.arraycopy(data, index + 1, data, index, size - index);
		}
		data[size] = null;
		
		return object;
	}
	
	public E remove(E object){
		for(int i = size - 1; i >= 0; --i){
			if(data[i].equals(object)) return remove(i);
		}
		return null;
	}
	
	public void resetTo(int index){
		size = index;
	}
	
	@SuppressWarnings("unchecked")
	public void clear(){
		data = (E[]) new Object[data.length];
		size = 0;
	}
	
	public void dirtyClear(){
		size = 0;
	}
	
	public int size(){
		return size;
	}
	
	public Object[] getBackingArray(){
		return data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		for (int i=0; i<size; i++) {
			if (i > 0) {
				builder.append(",");
			}
			builder.append(data[i]);
		}
		builder.append("]");
		return builder.toString();
	}
}
