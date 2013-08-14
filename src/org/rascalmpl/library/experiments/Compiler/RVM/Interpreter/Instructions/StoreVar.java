package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class StoreVar extends Instruction {

	int pos;
	int scope;
	
	public StoreVar(CodeBlock ins, int scope, int pos){
		super(ins, Opcode.STOREVAR);
		this.scope = scope;
		this.pos = pos;
	}
	
	public String toString() { return "STOREVAR " + scope + ", " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(scope);
		codeblock.addCode(pos);
	}
}
