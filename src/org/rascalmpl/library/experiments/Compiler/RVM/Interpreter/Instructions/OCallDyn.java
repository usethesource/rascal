package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class OCallDyn extends Instruction {
	
	final int arity;
	final int types;
	
	public OCallDyn(CodeBlock ins, int types, int arity) {
		super(ins, Opcode.OCALLDYN);
		this.arity = arity;
		this.types = types;
	}
	
	public String toString() { return "OCALLDYN " + types + ", " + arity; }
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnOCALLDYN", types, arity);
		
		codeblock.addCode2(opcode.getOpcode(), types, arity);
	}
}
