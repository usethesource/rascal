package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public abstract class Instruction {
	
	protected Opcode opcode;
	protected CodeBlock codeblock;

	Instruction(CodeBlock cb, Opcode opc){
		this.opcode = opc;
		this.codeblock = cb;
	}
	
	public int pcIncrement() {
		return opcode.getIncrement();
	}
	
	public String toString(){
		return opcode.toString();
	}
	
	public void generate(){
		 codeblock.addCode(opcode.getOpcode());
	}
   
}
