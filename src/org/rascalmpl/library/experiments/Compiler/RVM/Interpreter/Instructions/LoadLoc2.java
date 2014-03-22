package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;


public class LoadLoc2 extends Instruction {

	public LoadLoc2(CodeBlock ins){
		super(ins, Opcode.LOADLOC2);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADLOC2");
		codeblock.addCode0(opcode.getOpcode());
	}

}
