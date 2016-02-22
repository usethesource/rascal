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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This list implementation is shareable and guarantees that the following operations can be done
 * in constant time:
 * -insert (front)
 * -append (back)
 * -get (ordered)
 * -get (random)
 * -set (random)
 * -remove (front)
 * -remove (back)
 * -reverse
 * Additionally both cloning this list as the sublist operation are relatively cheap,
 * (two simple arraycopies).
 * 
 * @author Arnold Lankamp
 *
 * @param <E>
 *            The element type.
 */
public class ShareableList<E> implements Iterable<E>{
	private final static int INITIAL_LOG_SIZE = 2;

	private int frontCapacity;
	private E[] frontData;
	private int frontIndex;

	private int backCapacity;
	private E[] backData;
	private int backIndex;
	
	/**
	 * Default constructor
	 */
	public ShareableList(){
		super();
		
		frontCapacity = 1 << INITIAL_LOG_SIZE;
		frontData = (E[]) new Object[frontCapacity];
		frontIndex = 0;
		
		backCapacity = 1 << INITIAL_LOG_SIZE;
		backData = (E[]) new Object[backCapacity];
		backIndex = 0;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param shareableList
	 *            The list to copy.
	 */
	public ShareableList(ShareableList<E> shareableList){
		super();
		
		frontCapacity = shareableList.frontCapacity;
		frontData = shareableList.frontData.clone();
		frontIndex = shareableList.frontIndex;
		
		backCapacity = shareableList.backCapacity;
		backData = shareableList.backData.clone();
		backIndex = shareableList.backIndex;
	}
	
	/**
	 * SubList copy constructor.
	 * 
	 * @param shareableList
	 *            The list to copy.
	 * @param offset
	 *            The start index of the sublist.
	 * @param length
	 *            The length of the sublist.
	 */
	protected ShareableList(ShareableList<E> shareableList, int offset, int length){
		super();
		
		int backStartIndex = shareableList.backIndex - offset;
		if(backStartIndex <= 0){// Front only
			backIndex = 0;
			backCapacity = 2;
			backData = (E[]) new Object[backCapacity];
			
			int frontStartIndex = -backStartIndex;

			frontIndex = length;
			frontCapacity = closestPowerOfTwo(length);
			frontData = (E[]) new Object[frontCapacity];
			System.arraycopy(shareableList.frontData, frontStartIndex, frontData, 0, length);
		}else{
			if((offset + length) <= shareableList.backIndex){ // Back only
				backIndex = length;
				backCapacity = closestPowerOfTwo(length);
				backData = (E[]) new Object[backCapacity];
				System.arraycopy(shareableList.backData, backStartIndex - length, backData, 0, length);

				frontIndex = 0;
				frontCapacity = 2;
				frontData = (E[]) new Object[frontCapacity];
			}else{ // Front and Back overlap
				backIndex = backStartIndex;
				backCapacity = closestPowerOfTwo(backStartIndex);
				backData = (E[]) new Object[backCapacity];
				System.arraycopy(shareableList.backData, 0, backData, 0, backStartIndex);
				
				int frontLength = length - backStartIndex;

				frontIndex = frontLength;
				frontCapacity = closestPowerOfTwo(frontLength);
				frontData = (E[]) new Object[frontCapacity];
				System.arraycopy(shareableList.frontData, 0, frontData, 0, frontLength);
			}
		}
	}
	
	private static int closestPowerOfTwo(int v){
	    // https://graphics.stanford.edu/~seander/bithacks.html#RoundUpPowerOf2
	    v--;
	    v |= v >> 1;
	    v |= v >> 2;
	    v |= v >> 4;
	    v |= v >> 8;
	    v |= v >> 16;
	    return v + 1;
	}
	
	/**
	 * Removes all the elements from this list.
	 */
	public void clear(){
		frontCapacity = 1 << INITIAL_LOG_SIZE;
		frontData = (E[]) new Object[frontCapacity];
		frontIndex = 0;
		
		backCapacity = 1 << INITIAL_LOG_SIZE;
		backData = (E[]) new Object[backCapacity];
		backIndex = 0;
	}
	
	/**
	 * Calling this will guarantee that there is enough space for the next single element insert.
	 */
	private void ensureFrontCapacity(){
		if(frontCapacity == frontIndex){
			frontCapacity <<= 1;
			E[] newFrontData = (E[]) new Object[frontCapacity];
			System.arraycopy(frontData, 0, newFrontData, 0, frontData.length);
			frontData = newFrontData;
		}
	}
	
	/**
	 * Calling this will guarantee that there is enough space for the next single element append.
	 */
	private void ensureBackCapacity(){
		if(backCapacity == backIndex){
			backCapacity <<= 1;
			E[] newBackData = (E[]) new Object[backCapacity];
			System.arraycopy(backData, 0, newBackData, 0, backData.length);
			backData = newBackData;
		}
	}
	
	/**
	 * Calling this will guarantee that there is enough space for the next batch of inserted elements.
	 * 
	 * @param nrOfElements
	 *            The amount of free space that is required.
	 */
	private void ensureFrontBulkCapacity(int nrOfElements){
		int requiredCapacity = frontIndex + nrOfElements;
		if(frontCapacity <= requiredCapacity){
			do{
				frontCapacity <<= 1;
			}while(frontCapacity <= requiredCapacity);
			
			E[] newFrontData = (E[]) new Object[frontCapacity];
			System.arraycopy(frontData, 0, newFrontData, 0, frontData.length);
			frontData = newFrontData;
		}
	}
	
	/**
	 * Calling this will guarantee that there is enough space for the next batch of appended elements.
	 * 
	 * @param nrOfElements
	 *            The amount of free space that is required.
	 */
	private void ensureBackBulkCapacity(int nrOfElements){
		int requiredCapacity = backIndex + nrOfElements;
		
		if(backCapacity <= requiredCapacity){
			do{
				backCapacity <<= 1;
			}while(backCapacity <= requiredCapacity);
			
			E[] newBackData = (E[]) new Object[backCapacity];
			System.arraycopy(backData, 0, newBackData, 0, backData.length);
			backData = newBackData;
		}
	}
	
	/**
	 * Appends the given element to the end of this list.
	 * 
	 * @param element
	 *            The element to append.
	 */
	public void append(E element){
		ensureFrontCapacity();
		
		frontData[frontIndex++] = element;
	}
	
	/**
	 * Appends the given batch of element to the end of this list.
	 * 
	 * @param elements
	 *            The batch of elements to append.
	 */
	public void appendAll(E[] elements){
		int nrOfElements = elements.length;
		
		ensureFrontBulkCapacity(nrOfElements);
		
		System.arraycopy(elements, 0, frontData, frontIndex, nrOfElements);
		frontIndex += nrOfElements;
	}
	
	/**
	 * Appends the indicated range of elements from the given batch of elements to the end of this
	 * list.
	 * 
	 * @param elements
	 *            The batch that contains the elements to append.
	 * @param offset
	 *            The position in the given batch to start at.
	 * @param length
	 *            The number of elements from the given batch to append.
	 */
	public void appendAllAt(E[] elements, int offset, int length){
		if(offset < 0) throw new IllegalArgumentException("Offset must be > 0.");
		if((offset + length) > elements.length) throw new IllegalArgumentException("(offset + length) must be <= elements.length.");
		
		ensureFrontBulkCapacity(length);
		
		System.arraycopy(elements, offset, frontData, frontIndex, length);
		frontIndex += length;
	}
	
	/**
	 * Inserts the given element to the start of this list.
	 * 
	 * @param element
	 *            The element to insert.
	 */
	public void insert(E element){
		ensureBackCapacity();
		
		backData[backIndex++] = element;
	}
	
	/**
	 * Inserts the given batch of elements to the start of this list.
	 * 
	 * @param elements
	 *            The batch of elements to insert.
	 */
	public void insertAll(E[] elements){
		int nrOfElements = elements.length;
		
		ensureBackBulkCapacity(nrOfElements);
		
		for(int i = nrOfElements - 1; i >= 0; i--){
			backData[backIndex++] = elements[i];
		}
	}
	
	/**
	 * Inserts the given element at the indicated position in this list.
	 * 
	 * @param index
	 *            The index to insert the element at.
	 * @param element
	 *            The element to insert.
	 */
	public void insertAt(int index, E element){
		int realIndex = index - backIndex;
		if(realIndex >= 0){
			ensureFrontCapacity();
			
			if(realIndex > frontIndex) throw new ArrayIndexOutOfBoundsException(index+" > the the current size of the list ("+size()+")");

			int elementsToMove = frontIndex - realIndex;
			System.arraycopy(frontData, realIndex, frontData, realIndex + 1, elementsToMove);
			frontData[realIndex] = element;
			frontIndex++;
		}else{
			ensureBackCapacity();
			
			realIndex = 0 - realIndex;
			
			if(realIndex > backIndex) throw new ArrayIndexOutOfBoundsException(index+" < 0");
			
			int elementsToMove = backIndex - realIndex;
			System.arraycopy(backData, realIndex, backData, realIndex + 1, elementsToMove);
			backData[realIndex] = element;
			backIndex++;
		}
	}
	
	/**
	 * Replaces the element at the indicated position in this list by the given element.
	 * 
	 * @param index
	 *            The index to replace the element at.
	 * @param element
	 *            The element to place at the indicated position.
	 * @return The element that was located at the indicated position prior to the execution of this
	 * operation.
	 */
	public E set(int index, E element){
		int realIndex = index - backIndex;
		if(realIndex >= 0){
			if(realIndex >= frontIndex) throw new ArrayIndexOutOfBoundsException(index+" >= the current size of the list ("+size()+")");
			
			E oldElement = frontData[realIndex];
			frontData[realIndex] = element;
			
			return oldElement;
		}
		
		realIndex = -1 - realIndex;
		
		if(realIndex >= backIndex) throw new ArrayIndexOutOfBoundsException(index+" < 0");
		
		E oldElement = backData[realIndex];
		backData[realIndex] = element;
		
		return oldElement;
	}
	
	/**
	 * Retrieves the element at the indicated position from this list.
	 * 
	 * @param index
	 *            The position to retrieve the element from.
	 * @return The retrieved element.
	 */
	public E get(int index){
		int realIndex = index - backIndex;
		if(realIndex >= 0){
			if(realIndex >= frontIndex) {
				throw new ArrayIndexOutOfBoundsException(index+" >= the current size of the list ("+size()+")");
			}
			
			return frontData[realIndex];
		}
		
		realIndex = -1 - realIndex;
		
		if(realIndex >= backIndex) throw new ArrayIndexOutOfBoundsException(index+" < 0");
		
		return backData[realIndex];
	}
	
	/**
	 * Removes the element at the indicated position from this list.
	 * 
	 * @param index
	 *            The position to remove the element from.
	 * @return The element that was located at the indicated position prior to the execution of
	 * this operation.
	 */
	public E remove(int index){
		int realIndex = index - backIndex;
		if(realIndex >= 0){
			if(realIndex >= frontIndex) throw new ArrayIndexOutOfBoundsException(index+" >= the current size of the list ("+size()+")");
			
			E oldElement = frontData[realIndex];
			
			int elementsToMove = --frontIndex - realIndex;
			if(elementsToMove > 0) System.arraycopy(frontData, realIndex + 1, frontData, realIndex, elementsToMove);
			frontData[frontIndex] = null; // Remove the 'old' reference at the end of the array.
			
			return oldElement;
		}
		
		realIndex = -1 - realIndex;
		
		if(realIndex >= backIndex) throw new ArrayIndexOutOfBoundsException(index+" < 0");
		
		E oldElement = backData[realIndex];
		
		int elementsToMove = --backIndex - realIndex;
		if(elementsToMove > 0) System.arraycopy(backData, realIndex + 1, backData, realIndex, elementsToMove);
		backData[backIndex] = null; // Remove the 'old' reference at the end of the array.
		
		return oldElement;
	}
	
	/**
	 * Constructs a sublist of this list.
	 * 
	 * @param offset
	 *            The offset to start at.
	 * @param length
	 *            The number of elements.
	 * @return The constructed sublist.
	 */
	public ShareableList<E> subList(int offset, int length){
		if(offset < 0) throw new IndexOutOfBoundsException("Offset may not be smaller then 0.");
		if(length < 0) throw new IndexOutOfBoundsException("Length may not be smaller then 0.");
		if((offset + length) > size()) throw new IndexOutOfBoundsException("'offset + length' may not be larger then 'list.size()'");
		
		return new ShareableList<>(this, offset, length);
	}
	
	/**
	 * Reverses the order of the elements in this list.
	 * 
	 * @return A reference to this list.
	 */
	public ShareableList<E> reverse(){
		int tempCapacity = frontCapacity;
		E[] tempData = frontData;
		int tempIndex = frontIndex;
		
		frontCapacity = backCapacity;
		frontData = backData;
		frontIndex = backIndex;
		
		backCapacity = tempCapacity;
		backData = tempData;
		backIndex = tempIndex;
		
		return this;
	}
	
	/**
	 * Returns the number of elements that are currently present in this list.
	 * 
	 * @return The number of elements that are currently present in this list.
	 */
	public int size(){
		return (frontIndex + backIndex);
	}
	
	/**
	 * Checks whether or not this list is empty.
	 * 
	 * @return True if this list is empty; false otherwise.
	 */
	public boolean isEmpty(){
		return (size() == 0);
	}
	
	/**
	 * Constructs an iterator for this list.
	 * 
	 * @return An iterator for this list.
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<E> iterator(){
		return new ListIterator<>(this);
	}
	
	/**
	 * Computes the current hash code of this list.
	 * 
	 * @return The current hash code of this list.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode(){
		int hash = 0;
		
		Iterator<E> iterator = iterator();
		while(iterator.hasNext()){
			E element = iterator.next();
			hash = (hash << 1) ^ element.hashCode();
		}
		
		return hash;
	}
	
	/**
	 * Check whether or not the current content of this list is equal to that of the given object / list. 
	 * 
	 * @return True if the content of this list is equal to the given object / list.
	 * 
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object o){
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			ShareableList<?> other = (ShareableList<?>) o;
			
			if(other.size() == size()){
				if(isEmpty()) return true; // No need to check if the lists are empty.
				
				Iterator<E> thisIterator = iterator();
				Iterator<?> otherIterator = other.iterator();
				while(thisIterator.hasNext()){
					if(!thisIterator.next().equals(otherIterator.next())) return false;
				}
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Prints the internal representation of this list to a string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuilder buffer = new StringBuilder();
		
		buffer.append('[');
		
		Iterator<E> iterator = iterator();
		if(iterator.hasNext()){
			E element = iterator.next();
			buffer.append(element);
			
			while(iterator.hasNext()){
				element = iterator.next();
				
				buffer.append(',');
				buffer.append(element);
			}
		}
		
		buffer.append(']');
		
		return buffer.toString();
	}
	
	/**
	 * Iterator for this list.
	 * 
	 * @author Arnold Lankamp
	 *
	 * @param <E>
	 *            The element type.
	 */
	private static class ListIterator<E> implements Iterator<E>{
		private final ShareableList<E> shareableList;
		
		private int currentIndex;
		private boolean front;
		
		/**
		 * Constructor.
		 * 
		 * @param shareableList
		 *            The list to iterator over.
		 */
		public ListIterator(ShareableList<E> shareableList){
			super();
			
			this.shareableList = shareableList;
			
			currentIndex = shareableList.backIndex - 1;
			front = false;
			if(currentIndex < 0){
				currentIndex = 0;
				front = true;
			}
		}
		
		/**
		 * Check whether or not there are more elements in this iteration.
		 * 
		 * @return True if there are more element in this iteration.
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext(){
			return front ? (currentIndex < shareableList.frontIndex) : (currentIndex >= 0);
		}
		
		/**
		 * Returns the next element in this iteration.
		 * 
		 * @return The next element in this iteration.
		 * @throws java.util.NoSuchElementException
		 *            Thrown when there are no more elements in this iteration when calling this
		 *            method.
		 * 
		 * @see java.util.Iterator#next()
		 */
		public E next(){
			if(!hasNext()) throw new NoSuchElementException("There are no more elements in this iteration.");
			
			E element;
			
			if(front){
				element = shareableList.frontData[currentIndex++];
			}else{
				element = shareableList.backData[currentIndex--];
				if(currentIndex == -1){
					front = true;
					currentIndex = 0;
				}
			}
			
			return element;
		}
		
		/**
		 * This iterator does not support removal.
		 * 
		 * @throws java.lang.UnsupportedOperationException
		 * 
		 * @see java.util.Iterator#remove()
		 */
		public void remove(){
			throw new UnsupportedOperationException("This iterator doesn't support removal.");
		}
	}
}
