package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Create extends Instruction {
	
	final String fuid;
	final int arity;
	
	public Create(CodeBlock ins, String fuid, int arity) {
		super(ins, Opcode.CREATE);
		this.fuid = fuid;
		this.arity = arity;
	}
	
	public String toString() { return "CREATE " + fuid + ", " + arity + " [ " + codeblock.getFunctionIndex(fuid) + " ]"; }
	
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(),codeblock.getFunctionIndex(fuid), arity);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall2(opcode.name(),codeblock.getFunctionName(fuid), arity);
		
		codeEmittor.emitCallWithArgsSSFII_A("jvmCREATE", codeblock.getFunctionIndex(fuid), arity);
		codeEmittor.emitIncSP(-arity);			// TODO move to code generator
		codeEmittor.emitReturnValue2ACCU();
	}
}
