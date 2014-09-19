package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.NameMangler;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

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

	public void finalize(Map<String, Integer> functionMap) {
		if (funIn != null) {
			this.scopeIn = functionMap.get(funIn);
		}
	}

	public void finalize(BytecodeGenerator codeEmittor, Map<String, Integer> functionMap, int oid) {
		if (funIn != null) {
			this.scopeIn = functionMap.get(funIn);
		}
	}

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
}
