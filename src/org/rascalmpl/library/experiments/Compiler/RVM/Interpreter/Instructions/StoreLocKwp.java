package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class StoreLocKwp extends Instruction {
	
	final String name;
	
	public StoreLocKwp(CodeBlock ins, String name) {
		super(ins, Opcode.STORELOCKWP);
		this.name = name;
	}
	
	public String toString() { return "STORELOC " + name + " [" + codeblock.getConstantIndex(codeblock.vf.string(name)) + "]"; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		
		codeEmittor.emitVoidCallWithArgsSSFI("insnSTORELOCKWP", codeblock.getConstantIndex(codeblock.vf.string(name)),dcode);
		
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstantIndex(codeblock.vf.string(name)));
	}

}
