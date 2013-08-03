package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;


public class Halt extends Instruction {

	Halt(Instructions ins){
		super(ins, Opcode.HALT);
	}

}
