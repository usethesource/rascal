package org.rascalmpl.parser.sgll.util;

public class DoubleArrayList<F, S>{
	private final static int DEFAULT_SIZE = 8;
	
	private F[] first;
	private S[] second;
	private int size;
	
	public DoubleArrayList(){
		super();
		
		first = (F[]) new Object[DEFAULT_SIZE];
		second = (S[]) new Object[DEFAULT_SIZE];
		size = 0;
	}
	
	public DoubleArrayList(int initialSize){
		super();
		
		first = (F[]) new Object[initialSize];
		second = (S[]) new Object[initialSize];
		size = 0;
	}
	
	public void enlarge(){
		F[] oldFirst = first;
		first = (F[]) new Object[size << 1];
		System.arraycopy(oldFirst, 0, first, 0, size);
		

		S[] oldSecond = second;
		second = (S[]) new Object[size << 1];
		System.arraycopy(oldSecond, 0, second, 0, size);
	}
	
	public void add(F firstObject, S secondObject){
		if(size == first.length){
			enlarge();
		}
		
		first[size] = firstObject;
		second[size++] = secondObject;
	}
	
	public F getFirst(int index){
		return first[index];
	}
	
	public S getSecond(int index){
		return second[index];
	}
	
	public boolean containsFirst(F object){
		for(int i = size - 1; i >= 0; i--){
			if(first[i].equals(object)) return true;
		}
		return false;
	}
	
	public boolean containsSecond(S object){
		for(int i = size - 1; i >= 0; i--){
			if(second[i].equals(object)) return true;
		}
		return false;
	}
	
	public void remove(int index){
		if(index != --size){
			System.arraycopy(first, index + 1, first, index, size - index);
			System.arraycopy(second, index + 1, second, index, size - index);
		}
		first[size] = null;
		second[size] = null;
	}
	
	public void clear(){
		first = (F[]) new Object[DEFAULT_SIZE];
		second = (S[]) new Object[DEFAULT_SIZE];
		size = 0;
	}
	
	public void clear(int initialSize){
		first = (F[]) new Object[initialSize];
		second = (S[]) new Object[initialSize];
		size = 0;
	}
	
	public int size(){
		return size;
	}
}
