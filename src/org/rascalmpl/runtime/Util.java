/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.library.util.ToplevelType;

import io.usethesource.vallang.IValue;

public class Util {

	public static int getFingerprint(IValue val, boolean concretePatterns){
		return ToplevelType.getFingerprint(val, concretePatterns);
	}

	public static Map<java.lang.String,IValue> kwpMap(Object...objects){
		HashMap<String, IValue> m = new HashMap<String,IValue>();
		for(int i = 0; i < objects.length; i += 2) {
			m.put((String) objects[i], (IValue)objects[i+1]);
		}
		return m;
	}

	public static Map<java.lang.String,IValue> kwpMapExtend(java.util.Map<String,IValue> m, Object...objects){
		for(int i = 0; i < objects.length; i += 2) {
			m.put((String) objects[i], (IValue)objects[i+1]);
		}
		return m;
	}

	public static Map<java.lang.String,IValue> kwpMapRemoveRedeclared(java.util.Map<String,IValue> m, String...keys){
		boolean overlap = false;
		for(int i = 0; i < keys.length; i++) {
			if(m.containsKey(keys[i])) {
				overlap = true; break;
			}
		}
		if(overlap) {
			HashMap<String, IValue> mnew = new HashMap<String,IValue>();
			for(Entry<String,IValue> e : m.entrySet()) {
				if(!Arrays.asList(keys).contains(e.getKey())) {
					mnew.put(e.getKey(), e.getValue());
				}
			}
			return mnew;
		}
		return m;
	}


}
