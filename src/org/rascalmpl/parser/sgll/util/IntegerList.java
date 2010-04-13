package org.rascalmpl.parser.sgll.util;

public class IntegerList{
	private final static int DEFAULT_SIZE = 8;
	
	private int[] data;
	private int size;
	
	public IntegerList(){
		super();
		
		data = new int[DEFAULT_SIZE];
		size = 0;
	}
	
	public IntegerList(int initialSize){
		super();
		
		data = new int[initialSize];
		size = 0;
	}
	
	public void enlarge(){
		int[] oldData = data;
		data = new int[size << 1];
		System.arraycopy(oldData, 0, data, 0, size);
	}
	
	public void add(int integer){
		int index = size;
		if(size == data.length){
			enlarge();
		}
		size++;
		
		data[index] = integer;
	}
	
	public int get(int index){
		return data[index];
	}
	
	public int size(){
		return size;
	}
	
	public int[] getBackingArray(){
		return data;
	}
}
