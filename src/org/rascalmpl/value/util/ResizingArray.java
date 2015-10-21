/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*******************************************************************************/
package org.rascalmpl.value.util;

/**
 * This array will always have enough space.
 * 
 * @author Arnold Lankamp
 *
 * @param <E> The value type.
 */
public class ResizingArray<E>{
	private final static int DEFAULT_INITIAL_SIZE = 32;
	
	private E[] content;
	
	/**
	 * Constructor.
	 */
	public ResizingArray(){
		super();
		
		content = (E[]) new Object[DEFAULT_INITIAL_SIZE];
	}
	
	/**
	 * Constructor.
	 * 
	 * @param initialSize
	 *            The initial size of the backing array.
	 */
	public ResizingArray(int initialSize){
		super();
		
		content = (E[]) new Object[initialSize];
	}
	
	private void ensureCapacity(int max){
		if(max >= content.length){
			E[] newContent = (E[]) new Object[max << 2];
			System.arraycopy(content, 0, newContent, 0, content.length);
			content = newContent;
		}
	}
	
	/**
	 * Set the give element at 'index'.
	 * 
	 * @param element
	 *            The element to set.
	 * @param index
	 *            The index at which to set.
	 */
	public void set(E element, int index){
		ensureCapacity(index);
		
		content[index] = element;
	}
	
	/**
	 * Returns the element located at 'index'.
	 * 
	 * @param index
	 *            The index to retrieve the element from.
	 * @return The element.
	 */
	public E get(int index){
		return content[index];
	}
}
