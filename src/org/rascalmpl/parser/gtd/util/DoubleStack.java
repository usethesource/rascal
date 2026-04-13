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
public class DoubleStack<E, F>{
	private final static int DEFAULT_SIZE = 8;
	
	private E[] firstData;
	private F[] secondData;
	private int size;
	
	public DoubleStack(){
		super();
		
		firstData = (E[]) new Object[DEFAULT_SIZE];
		secondData = (F[]) new Object[DEFAULT_SIZE];
		size = 0;
	}
	
	public DoubleStack(int initialSize){
		super();
		
		firstData = (E[]) new Object[initialSize];
		secondData = (F[]) new Object[initialSize];
		size = 0;
	}
	
	public void enlarge(){
		E[] oldFirstData = firstData;
		firstData = (E[]) new Object[size << 1];
		System.arraycopy(oldFirstData, 0, firstData, 0, size);
		
		F[] oldSecondData = secondData;
		secondData = (F[]) new Object[size << 1];
		System.arraycopy(oldSecondData, 0, secondData, 0, size);
	}
	
	public void push(E first, F second){
		while(size >= firstData.length){
			enlarge();
		}
		
		firstData[size] = first;
		secondData[size++] = second;
	}
	
	public E peekFirst(){
		return firstData[size - 1];
	}
	
	public F peekSecond(){
		return secondData[size - 1];
	}
	
	public E popFirst(){
		E first = firstData[--size];
		firstData[size] = null;
		secondData[size] = null;
		return first;
	}
	
	public F popSecond(){
		F second = secondData[--size];
		firstData[size] = null;
		secondData[size] = null;
		return second;
	}
	
	public void purge(){
		firstData[--size] = null;
	}
	
	public E dirtyPop(){
		return firstData[--size];
	}
	
	public void dirtyPurge(){
		--size;
	}
	
	public E getFirst(int index){
		return firstData[index];
	}
	
	public F getSecond(int index){
		return secondData[index];
	}
	
	public int findFirst(E object){
		for(int i = size - 1; i >= 0; --i){
			if(firstData[i].equals(object)) return i;
		}
		return -1;
	}
	
	public int findSecond(F object){
		for(int i = size - 1; i >= 0; --i){
			if(secondData[i].equals(object)) return i;
		}
		return -1;
	}
	
	public boolean containsFirst(E object){
		for(int i = size - 1; i >= 0; --i){
			if(firstData[i].equals(object)) return true;
		}
		return false;
	}
	
	public boolean containsSecond(F object){
		for(int i = size - 1; i >= 0; --i){
			if(secondData[i].equals(object)) return true;
		}
		return false;
	}
	
	public E findFirstWithFirst(E object){
		for(int i = size - 1; i >= 0; --i){
			E objectToCompareWith = firstData[i];
			if(objectToCompareWith.equals(object)) return objectToCompareWith;
		}
		return null;
	}
	
	public F findSecondWithFirst(E object){
		for(int i = size - 1; i >= 0; --i){
			if(firstData[i].equals(object)) return secondData[i];
		}
		return null;
	}
	
	public E findFirstWithSecond(F object){
		for(int i = size - 1; i >= 0; --i){
			if(secondData[i].equals(object)) return firstData[i];
		}
		return null;
	}
	
	public F findSecondWithSecond(F object){
		for(int i = size - 1; i >= 0; --i){
			F objectToCompareWith = secondData[i];
			if(objectToCompareWith.equals(object)) return objectToCompareWith;
		}
		return null;
	}
	
	public int getSize(){
		return size;
	}
	
	public boolean isEmpty(){
		return (size == 0);
	}
	
	public void clear(){
		firstData = (E[]) new Object[firstData.length];
		secondData = (F[]) new Object[secondData.length];
		size = 0;
	}
	
	public void dirtyClear(){
		size = 0;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("stack[");
		for (int i=0; i<size; i++) {
			builder.append("\n    ");
			builder.append("<");
			builder.append(firstData[0]);
			builder.append(",");
			builder.append(secondData[1]);
			builder.append(">");
		}
		builder.append("]\n");
		return builder.toString();
	}
}
