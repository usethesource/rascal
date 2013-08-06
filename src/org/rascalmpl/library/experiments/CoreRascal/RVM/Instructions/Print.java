package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Print extends Instruction {

	String constant;
	
	public Print(CodeBlock ins, String constant){
		super(ins, Opcode.PRINT);
		this.constant = constant;
	}
	
	public String toString() { return "PRINT " + constant + "[" + ins.constMap.get(constant) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		Object o = ins.constMap.get(constant);
		if(o == null){
			throw new RuntimeException("PANIC: undefined constant " + constant);
		}
		ins.addCode((int)o);
	}

}
