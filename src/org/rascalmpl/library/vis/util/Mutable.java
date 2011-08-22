package org.rascalmpl.library.vis.util;

public class Mutable<T> {
	
	private T val;
	
	public Mutable(T initialVal){
		this.val = initialVal;
	}
	
	public void set(T v){
		this.val = v;
	}
	
	public T get(){
		return val;
	}
}
