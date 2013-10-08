package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;

public class OverloadedFunction {
	
	final int[] functions;
	final int[] constructors;
	final String funIn;
	int scopeIn = -1;
	
	public OverloadedFunction(int[] functions, int[] constructors, String funIn) {
		this.functions = functions;
		this.constructors = constructors;
		this.funIn = funIn;
	}
	
	public void  finalize(Map<String, Integer> functionMap){
		if(funIn != null) {
			this.scopeIn = functionMap.get(funIn);
		}
	}

}
