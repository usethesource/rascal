package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class CallConstr extends Instruction {
	
	final String fuid;
	final int arity;
	
	public CallConstr(CodeBlock ins, String fuid, int arity) {
		super(ins, Opcode.CALLCONSTR);
		this.fuid = fuid;
		this.arity = arity;
	}
	
	public String toString() { return "CALLCONSTRUCTOR " + fuid + ", " + arity + " [ " + codeblock.getConstructorIndex(fuid) + " ]"; }
	
	public void generate(Generator codeEmittor){
		codeEmittor.emitCall("insnCALLCONSTR", codeblock.getConstructorIndex(fuid), arity);
		codeblock.addCode2(opcode.getOpcode(), codeblock.getConstructorIndex(fuid), arity);
	}

}
