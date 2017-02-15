package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class UnwrapThrownVar extends Instruction {
	
	final String fuid;
	final int pos;
	
	public UnwrapThrownVar(CodeBlock ins, String fuid, int pos) {
		super(ins, Opcode.UNWRAPTHROWNVAR);
		this.fuid = fuid;
		this.pos = pos;
	}
	
	public String toString() { 
		return "UNWRAPTHROWNVAR " + fuid + ", " + pos + " [" + codeblock.getFunctionIndex(fuid) + ", " + pos + "]";
	}
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall2(opcode.name(), codeblock.getFunctionName(fuid), pos);
		
		if(pos == -1){
			codeEmittor.emitCallWithArgsSSFI_S("UNWRAPTHROWNVARMODULE", codeblock.getFunctionIndex(fuid)); 
		} else {
			codeEmittor.emitCallWithArgsSSFII_S("UNWRAPTHROWNVARSCOPED", codeblock.getFunctionIndex(fuid), pos); 
		}
	}
}
