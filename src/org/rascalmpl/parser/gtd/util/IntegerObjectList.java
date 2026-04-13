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
public class IntegerObjectList<V>{
	private final static int DEFAULT_SIZE = 8;
	
	private int[] keys;
	private V[] values;
	
	private int size;
	
	public IntegerObjectList(){
		super();
		
		keys = new int[DEFAULT_SIZE];
		values = (V[]) new Object[DEFAULT_SIZE];
	}
	
	public IntegerObjectList(int size){
		super();
		
		keys = new int[size];
		values = (V[]) new Object[size];
	}
	
	public IntegerObjectList(IntegerObjectList<V> original){
		super();
		
		int[] oldKeys = original.keys;
		V[] oldValues = original.values;
		int length = oldKeys.length;
		
		size = original.size;
		keys = new int[length];
		System.arraycopy(oldKeys, 0, keys, 0, size);
		values = (V[]) new Object[length];
		System.arraycopy(oldValues, 0, values, 0, size);
	}
	
	public void enlarge(){
		int[] oldKeys = keys;
		keys = new int[size << 1];
		System.arraycopy(oldKeys, 0, keys, 0, size);

		V[] oldValues = values;
		values = (V[]) new Object[size << 1];
		System.arraycopy(oldValues, 0, values, 0, size);
	}
	
	public void add(int key, V value){
		while(size >= keys.length){
			enlarge();
		}
		
		keys[size] = key;
		values[size++] = value;
	}
	
	public int getKey(int index){
		return keys[index];
	}
	
	public V getValue(int index){
		return values[index];
	}
	
	public int findKey(int key){
		for(int i = size - 1; i >= 0; --i){
			if(keys[i] == key){
				return i;
			}
		}
		return -1;
	}
	
	public int findKeyBefore(int key, int index){
		for(int i = index - 1; i >= 0; --i){
			if(keys[i] == key){
				return i;
			}
		}
		return -1;
	}
	
	public V findValue(int key){
		for(int i = size - 1; i >= 0; --i){
			if(keys[i] == key){
				return values[i];
			}
		}
		return null;
	}
	
	public V findValueBefore(int key, int index){
		for(int i = index - 1; i >= 0; --i){
			if(keys[i] == key){
				return values[i];
			}
		}
		return null;
	}
	
	public int size(){
		return size;
	}
	
	public void clear(){
		int length = keys.length;
		keys = new int[length];
		values = (V[]) new Object[length];
		size = 0;
	}
	
	public void dirtyClear(){
		size = 0;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder("[");

		for (int i=0; i<size; i++) {
			if (i > 0) {
				builder.append(",");
			}
			builder.append(keys[i]);
			builder.append("=");
			builder.append(values[i]);
		}

		builder.append("]");
		return builder.toString();
	}
}
