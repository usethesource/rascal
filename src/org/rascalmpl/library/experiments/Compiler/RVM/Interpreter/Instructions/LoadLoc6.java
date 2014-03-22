package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;


public class LoadLoc6 extends Instruction {

	public LoadLoc6(CodeBlock ins){
		super(ins, Opcode.LOADLOC6);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADLOC6");
		codeblock.addCode0(opcode.getOpcode());
	}
}
