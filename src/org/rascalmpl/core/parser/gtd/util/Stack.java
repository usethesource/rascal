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
package org.rascalmpl.core.parser.gtd.util;

@SuppressWarnings("unchecked")
public class Stack<E>{
	private final static int DEFAULT_SIZE = 8;
	
	private E[] data;
	private int size;
	
	public Stack(){
		super();
		
		data = (E[]) new Object[DEFAULT_SIZE];
		size = 0;
	}
	
	public Stack(int initialSize){
		super();
		
		data = (E[]) new Object[initialSize];
		size = 0;
	}
	
	public void enlarge(){
		E[] oldData = data;
		data = (E[]) new Object[size << 1];
		System.arraycopy(oldData, 0, data, 0, size);
	}
	
	public void push(E object){
		while(size >= data.length){
			enlarge();
		}
		
		data[size++] = object;
	}
	
	public E peek(){
		return data[size - 1];
	}
	
	public E pop(){
		E object = data[--size];
		data[size] = null;
		return object;
	}
	
	public void purge(){
		data[--size] = null;
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
	
	public void dirtyClear(){
		size = 0;
	}
}
