package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;
import org.rascalmpl.value.ISourceLocation;

public class PushCallPrimN extends Instruction {

	RascalPrimitive prim;
	int arity;
	ISourceLocation src;
	
	public PushCallPrimN(CodeBlock ins, RascalPrimitive prim, int arity, ISourceLocation src){
		super(ins, Opcode.PUSHCALLPRIMN);
		this.prim = prim;
		this.arity = arity;
		this.src = src;
	}
	
	public String toString() { return "PUSHCALLPRIMN " + prim + ", " + arity + ", " + src; }
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), prim.ordinal(), arity);
		codeblock.addCode(codeblock.getConstantIndex(src));
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());

		codeEmittor.emitInlinePushCallPrimN(prim, arity, debug); 
	}
}
