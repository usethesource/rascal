package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;


public class LoadLoc3 extends Instruction {

	public LoadLoc3(CodeBlock ins){
		super(ins, Opcode.LOADLOC3);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		//codeEmittor.emitInlineLoadLoc3(dcode);
		codeEmittor.emitCall("insnLOADLOC3");
		codeblock.addCode0(opcode.getOpcode());
	}
}
