package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;


public class LoadLoc3 extends Instruction {

	public LoadLoc3(CodeBlock ins){
		super(ins, Opcode.LOADLOC3);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if (dcode) codeEmittor.emitCall("insnLOADLOC3");
		else codeEmittor.emitInlineLoadLocN(3,dcode);
		codeblock.addCode0(opcode.getOpcode());
	}
}
