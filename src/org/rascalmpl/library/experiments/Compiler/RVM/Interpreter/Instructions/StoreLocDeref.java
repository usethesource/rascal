package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class StoreLocDeref extends Instruction {
	
	int pos;
	
	public StoreLocDeref(CodeBlock ins, int pos) {
		super(ins, Opcode.STORELOCDEREF);
		this.pos = pos;
	}
	
	public String toString() { return "STORELOCDEREF " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall1(opcode.name(), pos);
		
		//codeEmittor.emitVoidCallWithArgsSSI_S("insnSTORELOCDEREF", pos, debug);
		codeEmittor.emitInlineStoreLocDeref(pos);
	}
}
