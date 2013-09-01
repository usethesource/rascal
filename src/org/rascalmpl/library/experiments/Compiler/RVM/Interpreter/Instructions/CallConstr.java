package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CallConstr extends Instruction {
	
	final String constr;
	final int arity;
	
	public CallConstr(CodeBlock ins, String constr, int arity) {
		super(ins, Opcode.CALLCONSTR);
		this.constr = constr;
		this.arity = arity;
	}
	
	public int spIncrement() {
		return arity + 1;
	}
	
	public String toString() { return "CALL " + constr + ", " + arity + "[" + codeblock.getConstructorIndex(constr) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getConstructorIndex(constr));
		codeblock.addCode(arity);
	}

}
