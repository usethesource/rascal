package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class LoadConstr extends Instruction {
	
	final String constr;
	
	public LoadConstr(CodeBlock ins, String constr) {
		super(ins, Opcode.LOADCONSTR);
		this.constr = constr;
	}
	
	public String toString() { return "LOADCONSTR " + constr + "[" + codeblock.getConstructorIndex(constr) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getConstructorIndex(constr));
	}

	
}
