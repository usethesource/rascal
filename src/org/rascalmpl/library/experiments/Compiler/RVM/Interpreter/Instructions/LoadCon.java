package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadCon extends Instruction {

	int constant;
	
	public LoadCon(CodeBlock cb, int constant){
		super(cb, Opcode.LOADCON);
		this.constant = constant;
	}
	
	public String toString() { return "LOADCON " + constant + "[" + codeblock.getConstantValue(constant) + "]"; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), constant);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		IValue val = codeblock.getConstantValue(constant) ;
		codeEmittor.emitCallWithArgsSSFI("insnLOADCON", constant, debug);
//		codeEmittor.emitInlineLoadConOrType(constant,true,debug);
	}
}
