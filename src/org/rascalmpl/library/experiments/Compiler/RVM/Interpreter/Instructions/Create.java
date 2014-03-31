package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Create extends Instruction {
	
	final String fuid;
	final int arity;
	
	public Create(CodeBlock ins, String fuid, int arity) {
		super(ins, Opcode.CREATE);
		this.fuid = fuid;
		this.arity = arity;
	}
	
	public String toString() { return "CREATE " + fuid + ", " + arity + " [ " + codeblock.getFunctionIndex(fuid) + " ]"; }
	
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("jvmCREATE", codeblock.getFunctionIndex(fuid), arity);
		codeblock.addCode2(opcode.getOpcode(),codeblock.getFunctionIndex(fuid), arity);
	}

}
