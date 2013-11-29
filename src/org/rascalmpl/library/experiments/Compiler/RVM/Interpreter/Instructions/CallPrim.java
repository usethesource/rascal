package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;

public class CallPrim extends Instruction {

	RascalPrimitive prim;
	int arity;
	
	public CallPrim(CodeBlock ins, RascalPrimitive prim, int arity){
		super(ins, Opcode.CALLPRIM);
		this.prim = prim;
		this.arity = arity;
	}
	
	public String toString() { return "CALLPRIM " + prim + ", " + arity; }
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), prim.ordinal(), arity);
	}
}
