package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Primitive;

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
