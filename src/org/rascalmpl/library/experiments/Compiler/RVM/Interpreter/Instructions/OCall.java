package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

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

	public String toString() {
		return "OCALL " + fuid + ", " + arity + " [ " + codeblock.getOverloadedFunctionIndex(fuid) + " ]" + ", " + src;
	}

	public void generate(BytecodeGenerator codeEmittor, boolean dcode) {

		if (dcode)
			codeEmittor.emitCall("dinsnOCALL", codeblock.getOverloadedFunctionIndex(fuid));

		// codeEmittor.emitOCall("OverLoadedHandlerOID" + codeblock.getOverloadedFunctionIndex(fuid),continuationPoint) ; // TODO ,arity);
		// codeEmittor.emitOCallV2(codeblock.getOverloadedFunctionIndex(fuid), arity);
		codeEmittor.emitOptimizedOcall(fuid,codeblock.getOverloadedFunctionIndex(fuid), arity, dcode) ;
		
		//codeEmittor.emitVoidCallWithArgsSSFII("jvmOCALL", codeblock.getOverloadedFunctionIndex(fuid), arity, dcode);

		codeblock.addCode2(opcode.getOpcode(), codeblock.getOverloadedFunctionIndex(fuid), arity);
		codeblock.addCode(codeblock.getConstantIndex(src));
	}
}
