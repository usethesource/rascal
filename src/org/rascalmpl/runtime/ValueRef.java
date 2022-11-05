package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import io.usethesource.vallang.IValue;

public final class ValueRef<T extends IValue> {
	private T value;
	private String name;
	
	public ValueRef(){
		this.name = "";
		this.value  = null;
	}

	public ValueRef(T value) {
		this.name = "";
		this.value = value;
	}
	
	public ValueRef(String name, T value) {
		this.name = name;
		this.value = value;
	}
	
	public T getValue() {
//		if(name == "facts") {
//			IMap m = (IMap) value;
//			Iterator<Entry<IValue,IValue>> iter = m.entryIterator();
//			System.err.println("Value of facts/" + hashCode() + " [" + m.size() + " entries]:");
//			while (iter.hasNext()) {
//				Entry<IValue,IValue> entry = iter.next();
//				System.err.println("\t" + entry.getKey() + ": " + entry.getValue());
//			}
//		}
		return this.value;
	}
	
	public void setValue(T value) {
//		if(name == "facts") {
//			IMap mold = (IMap) this.value;
//			IMap m = (IMap) value;
//			if( m.size() < mold.size()) {
//				System.err.println("facts gets smaller");
//			}
//			
//			System.err.println("Set facts/" + hashCode() + " to: [" + m.size() + " entries]:");
//			
//			Iterator<Entry<IValue,IValue>> iter = m.entryIterator();
//			while (iter.hasNext()) {
//				Entry<IValue,IValue> entry = iter.next();
//				System.err.println("\t" + entry.getKey() + ": " + entry.getValue());
//			}
//		}
		this.value = value;
	}
	
	public String toString() {
		return "ValueRef[" + name + ":" + hashCode() + "](" + value + ")";
	}
}