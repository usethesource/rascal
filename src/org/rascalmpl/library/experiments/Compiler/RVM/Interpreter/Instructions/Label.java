package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Label extends Instruction {

	String label;
	
	public Label(CodeBlock ins, String label){
		super(ins, Opcode.LABEL);
		this.label = label;
		ins.defLabel(label, this);
	}
	
	public String toString() { return "LABEL " + label + " [" +  "]"; }
	
	public void generate(Generator codeEmittor){
		codeEmittor.emitLabel(label);
		System.out.println("LABEL " + label + " [" +  "]");
	}
}
