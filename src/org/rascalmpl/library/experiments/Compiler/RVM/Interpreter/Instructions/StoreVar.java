package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class StoreVar extends Instruction {

	final int pos;
	final String fuid;
	
	public StoreVar(CodeBlock ins, String fuid, int pos){
		super(ins, Opcode.STOREVAR);
		this.fuid = fuid;
		this.pos = pos;
	}
	
	public String toString() { return "STOREVAR " + fuid + " [ " + codeblock.getFunctionIndex(fuid)  + " ] " + ", " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getFunctionIndex(fuid));
		codeblock.addCode(pos);
	}
}
