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

	public void finalize(Generator codeEmittor, Map<String, Integer> functionMap, int oid) {
		if (funIn != null) {
			this.scopeIn = functionMap.get(funIn);
		}
		codeEmittor.emitOCallHandler("OverLoadedHanderIOD"+oid,funIn,scopeIn,functions,constructors);
		int funcListIndex = 0 ;
		for (int i : functions) {
			String fname = null;
			for (Map.Entry<String, Integer> e : functionMap.entrySet()) {
				if (i == e.getValue()) {
					fname = e.getKey();
					break;
				}
			}
			codeEmittor.emitOCallCALL(NameMangler.mangle(fname),funcListIndex++) ;
			System.out.println("\t" + fname + "  // id " + i);
		}
		codeEmittor.emitOCallEnd();
		System.out.println("}");
	}
}
