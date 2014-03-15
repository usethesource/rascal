package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;


public class LoadLoc8 extends Instruction {

	public LoadLoc8(CodeBlock ins){
		super(ins, Opcode.LOADLOC8);
	}
	public void generate(){
		System.out.println("LOADLOC8");
		codeblock.addCode0(opcode.getOpcode());
	}
}
