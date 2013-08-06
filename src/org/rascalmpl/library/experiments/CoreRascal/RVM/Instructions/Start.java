package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Start extends Instruction {

	String function;
	
	public Start(CodeBlock ins, String function) {
		super(ins, Opcode.START);
		this.function = function;
	}
	
	public String toString() { return "START " + function + "[" + ins.codeMap.get(function) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		Object o = ins.codeMap.get(function);
		if(o == null){
			throw new RuntimeException("PANIC: undefined function " + function);
		}
		ins.addCode((int)o);
	}

	
}
