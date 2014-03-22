package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class LoadOFun extends Instruction {
	
	final String fuid;
	
	public LoadOFun(CodeBlock ins, String fuid) {
		super(ins, Opcode.LOADOFUN);
		this.fuid = fuid;
	}
	
	public String toString() { return "LOADOFUN " + fuid + " [ " + codeblock.getOverloadedFunctionIndex(fuid) + " ]"; }
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeblock.addCode1(opcode.getOpcode(), codeblock.getOverloadedFunctionIndex(fuid));
	}
}
