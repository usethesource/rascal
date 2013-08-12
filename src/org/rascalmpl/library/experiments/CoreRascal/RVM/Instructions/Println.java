package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Println extends Instruction {
	
	public Println(CodeBlock cb){
		super(cb, Opcode.PRINTLN);
	}
}
