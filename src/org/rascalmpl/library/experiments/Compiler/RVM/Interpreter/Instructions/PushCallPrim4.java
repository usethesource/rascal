package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import io.usethesource.vallang.ISourceLocation;

public class PushCallPrim4 extends Instruction {

	RascalPrimitive prim;
	ISourceLocation src;
	
	public PushCallPrim4(CodeBlock ins, RascalPrimitive prim, ISourceLocation src){
		super(ins, Opcode.PUSHCALLPRIM4);
		this.prim = prim;
		this.src = src;
	}
	
	public String toString() { return "PUSHCALLPRIM4 " + prim + ", " + src; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), prim.ordinal());
		codeblock.addCode(codeblock.getConstantIndex(src));
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), prim.ordinal());

		codeEmittor.emitInlinePushCallPrim4(prim, codeblock.getConstantIndex(src)); 
	}
}
