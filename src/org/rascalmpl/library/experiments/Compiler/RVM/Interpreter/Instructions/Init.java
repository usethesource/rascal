package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Init extends Instruction {

	final int arity;
	
	public Init(CodeBlock ins, int arity) {
		super(ins, Opcode.INIT);
		this.arity = arity;
	}
	
	public String toString() { return "INIT " + arity; }
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnINIT", arity);
		codeblock.addCode1(opcode.getOpcode(), arity);
	}
		
}
