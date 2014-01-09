package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadLocRef extends Instruction {
	
	final int pos;
	
	public LoadLocRef(CodeBlock ins, int pos) {
		super(ins, Opcode.LOADLOCREF);
		this.pos = pos;
	}

	public String toString() { return "LOADLOCREF " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}
}
