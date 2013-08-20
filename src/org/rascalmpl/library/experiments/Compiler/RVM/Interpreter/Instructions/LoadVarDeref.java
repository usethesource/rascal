package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadVarDeref extends Instruction {
	
	final int scope;
	final int pos;
	
	public LoadVarDeref(CodeBlock ins, int scope, int pos) {
		super(ins, Opcode.LOADVARDEREF);
		this.scope = scope;
		this.pos = pos;
	}

	public String toString() { return "LOADVARDEREF " + scope + ", " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(scope);
		codeblock.addCode(pos);
	}

}
