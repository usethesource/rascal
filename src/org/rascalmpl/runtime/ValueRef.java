/*
 * Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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