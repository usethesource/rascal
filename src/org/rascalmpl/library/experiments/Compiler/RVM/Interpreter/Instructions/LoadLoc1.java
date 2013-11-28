package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;


public class LoadLoc1 extends Instruction {

	public LoadLoc1(CodeBlock ins){
		super(ins, Opcode.LOADLOC1);
	}

}
