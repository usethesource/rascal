package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Resume1 extends Instruction {
	
	String function;
	
	public Resume1(CodeBlock ins, String function) {
		super(ins, Opcode.RESUME1);
		this.function = function;
	}
	
	public String toString() { return "RESUME1 " + function + "[" + ins.codeMap.get(function) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		Object o = ins.codeMap.get(function);
		if(o == null){
			throw new RuntimeException("PANIC: undefined function " + function);
		}
		ins.addCode((int)o);
	}


}
