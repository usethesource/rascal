package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CallConstr extends Instruction {
	
	final String constr;
	
	public CallConstr(CodeBlock ins, String constr) {
		super(ins, Opcode.CALLCONSTR);
		this.constr = constr;
	}
	
	public String toString() { return "CALL " + constr + "[" + codeblock.getConstructorIndex(constr) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getConstructorIndex(constr));
	}

}
