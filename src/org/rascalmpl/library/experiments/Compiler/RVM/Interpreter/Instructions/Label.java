package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Label extends Instruction {

	String label;
	
	public Label(CodeBlock ins, String label){
		super(ins, Opcode.LABEL);
		this.label = label;
		ins.defLabel(label, this);
	}
	
	public String toString() { return "LABEL " + label + " [" +  "]"; }
	
	public void generate(){
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		codeEmittor.emitLabel(label);
	}
}
