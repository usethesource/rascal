package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public abstract class Instruction {
	
	protected Opcode opcode;
	protected CodeBlock codeblock;

	Instruction(CodeBlock cb, Opcode opc){
		this.opcode = opc;
		this.codeblock = cb;
	}
	
	public int pcIncrement() {
		return opcode.getPcIncrement();
	}
	
	public int spIncrement() {
		int n = opcode.getSpIncrement();
		if(n == -1000)
			throw new RuntimeException("Instruction with varyadic sp, should specialize spIncrement");
		return n;
	}
	
	public String toString(){
		return opcode.toString();
	}
	
	public void generate(){
		 codeblock.addCode(opcode.getOpcode());
	}
   
}
