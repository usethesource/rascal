package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;


public class LoadLoc2 extends Instruction {

	public LoadLoc2(CodeBlock ins){
		super(ins, Opcode.LOADLOC2);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADLOC2");
		//codeEmittor.emitInlineLoadLocN(2,dcode);
		codeblock.addCode0(opcode.getOpcode());
	}

}
