package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class StoreLocKwp extends Instruction {
	
	final String name;
	
	public StoreLocKwp(CodeBlock ins, String name) {
		super(ins, Opcode.STORELOCKWP);
		this.name = name;
	}
	
	public String toString() { return "STORELOCKWP " + name + " [" + codeblock.getConstantIndex(codeblock.vf.string(name)) + "]"; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean debug){
		if (!debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitVoidCallWithArgsSSFI("insnSTORELOCKWP", codeblock.getConstantIndex(codeblock.vf.string(name)),debug);
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstantIndex(codeblock.vf.string(name)));
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitVoidCallWithArgsSSFI("insnSTORELOCKWP", codeblock.getConstantIndex(codeblock.vf.string(name)),debug);
	}
}
