package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadCon extends Instruction {

	int constant;
	
	public LoadCon(CodeBlock cb, int constant){
		super(cb, Opcode.LOADCON);
		this.constant = constant;
	}
	
	public String toString() { return "LOADCON " + constant + "[" + codeblock.getConstantValue(constant) + "]"; }
	
	public void generate(){
		System.out.println("\tLOADCON " + constant + "[" + codeblock.getConstantValue(constant) + "]");
		codeblock.addCode1(opcode.getOpcode(), constant);
	}

}
