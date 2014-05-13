package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class OCall extends Instruction {
	
	final String fuid;
	final int arity;
	ISourceLocation src;
	
	public OCall(CodeBlock ins, String fuid, int arity, ISourceLocation src) {
		super(ins, Opcode.OCALL);
		this.fuid = fuid;
		this.arity = arity;
		this.src = src;
	}
	
	public String toString() { return "OCALL " + fuid + ", " + arity + " [ " + codeblock.getOverloadedFunctionIndex(fuid) + " ]" + ", " + src; }
		
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), codeblock.getOverloadedFunctionIndex(fuid), arity);
		codeblock.addCode(codeblock.getConstantIndex(src));
	}
}
