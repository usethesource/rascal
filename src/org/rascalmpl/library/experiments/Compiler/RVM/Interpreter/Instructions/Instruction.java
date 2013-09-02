package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public abstract class Instruction {
	
	protected Opcode opcode;
	protected CodeBlock codeblock;
	public int maxStackSize;
	public boolean busy = false;

	Instruction(CodeBlock cb, Opcode opc){
		this.opcode = opc;
		this.codeblock = cb;
		this.maxStackSize = spIncrement();
	}
	
	public int pcIncrement() {
		return opcode.getPcIncrement();
	}
	
	public int spIncrement() {
		int n = opcode.getSpIncrement();
		if(n == -1000)
			throw new RuntimeException("Instruction with varyadic sp, should specialize spIncrement");
		return n;
	}
	
	public boolean updateStackSize(int sp){
		if(sp < maxStackSize)
			return false;
		maxStackSize = sp;
		return true;
	}
	
	public boolean computeStackSize(int oldStackSize){
		int newStackSize = oldStackSize + spIncrement();
		if(newStackSize > maxStackSize){
			maxStackSize = newStackSize;
			return true;
		}
		return false;
	}
	
	public String toString(){
		return opcode.toString();
	}
	
	public void generate(){
		 codeblock.addCode(opcode.getOpcode());
	}
   
}
