package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class StoreLocDeref extends Instruction {
	
	int pos;
	
	public StoreLocDeref(CodeBlock ins, int pos) {
		super(ins, Opcode.STORELOCDEREF);
		this.pos = pos;
	}
	
	public String toString() { return "STORELOCDEREF " + pos; }
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}
}
