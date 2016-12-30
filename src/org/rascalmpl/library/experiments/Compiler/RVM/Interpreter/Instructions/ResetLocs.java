package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class ResetLocs extends Instruction {

	int positions;
	
	public ResetLocs(CodeBlock ins, int positions){
		super(ins, Opcode.RESETLOCS);
		this.positions = positions;
	}
	
	public String toString() { return "RESETLOCS " + codeblock.getConstantValue(positions); }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), positions);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall1(opcode.name(), positions);
		
		codeEmittor.emitInlineResetLocs(codeblock.getConstantValue(positions)) ;
	}	
}
