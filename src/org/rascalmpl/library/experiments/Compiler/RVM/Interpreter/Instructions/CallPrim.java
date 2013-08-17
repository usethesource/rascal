package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Primitive;

public class CallPrim extends Instruction {

	Primitive prim;
	int arity;
	
	public CallPrim(CodeBlock ins, Primitive prim, int arity){
		super(ins, Opcode.CALLPRIM);
		this.prim = prim;
		this.arity = arity;
	}
	
	public String toString() { return "CALLPRIM " + prim + ", " + arity; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(prim.ordinal());
		codeblock.addCode(arity);
	}
}
