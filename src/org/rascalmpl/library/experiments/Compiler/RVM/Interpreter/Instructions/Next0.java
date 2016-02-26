package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Next0 extends Instruction {

	public Next0(CodeBlock ins) {
		super(ins, Opcode.NEXT0);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsFA_A("jvmNEXT0", debug);
		//codeEmittor.emitReturnValue2ACCU();
	}
}
