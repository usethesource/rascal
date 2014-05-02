package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CallConstr extends Instruction {
	
	final String fuid;
	final int arity;
	//final ISourceLocation src;
	
	public CallConstr(CodeBlock ins, String fuid, int arity/*, ISourceLocation src*/) {
		super(ins, Opcode.CALLCONSTR);
		this.fuid = fuid;
		this.arity = arity;
		//this.src = src;
	}
	
	public String toString() { return "CALL " + fuid + ", " + arity + " [ " + codeblock.getConstructorIndex(fuid) + " ]" /*+ ", " + src*/; }
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), codeblock.getConstructorIndex(fuid), arity);
		//codeblock.addCode(codeblock.getConstantIndex(src));
	}

}
