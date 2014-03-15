package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadLoc extends Instruction {

	int pos;
	
	public LoadLoc(CodeBlock ins, int pos){
		super(ins, Opcode.LOADLOC);
		this.pos = pos;
	}
	
	public String toString() { return "LOADLOC " + pos; }
	
	public void generate(){
		System.out.println("LOADLOC " + pos);
		codeblock.addCode1(opcode.getOpcode(), pos);
	}
}
