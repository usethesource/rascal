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
package org.rascalmpl.value.impl.util.sharing;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * This cache stores a predefined range of values.<br /><br />
 * Note that this class provides lock-free thread-safe access.
 * 
 * @author Arnold Lankamp
 *
 * @param <T> The content type.
 */
public class IndexedCache<T>{
	private final AtomicReferenceArray<T> atomicCacheArray;
	private final int size;
	
	/**
	 * Constructor.
	 * 
	 * @param size
	 *            The range of values to cache (0 till 'size').
	 */
	public IndexedCache(int size){
		super();
		
		atomicCacheArray = new AtomicReferenceArray<>(size);
		this.size = size;
	}
	
	/**
	 * Returns the range of values that will be cached (0 till size).
	 * 
	 * @return The range of values that will be cached (0 till size).
	 */
	public int size(){
		return size;
	}
	
	/**
	 * Returns the unique representation of the given value that is located at the indicated index.
	 * 
	 * @param index
	 *            The index at which the value is located / must be inserted.
	 * @param prototype
	 *            The value to use in case it have not yet been cached.
	 * @return The unique representation of the given value that is located at the indicated index.
	 */
	public T getOrDefine(int index, T prototype){
		if(index >= size) throw new IndexOutOfBoundsException("Index may not be larger then size.");
		
		if(atomicCacheArray.compareAndSet(index, null, prototype)){
			return prototype; // We just put the prototype in the cache.
		}
		return atomicCacheArray.get(index); // Someone beat us to it, use that one.
	}
	
	/**
	 * Returns the value that is currently cached at the indicated position.
	 * 
	 * @param index
	 *            The index at which the requested value resides. Must be within the range of this
	 *            cache.
	 * @return The requested value; may be null.
	 */
	public T get(int index){
		return atomicCacheArray.get(index);
	}
}
