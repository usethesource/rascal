package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class LoadFun extends Instruction {

	final String fuid;
	
	public LoadFun(CodeBlock ins, String fuid){
		super(ins, Opcode.LOADFUN);
		this.fuid = fuid;
	}
	
	public String toString() { return "LOADFUN " + fuid + "[" + codeblock.getFunctionIndex(fuid) + "]"; }
	
	public void generate(Generator codeEmittor){
		codeEmittor.emitCall("insnLOADFUN", codeblock.getFunctionIndex(fuid));

		codeblock.addCode1(opcode.getOpcode(), codeblock.getFunctionIndex(fuid));
	}

}
