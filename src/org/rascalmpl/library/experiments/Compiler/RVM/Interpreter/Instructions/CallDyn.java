package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class CallDyn extends Instruction {
	
	final int arity;

	public CallDyn(CodeBlock ins, int arity){
		super(ins, Opcode.CALLDYN);
		this.arity = arity;
	}
	
	public String toString() { return "CALLDYN " + arity; }
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnCALLDYN", arity);
		codeblock.addCode1(opcode.getOpcode(), arity);
	}
}
