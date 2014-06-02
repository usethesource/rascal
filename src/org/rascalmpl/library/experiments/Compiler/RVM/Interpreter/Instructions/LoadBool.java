package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadBool extends Instruction {
	
	final boolean bool;
	
	public LoadBool(CodeBlock ins, boolean bool) {
		super(ins, Opcode.LOADBOOL);
		this.bool = bool;
	}
	
	public String toString() { return "LOADBOOL " + bool; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		//codeEmittor.emitCall("insnLOADBOOL", bool ? 1 : 0);
		
		if ( bool )
			codeEmittor.emitCall("insnLOADBOOLTRUE");	
		else 
			codeEmittor.emitCall("insnLOADBOOLFALSE");
		
		codeblock.addCode1(opcode.getOpcode(), bool ? 1 : 0);
	}
}
