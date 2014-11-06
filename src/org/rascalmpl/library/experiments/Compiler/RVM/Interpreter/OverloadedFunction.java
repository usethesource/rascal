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
	
	public String toString(){
		StringBuilder sb = new StringBuilder("Overloaded: ");
		if(functions.length > 0){
			sb.append("functions:");
			for(int i = 0; i < functions.length; i++){
				sb.append(" ").append(functions[i]);
			}
		}
		if(constructors.length > 0){
			sb.append("; constructors:");
			for(int i = 0; i < constructors.length; i++){
				sb.append(" ").append(constructors[i]);
			}
		}
		return sb.toString();
	}

}
