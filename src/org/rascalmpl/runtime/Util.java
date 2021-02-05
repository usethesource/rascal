package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.library.util.ToplevelType;

import io.usethesource.vallang.IValue;

public class Util {

	private static Object object;

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
