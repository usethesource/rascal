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
 * A simple (fast) queue.
 * 
 * @author Arnold Lankamp
 *
 * @param <T> The value type.
 */
public final class RotatingQueue<T>{
	private final static int DEFAULT_CAPACITY = 16;
	private final static int DEFAULT_CAPACITY_MASK = DEFAULT_CAPACITY - 1;
	
    private T[] queue;
    private int capacity;
    private int capacityMask;
    private int nextPutIndex;
    private int getIndex;
    
    /**
     * Constructor.
     */
    public RotatingQueue(){
            super();

            capacity = DEFAULT_CAPACITY;
            capacityMask = DEFAULT_CAPACITY_MASK;

            queue = (T[]) new Object[capacity];

            nextPutIndex = 1;
            getIndex = 0;
    }
    
    private void ensureCapacity(){
            if(nextPutIndex == getIndex){
                    int size = capacity;
                    capacity <<= 1;
                    capacityMask = capacity - 1;
                    T[] newQueue = (T[]) new Object[capacity];
                    if(getIndex == 0){
                            System.arraycopy(queue, 0, newQueue, 0, queue.length);

                            nextPutIndex = size;
                    }else{
                            int numElemsTillEnd = size - getIndex;
                            System.arraycopy(queue, getIndex, newQueue, 0, numElemsTillEnd);
                            System.arraycopy(queue, 0, newQueue, numElemsTillEnd, getIndex);

                            getIndex = 0;
                            nextPutIndex = size;
                    }

                    queue = newQueue;
            }
    }
    
    /**
     * Enqueues the given element.
     * 
     * @param element
     *            The element to enqueue.
     */
    public void put(T element){
            ensureCapacity();

            queue[nextPutIndex] = element;

            nextPutIndex = (nextPutIndex + 1) & capacityMask;
    }
    
    /**
     * Check if the queue contains any elements.
     * 
     * @return True if the queue contains any elements; false otherwise.
     */
    public boolean isEmpty(){
            return (nextPutIndex == ((getIndex + 1) & capacityMask));
    }
    
    /**
     * Returns and removes the next element from the queue.
     * 
     * @return The next element from the queue; null if the queue was empty.
     */
    public T get(){
            if(isEmpty()) return null;

            getIndex = (getIndex + 1) & capacityMask;
            T element = queue[getIndex];
            queue[getIndex] = null;

            return element;
    }
}