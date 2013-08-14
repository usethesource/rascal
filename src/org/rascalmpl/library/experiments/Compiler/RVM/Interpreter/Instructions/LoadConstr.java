package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

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
