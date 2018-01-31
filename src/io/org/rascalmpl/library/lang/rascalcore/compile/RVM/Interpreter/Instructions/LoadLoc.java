package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadLoc extends Instruction {

	int pos;
	
	public LoadLoc(CodeBlock ins, int pos){
		super(ins, Opcode.LOADLOC);
		this.pos = pos;
	}
	
	public String toString() { return "LOADLOC " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), pos);

		codeEmittor.emitInlineLoadLocN(pos);
	}
}
