package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadVarRef extends Instruction {
	
	final int scope;
	final int pos;
	
	public LoadVarRef(CodeBlock ins, int scope, int pos) {
		super(ins, Opcode.LOADVARREF);
		this.scope = scope;
		this.pos = pos;
	}
	
	public String toString() { return "LOADVARREF " + scope + ", " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(scope);
		codeblock.addCode(pos);
	}

}
