package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class OCallDyn extends Instruction {
	
	final int arity;
	
	public OCallDyn(CodeBlock ins, int arity) {
		super(ins, Opcode.OCALLDYN);
		this.arity = arity;
	}
	
	public String toString() { return "OCALLDYN " + arity; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(arity);
	}

}
