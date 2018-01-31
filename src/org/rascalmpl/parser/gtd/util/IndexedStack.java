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

@SuppressWarnings("unchecked")
public class IndexedStack<E>{
	private final static int DEFAULT_SIZE = 8;
	
	private E[] data;
	private int[] indexes;
	private int size;
	
	public IndexedStack(){
		super();
		
		data = (E[]) new Object[DEFAULT_SIZE];
		indexes = new int[DEFAULT_SIZE];
		size = 0;
	}
	
	public IndexedStack(int initialSize){
		super();
		
		data = (E[]) new Object[initialSize];
		indexes = new int[initialSize];
		size = 0;
	}
	
	public void enlarge(){
		E[] oldData = data;
		data = (E[]) new Object[size << 1];
		System.arraycopy(oldData, 0, data, 0, size);
		
		int[] oldIndexes = indexes;
		indexes = new int[size << 1];
		System.arraycopy(oldIndexes, 0, indexes, 0, size);
	}
	
	public void push(E object, int index){
		while(size >= data.length){
			enlarge();
		}
		
		data[size] = object;
		indexes[size++] = index;
	}
	
	public E peek(){
		return data[size - 1];
	}
	
	public int peekIndex(){
		return indexes[size - 1];
	}
	
	public E pop(){
		E object = data[--size];
		data[size] = null;
		return object;
	}
	
	public void purge(){
		data[--size] = null;
	}
	
	public E dirtyPop(){
		return data[--size];
	}
	
	public void dirtyPurge(){
		--size;
	}
	
	public E get(int index){
		return data[index];
	}
	
	public int contains(E object){
		for(int i = size - 1; i >= 0; --i){
			if(data[i].equals(object)) return indexes[i];
		}
		return -1;
	}
	
	public int findIndex(E object){
		for(int i = size - 1; i >= 0; --i){
			if(data[i].equals(object)) return indexes[i];
		}
		return -1;
	}
	
	public int getSize(){
		return size;
	}
	
	public boolean isEmpty(){
		return (size == 0);
	}
	
	public void clear(){
		data = (E[]) new Object[data.length];
		size = 0;
	}
}
