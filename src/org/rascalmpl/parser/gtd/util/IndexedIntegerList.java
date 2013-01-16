/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.util;

public class IndexedIntegerList{
	private final static int DEFAULT_SIZE = 8;
	
	private int[] elements;
	
	private int size;
	
	public IndexedIntegerList(){
		super();
		
		elements = new int[DEFAULT_SIZE];
	}
	
	public IndexedIntegerList(IndexedIntegerList original){
		super();
		
		int[] oldElements = original.elements;
		int length = oldElements.length;
		
		size = original.size;
		elements = new int[length];
		System.arraycopy(oldElements, 0, elements, 0, size);
	}
	
	public void enlarge(){
		int[] oldElements = elements;
		elements = new int[size << 1];
		System.arraycopy(oldElements, 0, elements, 0, size);
	}
	
	public void add(int element){
		while(size >= elements.length){
			enlarge();
		}
		
		elements[size++] = element;
	}
	
	public int getElement(int index){
		return elements[index];
	}
	
	public boolean contains(int element){
		for(int i = size - 1; i >= 0; --i){
			if(elements[i] == element){
				return true;
			}
		}
		return false;
	}
	
	public int find(int element){
		for(int i = size - 1; i >= 0; --i){
			if(elements[i] == element){
				return i;
			}
		}
		return -1;
	}
	
	public int size(){
		return size;
	}
	
	public int capacity(){
		return elements.length;
	}
	
	public void clear(){
		int length = elements.length;
		elements = new int[length];
		size = 0;
	}
	
	public void dirtyClear(){
		size = 0;
	}
}
