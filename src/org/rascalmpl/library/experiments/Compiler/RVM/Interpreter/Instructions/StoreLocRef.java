package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class StoreLocRef extends Instruction {
	
	int pos;
	
	public StoreLocRef(CodeBlock ins, int pos) {
		super(ins, Opcode.STORELOCREF);
		this.pos = pos;
	}
	
	public String toString() { return "STORELOCREF " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(pos);
	}

}
