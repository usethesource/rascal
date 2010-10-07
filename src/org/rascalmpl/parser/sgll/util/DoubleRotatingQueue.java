package org.rascalmpl.parser.sgll.util;

public class DoubleRotatingQueue<E, F>{
	private final static int DEFAULT_CAPACITY = 8;
	
	private E[] queue1;
	private F[] queue2;
	private int capacity;
	private int capacityMask;
	private int nextPutIndex;
	private int getIndex;
	
	public DoubleRotatingQueue(){
		super();
		
		capacity = DEFAULT_CAPACITY;
		capacityMask = capacity - 1;
		
		queue1 = (E[]) new Object[capacity];
		queue2 = (F[]) new Object[capacity];
		
		nextPutIndex = 1;
		getIndex = 0;
	}
	
	public DoubleRotatingQueue(int capacity){
		super();
		
		this.capacity = capacity;
		capacityMask = capacity - 1;
		
		queue1 = (E[]) new Object[capacity];
		queue2 = (F[]) new Object[capacity];
		
		nextPutIndex = 1;
		getIndex = 0;
	}
	
	private void ensureCapacity(){
		int size = capacity;
		capacityMask = (capacity <<= 1) - 1;
		E[] newQueue1 = (E[]) new Object[capacity];
		F[] newQueue2 = (F[]) new Object[capacity];
		if(getIndex == 0){
			System.arraycopy(queue1, 0, newQueue1, 0, queue1.length);
			System.arraycopy(queue2, 0, newQueue2, 0, queue2.length);
		}else{
			int numElemsTillEnd = size - getIndex;
			System.arraycopy(queue1, getIndex, newQueue1, 0, numElemsTillEnd);
			System.arraycopy(queue1, 0, newQueue1, numElemsTillEnd, getIndex);
			System.arraycopy(queue2, getIndex, newQueue2, 0, numElemsTillEnd);
			System.arraycopy(queue2, 0, newQueue2, numElemsTillEnd, getIndex);
			
			getIndex = 0;
		}
		nextPutIndex = size;
		
		queue1 = newQueue1;
		queue2 = newQueue2;
	}
	
	public void put(E element1, F element2){
		if(nextPutIndex == getIndex){
			ensureCapacity();
		}
		
		queue1[nextPutIndex] = element1;
		queue2[nextPutIndex] = element2;
		
		nextPutIndex = (nextPutIndex + 1) & capacityMask;
	}
	
	public boolean isEmpty(){
		return (nextPutIndex == ((getIndex + 1) & capacityMask));
	}
	
	public E peekFirst(){
		if(isEmpty()) return null;
		
		return queue1[(getIndex + 1) & capacityMask];
	}
	
	public F peekSecond(){
		if(isEmpty()) return null;
		
		return queue2[(getIndex + 1) & capacityMask];
	}
	
	public E peekFirstUnsafe(){
		return queue1[(getIndex + 1) & capacityMask];
	}
	
	public F peekSecondUnsafe(){
		return queue2[(getIndex + 1) & capacityMask];
	}
	
	public E getFirst(){
		if(isEmpty()) return null;
		
		getIndex = (getIndex + 1) & capacityMask;
		E element = queue1[getIndex];
		queue1[getIndex] = null;
		
		return element;
	}
	
	public F getSecond(){
		if(isEmpty()) return null;
		
		getIndex = (getIndex + 1) & capacityMask;
		F element = queue2[getIndex];
		queue2[getIndex] = null;
		
		return element;
	}
	
	public E getFirstUnsafe(){
		getIndex = (getIndex + 1) & capacityMask;
		E element = queue1[getIndex];
		queue1[getIndex] = null;
		
		return element;
	}
	
	public F getSecondUnsafe(){
		getIndex = (getIndex + 1) & capacityMask;
		F element = queue2[getIndex];
		queue2[getIndex] = null;
		
		return element;
	}
	
	public E getFirstDirtyUnsafe(){
		getIndex = (getIndex + 1) & capacityMask;
		return queue1[getIndex];
	}
	
	public F getSecondDirtyUnsafe(){
		getIndex = (getIndex + 1) & capacityMask;
		return queue2[getIndex];
	}
	
	public void clear(){
		queue1 = (E[]) new Object[capacity];
		queue2 = (F[]) new Object[capacity];
		
		nextPutIndex = 1;
		getIndex = 0;
	}
}
