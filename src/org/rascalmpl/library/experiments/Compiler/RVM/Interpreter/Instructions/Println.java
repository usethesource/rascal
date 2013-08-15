package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Println extends Instruction {
	
	public Println(CodeBlock cb){
		super(cb, Opcode.PRINTLN);
	}
}
