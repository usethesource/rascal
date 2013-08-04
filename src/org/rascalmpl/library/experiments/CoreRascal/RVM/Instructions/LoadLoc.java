package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class LoadLoc extends Instruction {

	int pos;
	
	public LoadLoc(CodeBlock ins, int pos){
		super(ins, Opcode.LOADLOC);
		this.pos = pos;
	}
	
	public String toString() { return "LOADLOC " + pos; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(pos);
	}
}
