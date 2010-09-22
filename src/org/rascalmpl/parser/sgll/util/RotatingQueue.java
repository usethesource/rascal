package org.rascalmpl.parser.sgll.util;

public class RotatingQueue<E>{
	private final static int DEFAULT_CAPACITY = 8;
	
	private E[] queue;
	private int capacity;
	private int capacityMask;
	private int nextPutIndex;
	private int getIndex;
	
	public RotatingQueue(){
		super();
		
		capacity = DEFAULT_CAPACITY;
		capacityMask = capacity - 1;
		
		queue = (E[]) new Object[capacity];
		
		nextPutIndex = 1;
		getIndex = 0;
	}
	
	public RotatingQueue(int capacity){
		super();
		
		this.capacity = capacity;
		capacityMask = capacity - 1;
		
		queue = (E[]) new Object[capacity];
		
		nextPutIndex = 1;
		getIndex = 0;
	}
	
	private void ensureCapacity(){
		int size = capacity;
		capacityMask = (capacity <<= 1) - 1;
		E[] newQueue = (E[]) new Object[capacity];
		if(getIndex == 0){
			System.arraycopy(queue, 0, newQueue, 0, queue.length);
		}else{
			int numElemsTillEnd = size - getIndex;
			System.arraycopy(queue, getIndex, newQueue, 0, numElemsTillEnd);
			System.arraycopy(queue, 0, newQueue, numElemsTillEnd, getIndex);
			
			getIndex = 0;
		}
		nextPutIndex = size;
		
		queue = newQueue;
	}
	
	public void put(E element){
		if(nextPutIndex == getIndex){
			ensureCapacity();
		}
		
		queue[nextPutIndex] = element;
		
		nextPutIndex = (nextPutIndex + 1) & capacityMask;
	}
	
	public boolean isEmpty(){
		return (nextPutIndex == ((getIndex + 1) & capacityMask));
	}
	
	public E get(){
		if(isEmpty()) return null;
		
		getIndex = (getIndex + 1) & capacityMask;
		E element = queue[getIndex];
		queue[getIndex] = null;
		
		return element;
	}
	
	public E unsafeGet(){
		getIndex = (getIndex + 1) & capacityMask;
		E element = queue[getIndex];
		queue[getIndex] = null;
		
		return element;
	}
	
	public void clear(){
		queue = (E[]) new Object[capacity];
		
		nextPutIndex = 1;
		getIndex = 0;
	}
	
	public void dirtyClear(){
		nextPutIndex = 1;
		getIndex = 0;
	}
}
