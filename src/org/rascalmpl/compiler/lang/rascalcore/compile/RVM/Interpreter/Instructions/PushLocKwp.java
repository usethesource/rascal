package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushLocKwp extends Instruction {
	
	final String name;
	
	public PushLocKwp(CodeBlock ins, String name) {
		super(ins, Opcode.PUSHLOCKWP);
		this.name = name;
	}
	
	public String toString() { return "PUSHLOCKWP " + name + " [" + codeblock.getConstantIndex(codeblock.vf.string(name)) + "]"; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstantIndex(codeblock.vf.string(name)));
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall1(opcode.name(), codeblock.getConstantIndex(codeblock.vf.string(name)));
		
		codeEmittor.emitCallWithArgsSSFI_S("PUSHLOCKWP" , codeblock.getConstantIndex(codeblock.vf.string(name)));
	}
}
