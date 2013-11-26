package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Call extends Instruction {

	final String fuid;
	final int arity;
	
	public Call(CodeBlock ins, String fuid, int arity){
		super(ins, Opcode.CALL);
		this.fuid = fuid;
		this.arity = arity;
	}
	
	public String toString() { return "CALL " + fuid + ", " + arity + " [ " + codeblock.getFunctionIndex(fuid) + " ]"; }
		
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), arity);
	}

}
