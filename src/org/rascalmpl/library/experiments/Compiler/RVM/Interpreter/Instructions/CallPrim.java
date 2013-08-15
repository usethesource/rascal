package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Primitive;

public class CallPrim extends Instruction {

	Primitive prim;
	
	public CallPrim(CodeBlock ins, Primitive prim){
		super(ins, Opcode.CALLPRIM);
		this.prim = prim;
	}
	
	public String toString() { return "CALLPRIM " + prim; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(prim.ordinal());
	}
}
