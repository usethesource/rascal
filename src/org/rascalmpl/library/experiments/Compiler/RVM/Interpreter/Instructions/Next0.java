package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Next0 extends Instruction {

	public Next0(CodeBlock ins) {
		super(ins, Opcode.NEXT0);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		codeEmittor.emitCallWithArgsSSF("jvmNEXT0", dcode);
		codeblock.addCode0(opcode.getOpcode());
	}
}
