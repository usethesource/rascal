package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class CallConstr extends Instruction {
	
	final String fuid;
	final int arity;
	//final ISourceLocation src;
	
	public CallConstr(CodeBlock ins, String fuid, int arity/*, ISourceLocation src*/) {
		super(ins, Opcode.CALLCONSTR);
		this.fuid = fuid;
		this.arity = arity;
		//this.src = src;
	}
	
	public String toString() { return "CALLCONSTRUCTOR " + fuid + ", " + arity + " [ " + codeblock.getConstructorIndex(fuid) + " ]"; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if ( !dcode ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSII("insnCALLCONSTR", codeblock.getConstructorIndex(fuid), arity,dcode);
		codeblock.addCode2(opcode.getOpcode(), codeblock.getConstructorIndex(fuid), arity);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( !debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSII("insnCALLCONSTR", codeblock.getConstructorIndex(fuid), arity,debug);
	}

}
