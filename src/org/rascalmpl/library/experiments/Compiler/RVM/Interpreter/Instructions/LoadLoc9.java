package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadLoc9 extends Instruction {

	public LoadLoc9(CodeBlock ins){
		super(ins, Opcode.LOADLOC9);
	}
	public void generate(){
		System.out.println("LOADLOC9");
		codeblock.addCode0(opcode.getOpcode());
	}
}
