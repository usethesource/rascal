package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import io.usethesource.vallang.ISourceLocation;

public class OCallDyn extends Instruction {
	
	final int arity;
	final int types;
	final ISourceLocation src;
	
	public OCallDyn(CodeBlock ins, int types, int arity, ISourceLocation src) {
		super(ins, Opcode.OCALLDYN);
		this.arity = arity;
		this.types = types;
		this.src = src;
	}
	
	public String toString() { return "OCALLDYN " + types + ", " + arity + " " + src; }
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), types, arity);
		codeblock.addCode(codeblock.getConstantIndex(src));
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall2(opcode.name(), Integer.toString(types), arity);
		
		// TODO add source line.
		codeEmittor.emitCallWithArgsSSFII_S("jvmOCALLDYN", types, arity);
		//codeEmittor.emitIncSP(-arity-1); ??
		codeEmittor.emitReturnValue2ACCU();
	}
}
