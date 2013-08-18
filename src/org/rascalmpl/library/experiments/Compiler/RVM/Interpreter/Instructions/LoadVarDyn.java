package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadVarDyn extends Instruction {
	
	public LoadVarDyn(CodeBlock ins){
		super(ins, Opcode.LOADVARDYN);
	}
}
