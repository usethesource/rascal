package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CallConstr extends Instruction {
	
	final String fuid;
	
	public CallConstr(CodeBlock ins, String fuid) {
		super(ins, Opcode.CALLCONSTR);
		this.fuid = fuid;
	}
	
	public String toString() { return "CALL " + fuid + "[" + codeblock.getConstructorIndex(fuid) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getConstructorIndex(fuid));
	}

}
