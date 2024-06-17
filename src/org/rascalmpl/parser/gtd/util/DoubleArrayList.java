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
public class DoubleArrayList<F, S>{
	private final static int DEFAULT_SIZE = 8;
	
	private F[] first;
	private S[] second;
	private int size;
	
	public DoubleArrayList(){
		super();

		first = (F[]) new Object[DEFAULT_SIZE];
		second = (S[]) new Object[DEFAULT_SIZE];
		size = 0;
	}
	
	public DoubleArrayList(int initialSize){
		super();
		
		first = (F[]) new Object[initialSize];
		second = (S[]) new Object[initialSize];
		size = 0;
	}
	
	public void enlarge(){
		F[] oldFirst = first;
		first = (F[]) new Object[size << 1];
		System.arraycopy(oldFirst, 0, first, 0, size);
		
		S[] oldSecond = second;
		second = (S[]) new Object[size << 1];
		System.arraycopy(oldSecond, 0, second, 0, size);
	}
	
	public void add(F firstObject, S secondObject){
		while(size >= first.length){
			enlarge();
		}
		
		first[size] = firstObject;
		second[size++] = secondObject;
	}
	
	public F getFirst(int i){
		return first[i];
	}
	
	public S getSecond(int i){
		return second[i];
	}
	
	public boolean containsFirst(F firstObject){
		for(int i = size - 1; i >= 0; --i){
			if(first[i] == firstObject) return true;
		}
		return false;
	}
	
	public boolean containsSecond(S secondObject){
		for(int i = size - 1; i >= 0; --i){
			if(first[i] == secondObject) return true;
		}
		return false;
	}
	
	public void remove(int index){
		if(index != --size){
			System.arraycopy(first, index + 1, first, index, size - index);
			System.arraycopy(second, index + 1, second, index, size - index);
		}
		first[size] = null;
		second[size] = null;
	}
	
	public void resetTo(int index){
		size = index;
	}
	
	public void clear(){
		first = (F[]) new Object[DEFAULT_SIZE];
		second = (S[]) new Object[DEFAULT_SIZE];
		size = 0;
	}
	
	public void ditryClear(){
		size = 0;
	}
	
	public int size(){
		return size;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("list[");
		for (int i=0; i<size; i++) {
			builder.append("\n    ");
			builder.append("<");
			builder.append(first[0]);
			builder.append(",");
			builder.append(second[1]);
			builder.append(">");
		}
		builder.append("]\n");
		return builder.toString();
	}

}
