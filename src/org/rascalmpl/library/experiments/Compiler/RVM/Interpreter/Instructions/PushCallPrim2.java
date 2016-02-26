package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;
import org.rascalmpl.value.ISourceLocation;

public class PushCallPrim2 extends Instruction {

	RascalPrimitive prim;
	ISourceLocation src;
	
	public PushCallPrim2(CodeBlock ins, RascalPrimitive prim, ISourceLocation src){
		super(ins, Opcode.PUSHCALLPRIM2);
		this.prim = prim;
		this.src = src;
	}
	
	public String toString() { return "PUSHCALLPRIM2 " + prim + ", " + src; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), prim.ordinal());
		codeblock.addCode(codeblock.getConstantIndex(src));
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), prim.ordinal());

		codeEmittor.emitInlinePushCallPrim2(prim, debug); 
	}
}
