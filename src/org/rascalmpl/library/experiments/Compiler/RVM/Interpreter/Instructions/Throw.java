package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Throw extends Instruction {
	
	private final ISourceLocation src;
	
	public Throw(CodeBlock ins, ISourceLocation src) {
		super(ins, Opcode.THROW);
		this.src = src;
	}
	
	public String toString() { return "THROW " + src; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstantIndex(src));
	}
}
