package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadVarDeref extends Instruction {
	
	final String fuid;
	final int pos;
	
	public LoadVarDeref(CodeBlock ins, String fuid, int pos) {
		super(ins, Opcode.LOADVARDEREF);
		this.fuid = fuid;
		this.pos = pos;
	}

	public String toString() { return "LOADVARDEREF " + fuid + " [ " + codeblock.getFunctionIndex(fuid) + " ] " + ", " + pos; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getFunctionIndex(fuid));
		codeblock.addCode(pos);
	}

}
