package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Jmp extends Instruction {

	String label;

	public Jmp(CodeBlock ins, String label){
		super(ins, Opcode.JMP);
		this.label = label;
	}
	
	public String toString() { return "JMP " + label + " [" + codeblock.getLabelPC(label) + "]"; }
	
	public boolean computeStackSize(int oldStackSize){
		int newStackSize = oldStackSize + spIncrement();
		if(newStackSize > maxStackSize){
			maxStackSize = newStackSize;
			Instruction targetInstr = codeblock.getLabelInstruction(label);
			if(!targetInstr.busy){
				busy = true;
				computeStackSize(maxStackSize);
				busy = false;
			}
			return true;
		}
		return false;
	}
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getLabelPC(label));
	}
}
