package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class ApplyDyn extends Instruction {
	
	final int arity;
	
	public ApplyDyn(CodeBlock ins, int arity) {
		super(ins, Opcode.APPLYDYN);
		this.arity = arity;
	}
	
	public String toString() { return "APPLYDYN " + arity; }
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnAPPLYDYN", arity);
		codeblock.addCode1(opcode.getOpcode(), arity);
	}
}
