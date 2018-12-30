package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.HashMap;
import java.util.Map;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ToplevelType;

import io.usethesource.vallang.IValue;

public class Util {

	public int getFingerprint(IValue val, boolean concretePatterns){
		return ToplevelType.getFingerprint(val, concretePatterns);
	}

	public static Map<String,IValue> kwpMap(Object...objects){
		HashMap<String, IValue> m = new HashMap<String,IValue>();
		for(int i = 0; i < objects.length; i += 2) {
			m.put((String) objects[i], (IValue)objects[i+1]);
		}
		return m;
	}
	
	
}
