package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class OCall extends Instruction {
	
	final String fuid;
	final int arity;
	final int continuationPoint ;
	public OCall(CodeBlock ins, String fuid, int arity, int continuationPoint) {
		super(ins, Opcode.OCALL);
		this.fuid = fuid;
		this.arity = arity;
		this.continuationPoint = continuationPoint ;
	}
	
	public String toString() { return "OCALL " + fuid + ", " + arity + " [ " + codeblock.getOverloadedFunctionIndex(fuid) + " ]"; }
		
	public void generate(Generator codeEmittor, boolean dcode){

		if ( dcode ) codeEmittor.emitCall("dinsnOCALL", codeblock.getOverloadedFunctionIndex(fuid));

		codeEmittor.emitOCall("OverLoadedHandlerOID" + codeblock.getOverloadedFunctionIndex(fuid),continuationPoint) ; //    TODO ,arity);
		
		codeblock.addCode2(opcode.getOpcode(), codeblock.getOverloadedFunctionIndex(fuid), arity);
	}
}
