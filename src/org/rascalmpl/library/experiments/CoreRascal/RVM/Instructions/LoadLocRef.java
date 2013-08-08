package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class LoadLocRef extends Instruction {

	int pos;
	
	public LoadLocRef(CodeBlock ins, int pos) {
		super(ins, Opcode.LOADLOCREF);
		this.pos = pos;
	}
	
	public String toString() { return "LOADLOCREF " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(pos);
	}
	
}
