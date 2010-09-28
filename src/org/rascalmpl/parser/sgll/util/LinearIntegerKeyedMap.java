package org.rascalmpl.parser.sgll.util;

public class LinearIntegerKeyedMap<V>{
	private final static int DEFAULT_SIZE = 8;
	
	private int[] keys;
	private V[] values;
	
	private int size;
	
	public LinearIntegerKeyedMap(){
		super();
		
		keys = new int[DEFAULT_SIZE];
		values = (V[]) new Object[DEFAULT_SIZE];
	}
	
	public LinearIntegerKeyedMap(int size){
		super();
		
		keys = new int[size];
		values = (V[]) new Object[size];
	}
	
	public LinearIntegerKeyedMap(LinearIntegerKeyedMap<V> original){
		super();
		
		int[] oldKeys = original.keys;
		V[] oldValues = original.values;
		int length = oldKeys.length;
		
		size = original.size;
		keys = new int[length];
		System.arraycopy(oldKeys, 0, keys, 0, size);
		values = (V[]) new Object[length];
		System.arraycopy(oldValues, 0, values, 0, size);
	}
	
	public void enlarge(){
		int[] oldKeys = keys;
		keys = new int[size << 1];
		System.arraycopy(oldKeys, 0, keys, 0, size);

		V[] oldValues = values;
		values = (V[]) new Object[size << 1];
		System.arraycopy(oldValues, 0, values, 0, size);
	}
	
	public void add(int key, V value){
		while(size >= keys.length){
			enlarge();
		}
		
		keys[size] = key;
		values[size++] = value;
	}
	
	public int getKey(int index){
		return keys[index];
	}
	
	public V getValue(int index){
		return values[index];
	}
	
	public int findKey(int key){
		for(int i = size - 1; i >= 0; --i){
			if(keys[i] == key){
				return i;
			}
		}
		return -1;
	}
	
	public V findValue(int key){
		for(int i = size - 1; i >= 0; --i){
			if(keys[i] == key){
				return values[i];
			}
		}
		return null;
	}
	
	public int size(){
		return size;
	}
	
	public void clear(){
		int length = keys.length;
		keys = new int[length];
		values = (V[]) new Object[length];
		size = 0;
	}
	
	public void dirtyClear(){
		size = 0;
	}
}
