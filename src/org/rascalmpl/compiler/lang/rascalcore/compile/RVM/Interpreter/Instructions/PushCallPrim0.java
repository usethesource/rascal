package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import io.usethesource.vallang.ISourceLocation;

public class PushCallPrim0 extends Instruction {

	RascalPrimitive prim;
	ISourceLocation src;
	
	public PushCallPrim0(CodeBlock ins, RascalPrimitive prim, ISourceLocation src){
		super(ins, Opcode.PUSHCALLPRIM0);
		this.prim = prim;
		this.src = src;
	}
	
	public String toString() { return "PUSHCALLPRIM0 " + prim + ", " + src; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), prim.ordinal());
		codeblock.addCode(codeblock.getConstantIndex(src));
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), prim.ordinal());

		codeEmittor.emitInlinePushCallPrim0(prim, codeblock.getConstantIndex(src)); 
	}
}
