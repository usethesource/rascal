package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadLocKwp extends Instruction {
	
	final String name;
	
	public LoadLocKwp(CodeBlock ins, String name) {
		super(ins, Opcode.LOADLOCKWP);
		this.name = name;
	}
	
	public String toString() { return "LOADLOCKWP " + name + " [" + codeblock.getConstantIndex(codeblock.vf.string(name)) + "]"; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstantIndex(codeblock.vf.string(name)));
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall1(opcode.name(), codeblock.getConstantIndex(codeblock.vf.string(name)));
		
		codeEmittor.emitCallWithArgsSFI_A("LOADLOCKWP" , codeblock.getConstantIndex(codeblock.vf.string(name)));
	}
}
