package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class OCall extends Instruction {
	
	final String fuid;
	final int arity;
	
	public OCall(CodeBlock ins, String fuid, int arity) {
		super(ins, Opcode.OCALL);
		this.fuid = fuid;
		this.arity = arity;
	}
	
	public String toString() { return "OCALL " + fuid + ", " + arity + " [ " + codeblock.getOverloadedFunctionIndex(fuid) + " ]"; }
		
	public void generate(){
		System.out.println("\tOCALL " + fuid + " // oid" +  codeblock.getOverloadedFunctionIndex(fuid));
		codeblock.addCode2(opcode.getOpcode(), codeblock.getOverloadedFunctionIndex(fuid), arity);
	}
}
