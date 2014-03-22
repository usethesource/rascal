package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class LoadLoc extends Instruction {

	int pos;
	
	public LoadLoc(CodeBlock ins, int pos){
		super(ins, Opcode.LOADLOC);
		this.pos = pos;
	}
	
	public String toString() { return "LOADLOC " + pos; }
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADLOC", pos) ;
		codeblock.addCode1(opcode.getOpcode(), pos);
	}
}
