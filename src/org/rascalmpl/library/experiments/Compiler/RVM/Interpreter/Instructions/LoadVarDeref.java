package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class LoadVarDeref extends Instruction {
	
	final String fuid;
	final int pos;
	
	public LoadVarDeref(CodeBlock ins, String fuid, int pos) {
		super(ins, Opcode.LOADVARDEREF);
		this.fuid = fuid;
		this.pos = pos;
	}

	public String toString() { return "LOADVARDEREF " + fuid + " [ " + codeblock.getFunctionIndex(fuid) + " ] " + ", " + pos; }
	
	public void generate(Generator codeEmittor){
		codeblock.addCode2(opcode.getOpcode(), (pos == -1) ? codeblock.getConstantIndex(codeblock.vf.string(fuid))
                					  					   : codeblock.getFunctionIndex(fuid),
                					  		    pos);
	}
}
