package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class LoadConRef extends Instruction {
	
	final int pos;
	
	public LoadConRef(CodeBlock ins, int pos) {
		super(ins, Opcode.LOADCONREF);
		this.pos = pos;
	}

	public String toString() { return "LOADCONREF " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(pos);
	}
	
}
