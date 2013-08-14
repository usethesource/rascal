package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;


public class Function {
	 final String name;
	 final int scope;
	 final int nformals;
	 final int nlocals;
	 final int maxstack;
	 final CodeBlock codeblock;
	 IValue[] constantStore;
	
	public Function(String name, int scope, int nformals, int nlocals, int maxstack, CodeBlock codeblock){
		this.name = name;
		this.scope = scope;
		this.nformals = nformals;
		this.nlocals = nlocals;
		this.maxstack = maxstack;
		this.codeblock = codeblock;
	}
	
	public void  finalize(Map<String, Integer> codeMap, Map<String, Integer> constructorMap, boolean listing){
		codeblock.done(name, codeMap, constructorMap, listing);
		this.constantStore = codeblock.getConstants();
	}
}
