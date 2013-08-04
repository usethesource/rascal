package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public abstract class Instruction {
	
	protected Opcode opcode;
	protected CodeBlock ins;

	Instruction(CodeBlock ins, Opcode opc){
		this.opcode = opc;
		this.ins = ins;
	}
	
	public int pcIncrement() {
		return opcode.getIncrement();
	}
	
	public String toString(){
		return opcode.toString();
	}
	
	public void generate(){
		 ins.addCode(opcode.getOpcode());
	}
   
}
