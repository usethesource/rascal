package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadVarAsRef extends Instruction {
	
	final int scope;
	final int pos;
	
	public LoadVarAsRef(CodeBlock ins, int scope, int pos) {
		super(ins, Opcode.LOADVAR_AS_REF);
		this.scope = scope;
		this.pos = pos;
	}
	
	public String toString() { return "LOADVAR_AS_REF " + scope + ", " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(scope);
		codeblock.addCode(pos);
	}

}
