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

	public void finalize(BytecodeGenerator codeEmittor, Map<String, Integer> functionMap, int oid) {
		if (funIn != null) {
			this.scopeIn = functionMap.get(funIn);
		}
		codeEmittor.emitOCallHandler("OverLoadedHandlerOID"+oid,funIn,scopeIn,functions,constructors);
		int funcListIndex = 0 ;
		for (int i : functions) {
			String fname = null;
			for (Map.Entry<String, Integer> e : functionMap.entrySet()) {
				if (i == e.getValue()) {
					fname = e.getKey();
					break;
				}
			}
			codeEmittor.emitOCallCALL(NameMangler.mangle(fname),funcListIndex++,false) ;
		}
		codeEmittor.emitOCallEnd();
	}
}
