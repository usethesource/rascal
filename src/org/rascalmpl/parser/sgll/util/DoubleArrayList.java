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
		while(size >= first.length){
			enlarge();
		}
		
		first[size] = firstObject;
		second[size++] = secondObject;
	}
	
	public F getFirst(int i){
		return first[i];
	}
	
	public S getSecond(int i){
		return second[i];
	}
	
	public boolean containsFirst(F firstObject){
		for(int i = size - 1; i >= 0; --i){
			if(first[i] == firstObject) return true;
		}
		return false;
	}
	
	public boolean containsSecond(S secondObject){
		for(int i = size - 1; i >= 0; --i){
			if(first[i] == secondObject) return true;
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
	
	public void resetTo(int index){
		size = index;
	}
	
	public void clear(){
		first = (F[]) new Object[DEFAULT_SIZE];
		second = (S[]) new Object[DEFAULT_SIZE];
		size = 0;
	}
	
	public void ditryClear(){
		size = 0;
	}
	
	public int size(){
		return size;
	}
}
