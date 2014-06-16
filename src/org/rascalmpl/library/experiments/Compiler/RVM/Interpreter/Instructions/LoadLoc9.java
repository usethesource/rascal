package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadLoc9 extends Instruction {

	public LoadLoc9(CodeBlock ins){
		super(ins, Opcode.LOADLOC9);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADLOC9");
		//codeEmittor.emitInlineLoadLocN(9,dcode);
		codeblock.addCode0(opcode.getOpcode());
	}
}
