package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadLocDeref extends Instruction {

	int pos;
	
	public LoadLocDeref(CodeBlock ins, int pos) {
		super(ins, Opcode.LOADLOCDEREF);
		this.pos = pos;
	}
	
	public String toString() { return "LOADLOCDEREF " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSI_A("insnLOADLOCDEREF", pos,debug);
	}
}
