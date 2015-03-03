package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;


public class LoadLoc1 extends Instruction {

	public LoadLoc1(CodeBlock ins){
		super(ins, Opcode.LOADLOC1);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if (dcode) codeEmittor.emitCall("insnLOADLOC1");
		else codeEmittor.emitInlineLoadLocN(1,dcode);
		codeblock.addCode0(opcode.getOpcode());
	}
}
