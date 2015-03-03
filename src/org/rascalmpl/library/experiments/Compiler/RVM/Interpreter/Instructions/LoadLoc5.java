package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;


public class LoadLoc5 extends Instruction {

	public LoadLoc5(CodeBlock ins){
		super(ins, Opcode.LOADLOC5);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if (dcode) codeEmittor.emitCall("insnLOADLOC5");
		else codeEmittor.emitInlineLoadLocN(5,dcode);
		codeblock.addCode0(opcode.getOpcode());
	}
}
