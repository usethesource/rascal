package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadLocDeref extends Instruction {

	int pos;
	
	public LoadLocDeref(CodeBlock ins, int pos) {
		super(ins, Opcode.LOADLOCDEREF);
		this.pos = pos;
	}
	
	public String toString() { return "LOADLOCDEREF " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}
}
