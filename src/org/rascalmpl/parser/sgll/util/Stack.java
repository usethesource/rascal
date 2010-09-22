package org.rascalmpl.parser.sgll.util;

public class Stack<E>{
	private final static int DEFAULT_SIZE = 8;
	
	private E[] data;
	private int size;
	
	public Stack(){
		super();
		
		data = (E[]) new Object[DEFAULT_SIZE];
		size = 0;
	}
	
	public Stack(int initialSize){
		super();
		
		data = (E[]) new Object[initialSize];
		size = 0;
	}
	
	public void enlarge(){
		E[] oldData = data;
		data = (E[]) new Object[size << 1];
		System.arraycopy(oldData, 0, data, 0, size);
	}
	
	public void push(E object){
		while(size >= data.length){
			enlarge();
		}
		
		data[size++] = object;
	}
	
	public E peek(){
		return data[size - 1];
	}
	
	public E pop(){
		E object = data[--size];
		data[size] = null;
		return object;
	}
	
	public void purge(){
		data[--size] = null;
	}
	
	public boolean contains(E object){
		for(int i = size - 1; i >= 0; --i){
			if(data[i].equals(object)) return true;
		}
		return false;
	}
	
	public void clear(){
		data = (E[]) new Object[data.length];
		size = 0;
	}
}
