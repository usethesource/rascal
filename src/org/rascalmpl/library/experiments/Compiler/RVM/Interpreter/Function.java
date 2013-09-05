package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;


public class Function {
	 final String name;
	 final Type ftype;
	 int scopeId;
	 private String funIn;
	 int scopeIn = -1;
	 final int nformals;
	 final int nlocals;
	 final int maxstack;
	 final CodeBlock codeblock;
	 IValue[] constantStore;
	 Type[] typeConstantStore;
	
	public Function(String name, Type ftype, String funIn, int nformals, int nlocals, int maxstack, CodeBlock codeblock){
		this.name = name;
		this.ftype = ftype;
		this.funIn = funIn;
		this.nformals = nformals;
		this.nlocals = nlocals;
		this.maxstack = maxstack;
		this.codeblock = codeblock;
	}
	
	public void  finalize(Map<String, Integer> codeMap, Map<String, Integer> constructorMap, boolean listing){
		codeblock.done(name, codeMap, constructorMap, listing);
		this.scopeId = codeblock.getFunctionIndex(name);
		if(funIn != null) {
			this.scopeIn = codeblock.getFunctionIndex(funIn);
		}
		this.constantStore = codeblock.getConstants();
		this.typeConstantStore = codeblock.getTypeConstants();
	}
	
	public String getName() {
		return name;
	}
	
}
