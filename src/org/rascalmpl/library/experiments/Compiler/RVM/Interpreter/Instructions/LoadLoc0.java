package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;


public class LoadLoc0 extends Instruction {

	public LoadLoc0(CodeBlock ins){
		super(ins, Opcode.LOADLOC0);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if ( dcode ) codeEmittor.emitCall("insnLOADLOC0");
		else codeEmittor.emitInlineLoadLocN(0,dcode);
		codeblock.addCode0(opcode.getOpcode());
	}
}
