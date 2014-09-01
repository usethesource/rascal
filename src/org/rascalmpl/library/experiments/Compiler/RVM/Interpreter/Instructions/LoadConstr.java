package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadConstr extends Instruction {
	
	final String fuid;
	
	public LoadConstr(CodeBlock ins, String fuid) {
		super(ins, Opcode.LOADCONSTR);
		this.fuid = fuid;
	}
	
	public String toString() { return "LOADCONSTR " + fuid + "[" + codeblock.getConstructorIndex(fuid) + "]"; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode) {
		codeEmittor.emitCallWithArgsSSI("insnLOADCONSTR", codeblock.getConstructorIndex(fuid),dcode);
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstructorIndex(fuid));
	}
}
