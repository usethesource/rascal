package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class LoadVar extends Instruction {

	int pos;
	int scope;
	
	public LoadVar(CodeBlock ins, int scope, int pos){
		super(ins, Opcode.LOADVAR);
		this.scope = scope;
		this.pos = pos;
	}
	
	public String toString() { return "LOADVar " + scope + ", " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(scope);
		codeblock.addCode(pos);
	}
}
