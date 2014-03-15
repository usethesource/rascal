package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;


public class LoadLoc2 extends Instruction {

	public LoadLoc2(CodeBlock ins){
		super(ins, Opcode.LOADLOC2);
	}
	public void generate(){
		System.out.println("LOADLOC2");
		codeblock.addCode0(opcode.getOpcode());
	}

}
