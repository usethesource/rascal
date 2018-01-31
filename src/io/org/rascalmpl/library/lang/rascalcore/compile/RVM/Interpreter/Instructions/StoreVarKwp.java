package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class StoreVarKwp extends Instruction {
	
	final String fuid;
	final String name;
	
	public StoreVarKwp(CodeBlock ins, String fuid, String name) {
		super(ins, Opcode.STOREVARKWP);
		this.fuid = fuid;
		this.name = name;
	}
	
	public String toString() { return "STOREVARKWP " + fuid + ", " + name + " [" + codeblock.getFunctionIndex(fuid) + ", " + codeblock.getConstantIndex(codeblock.vf.string(name)) + "]"; }
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), codeblock.getOverloadedFunctionIndex(fuid), codeblock.getConstantIndex(codeblock.vf.string(name)));	
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall2(opcode.name(), fuid, codeblock.getConstantIndex(codeblock.vf.string(name)));
		
		codeEmittor.emitVoidCallWithArgsFIIA("STOREVARKWP", codeblock.getFunctionIndex(fuid), codeblock.getConstantIndex(codeblock.vf.string(name)));
	
    }

}
