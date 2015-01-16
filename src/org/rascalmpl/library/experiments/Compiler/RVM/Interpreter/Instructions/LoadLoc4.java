package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;


public class LoadLoc4 extends Instruction {

	public LoadLoc4(CodeBlock ins){
		super(ins, Opcode.LOADLOC4);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADLOC4");
		//codeEmittor.emitInlineLoadLocN(4,dcode);
		codeblock.addCode0(opcode.getOpcode());
	}
}
