package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

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
		ins.addCode(opcode.getOpcode());
		ins.addCode(scope);
		ins.addCode(pos);
	}
}
