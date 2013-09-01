package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Call extends Instruction {

	final String fuid;
	
	public Call(CodeBlock ins, String fuid){
		super(ins, Opcode.CALL);
		this.fuid = fuid;
	}
	
	public String toString() { return "CALL " + fuid + "[" + codeblock.getFunctionIndex(fuid) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getFunctionIndex(fuid));
	}

}
