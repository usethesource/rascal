package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Call extends Instruction {

	String fun;
	
	public Call(CodeBlock ins, String fun){
		super(ins, Opcode.CALL);
		this.fun = fun;
	}
	
	public String toString() { return "CALL " + fun + "[" + ins.codeMap.get(fun) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		Object o = ins.codeMap.get(fun);
		if(o == null){
			throw new RuntimeException("PANIC: undefined constant " + fun);
		}
		ins.addCode((int)o);
	}

}
