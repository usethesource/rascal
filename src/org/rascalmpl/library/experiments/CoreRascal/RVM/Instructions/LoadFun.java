package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class LoadFun extends Instruction {

	String function;
	
	public LoadFun(CodeBlock ins, String function){
		super(ins, Opcode.LOADFUN);
		this.function = function;
	}
	
	public String toString() { return "LOADFUN " + function + "[" + ins.codeMap.get(function) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		Object o = ins.codeMap.get(function);
		if(o == null){
			throw new RuntimeException("PANIC: undefined function " + function);
		}
		ins.addCode((int)o);
	}

}
