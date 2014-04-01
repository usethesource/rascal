package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class CallDyn extends Instruction {
	
	final int arity;
	final int continuationPoint ;

	public CallDyn(CodeBlock ins, int arity, int cp){
		super(ins, Opcode.CALLDYN);
		this.arity = arity;
		this.continuationPoint = cp ;
	}
	
	public String toString() { return "CALLDYN " + arity; }
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitInlineCalldyn(arity, continuationPoint,dcode) ;
		codeblock.addCode1(opcode.getOpcode(), arity);
	}
}
