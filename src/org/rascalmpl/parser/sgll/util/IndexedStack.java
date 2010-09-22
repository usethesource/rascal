package org.rascalmpl.parser.sgll.util;

public class IndexedStack<E>{
	private final static int DEFAULT_SIZE = 8;
	
	private E[] data;
	private int[] indexes;
	private int size;
	
	public IndexedStack(){
		super();
		
		data = (E[]) new Object[DEFAULT_SIZE];
		indexes = new int[DEFAULT_SIZE];
		size = 0;
	}
	
	public IndexedStack(int initialSize){
		super();
		
		data = (E[]) new Object[initialSize];
		indexes = new int[initialSize];
		size = 0;
	}
	
	public void enlarge(){
		E[] oldData = data;
		data = (E[]) new Object[size << 1];
		System.arraycopy(oldData, 0, data, 0, size);
		
		int[] oldIndexes = indexes;
		indexes = new int[size << 1];
		System.arraycopy(oldIndexes, 0, indexes, 0, size);
	}
	
	public void push(E object, int index){
		while(size >= data.length){
			enlarge();
		}
		
		data[size] = object;
		indexes[size++] = index;
	}
	
	public E peek(){
		return data[size - 1];
	}
	
	public int peekIndex(){
		return indexes[size - 1];
	}
	
	public E pop(){
		E object = data[--size];
		data[size] = null;
		return object;
	}
	
	public void purge(){
		data[--size] = null;
	}
	
	public E dirtyPop(){
		return data[--size];
	}
	
	public void dirtyPurge(){
		--size;
	}
	
	public int contains(E object){
		for(int i = size - 1; i >= 0; --i){
			if(data[i].equals(object)) return indexes[i];
		}
		return -1;
	}
	
	public int findIndex(E object){
		for(int i = size - 1; i >= 0; --i){
			if(data[i].equals(object)) return indexes[i];
		}
		return -1;
	}
	
	public void clear(){
		data = (E[]) new Object[data.length];
		size = 0;
	}
}
