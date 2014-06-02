package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadLocDeref extends Instruction {

	int pos;
	
	public LoadLocDeref(CodeBlock ins, int pos) {
		super(ins, Opcode.LOADLOCDEREF);
		this.pos = pos;
	}
	
	public String toString() { return "LOADLOCDEREF " + pos; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADLOCDEREF", pos);
		
		codeblock.addCode1(opcode.getOpcode(), pos);
	}
}
