package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Resume0 extends Instruction {

	String function;
	
	public Resume0(CodeBlock ins, String function) {
		super(ins, Opcode.RESUME0);
		this.function = function;
	}
	
	public String toString() { return "RESUME0 " + function + "[" + ins.codeMap.get(function) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		Object o = ins.codeMap.get(function);
		if(o == null){
			throw new RuntimeException("PANIC: undefined function " + function);
		}
		ins.addCode((int)o);
	}

	
}
