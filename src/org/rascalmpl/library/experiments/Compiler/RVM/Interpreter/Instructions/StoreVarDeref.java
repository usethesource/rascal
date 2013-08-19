package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class StoreVarDeref extends Instruction {
	
	final int scope;
	final int pos;
	
	public StoreVarDeref(CodeBlock ins, int scope, int pos) {
		super(ins, Opcode.STOREVARDEREF);
		this.scope = scope;
		this.pos = pos;
	}

	public String toString() { return "STOREVARDEREF " + scope + ", " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(scope);
		codeblock.addCode(pos);
	}

}
