package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;


public class LoadLoc8 extends Instruction {

	public LoadLoc8(CodeBlock ins){
		super(ins, Opcode.LOADLOC8);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADLOC8");
		codeblock.addCode0(opcode.getOpcode());
	}
}
