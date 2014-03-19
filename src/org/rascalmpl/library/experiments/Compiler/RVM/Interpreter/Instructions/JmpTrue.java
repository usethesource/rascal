package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class JmpTrue extends Instruction {

	String label;
	
	public JmpTrue(CodeBlock ins, String label){
		super(ins, Opcode.JMPTRUE);
		this.label = label;
	}
	
	public String toString() { return "JMPTRUE " + label + " [" + codeblock.getLabelPC(label) + "]"; }
	
	public void generate(Generator codeEmittor){
		codeEmittor.emitJMPTRUE(label);

		/* TODO debug */ codeEmittor.emitCall("dinsnJMPTRUE", codeblock.getLabelPC(label));

		codeEmittor.emitJMPTRUE(label);
		codeblock.addCode1(opcode.getOpcode(), codeblock.getLabelPC(label));
	}
}
