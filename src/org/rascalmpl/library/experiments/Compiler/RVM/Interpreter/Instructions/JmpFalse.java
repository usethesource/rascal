package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class JmpFalse extends Instruction {

	String label;
	
	public JmpFalse(CodeBlock ins, String label){
		super(ins, Opcode.JMPFALSE);
		this.label = label;
	}
	
	public String toString() { return "JMPFALSE " + label + " [" + codeblock.getLabelPC(label) + "]"; }
	
	public void generate(Generator codeEmittor){
		System.out.println("\tJMPFALSE " + label + " [" + codeblock.getLabelPC(label) + "]");
		codeEmittor.emitJMPFALSE(label);
		codeblock.addCode1(opcode.getOpcode(), codeblock.getLabelPC(label));
	}
}
