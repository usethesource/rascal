package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class OCall extends Instruction {
	
	final String fuid;
	final int arity;
	
	public OCall(CodeBlock ins, String fuid, int arity) {
		super(ins, Opcode.OCALL);
		this.fuid = fuid;
		this.arity = arity;
	}
	
	public String toString() { return "OCALL " + fuid + ", " + arity + " [ " + codeblock.getOverloadedFunctionIndex(fuid) + " ]"; }
		
	public void generate(Generator codeEmittor){
		System.out.println("\tOCALL " + fuid + " // oid" +  codeblock.getOverloadedFunctionIndex(fuid));
		
		codeEmittor.emitCall("OverLoadedHanderIOD" + codeblock.getOverloadedFunctionIndex(fuid),arity);
		
		codeblock.addCode2(opcode.getOpcode(), codeblock.getOverloadedFunctionIndex(fuid), arity);
	}
}
