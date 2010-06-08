package org.rascalmpl.parser.sgll.util;

public class LinearObjectIntegerKeyedMap<K, V>{
	private final static int DEFAULT_SIZE = 8;
	
	private K[] keys;
	private int[] intKeys;
	private V[] values;
	
	private int size;
	
	public LinearObjectIntegerKeyedMap(){
		super();
		
		keys = (K[]) new Object[DEFAULT_SIZE];
		intKeys = new int[DEFAULT_SIZE];
		values = (V[]) new Object[DEFAULT_SIZE];
	}
	
	public void enlarge(){
		K[] oldKeys = keys;
		keys = (K[]) new Object[size << 1];
		System.arraycopy(oldKeys, 0, keys, 0, size);
		
		int[] oldIntKeys = intKeys;
		intKeys = new int[size << 1];
		System.arraycopy(oldIntKeys, 0, intKeys, 0, size);

		V[] oldValues = values;
		values = (V[]) new Object[size << 1];
		System.arraycopy(oldValues, 0, values, 0, size);
	}
	
	public void add(K key, int intKey, V value){
		if(size == keys.length){
			enlarge();
		}
		
		keys[size] = key;
		intKeys[size] = intKey;
		values[size++] = value;
	}
	
	public K getKey(int index){
		return keys[index];
	}
	
	public int getIntKey(int index){
		return intKeys[index];
	}
	
	public V getValue(int index){
		return values[index];
	}
	
	public V findValue(K key, int intKey){
		for(int i = size - 1; i >= 0; i--){
			if(keys[i] == key && intKeys[i] == intKey){
				return values[i];
			}
		}
		return null;
	}
	
	public void clear(){
		int length = keys.length;
		keys = (K[]) new Object[length];
		values = (V[]) new Object[length];
		size = 0;
	}
	
	public void dirtyClear(){
		size = 0;
	}
	
	public int size(){
		return size;
	}
}
