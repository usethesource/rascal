package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class StoreVarDyn extends Instruction {
	
	public StoreVarDyn(CodeBlock ins){
		super(ins, Opcode.STOREVARDYN);
	}
}
