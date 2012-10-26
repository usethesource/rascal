/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.util;

public class IntegerList{
	private final static int DEFAULT_SIZE = 8;
	
	private int[] data;
	private int size;
	
	public IntegerList(){
		super();
		
		data = new int[DEFAULT_SIZE];
		size = 0;
	}
	
	public IntegerList(int initialSize){
		super();
		
		data = new int[initialSize];
		size = 0;
	}
	
	public void enlarge(){
		int[] oldData = data;
		data = new int[size << 1];
		System.arraycopy(oldData, 0, data, 0, size);
	}
	
	public void add(int integer){
		while(size >= data.length){
			enlarge();
		}
		
		data[size++] = integer;
	}
	
	public int get(int index){
		return data[index];
	}
	
	public boolean contains(int integer){
		for(int i = size - 1; i >= 0; --i){
			if(data[i] == integer) return true;
		}
		return false;
	}
	
	public boolean containsBefore(int integer, int limit){
		for(int i = limit - 1; i >= 0; --i){
			if(data[i] == integer) return true;
		}
		return false;
	}
	
	public int size(){
		return size;
	}
	
	public void clear(){
		size = 0;
	}
	
	public int[] getBackingArray(){
		return data;
	}
}
