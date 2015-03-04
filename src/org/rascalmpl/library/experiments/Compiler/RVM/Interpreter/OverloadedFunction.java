package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;

public class OverloadedFunction {
	
	final int[] functions;
	final int[] constructors;
	final String funIn;
	int scopeIn = -1;
	
	public OverloadedFunction(final int[] functions, final int[] constructors, final String funIn) {
		this.functions = functions;
		this.constructors = constructors;
		this.funIn = funIn;
	}
	
	public void  finalize(final Map<String, Integer> functionMap){
		if(funIn != null) {
			this.scopeIn = functionMap.get(funIn);
		}
	}
	
	// The next four members are needed bij the bytecode generator.
	public int[] getFuntions() {
		return functions;
	}

	public int[] getConstructors() {
		return constructors;
	}
	
	public int getScope() {
		return scopeIn ;
	}
	
	public String getScopeFun() {
		return funIn ;
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
