package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class StoreLocDeref extends Instruction {
	
	int pos;
	
	public StoreLocDeref(CodeBlock ins, int pos) {
		super(ins, Opcode.STORELOCDEREF);
		this.pos = pos;
	}
	
	public String toString() { return "STORELOCDEREF " + pos; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean debug){
		if (!debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitVoidCallWithArgsSSI("insnSTORELOCDEREF", pos, debug);
		codeblock.addCode1(opcode.getOpcode(), pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitVoidCallWithArgsSSI("insnSTORELOCDEREF", pos, debug);
	}
}
