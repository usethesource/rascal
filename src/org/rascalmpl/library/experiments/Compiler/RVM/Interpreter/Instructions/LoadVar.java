package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadVar extends Instruction {

	final int pos;
	final String fuid;
	
	public LoadVar(CodeBlock ins, String fuid, int pos){
		super(ins, Opcode.LOADVAR);
		this.fuid = fuid;
		this.pos = pos;
	}
	
	public String toString() { return "LOADVar " + fuid + ", " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getFunctionIndex(fuid));
		codeblock.addCode(pos);
	}
}
