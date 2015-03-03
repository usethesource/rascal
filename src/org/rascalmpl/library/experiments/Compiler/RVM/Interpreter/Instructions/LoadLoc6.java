package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;


public class LoadLoc6 extends Instruction {

	public LoadLoc6(CodeBlock ins){
		super(ins, Opcode.LOADLOC6);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if (dcode) codeEmittor.emitCall("insnLOADLOC6");
		else codeEmittor.emitInlineLoadLocN(6,dcode);
		codeblock.addCode0(opcode.getOpcode());
	}
}
