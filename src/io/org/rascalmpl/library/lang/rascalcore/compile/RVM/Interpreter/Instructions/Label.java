package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Label extends Instruction {

	String label;
	
	public Label(CodeBlock ins, String label){
		super(ins, Opcode.LABEL);
		this.label = label;
		ins.defLabel(label, this);
	}
	
	public String toString() { return label; }
	
	public void generate(){
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitLabel(label);
	}
}
