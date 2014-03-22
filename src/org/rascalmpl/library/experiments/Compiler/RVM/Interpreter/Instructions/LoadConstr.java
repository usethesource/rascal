package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class LoadConstr extends Instruction {
	
	final String fuid;
	
	public LoadConstr(CodeBlock ins, String fuid) {
		super(ins, Opcode.LOADCONSTR);
		this.fuid = fuid;
	}
	
	public String toString() { return "LOADCONSTR " + fuid + "[" + codeblock.getConstructorIndex(fuid) + "]"; }
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADCONSTR", codeblock.getConstructorIndex(fuid));
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstructorIndex(fuid));
	}
}
