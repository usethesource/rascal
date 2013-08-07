package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class StoreLoc extends  Instruction {

	int pos;
	
	public StoreLoc(CodeBlock ins, int pos){
		super(ins, Opcode.STORELOC);
		this.ins = ins;
		this.pos = pos;
	}
	
	public String toString() { return "STORELOC " + pos; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(pos);
	}
}
