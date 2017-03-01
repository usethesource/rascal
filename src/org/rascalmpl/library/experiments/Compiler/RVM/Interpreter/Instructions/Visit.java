package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import io.usethesource.vallang.IBool;

public class Visit extends Instruction {

	int direction;
	int progress;
	int fixedpoint;
	int rebuild;
	
	public Visit(CodeBlock ins, int direction, int progress, int fixedpoint, int rebuild){
		super(ins, Opcode.VISIT);
		this.codeblock = ins;
		this.direction = direction;
		this.progress = progress;
		this.fixedpoint = fixedpoint;
		this.rebuild = rebuild;
	}
	
	public String toString() { return "VISIT " 
									  + "bottomUp="   + codeblock.getConstantValue(direction) + ", " 
									  + "continuing=" + codeblock.getConstantValue(progress)  + ", " 
									  + "fixedpoint=" + codeblock.getConstantValue(fixedpoint)  + ", " 
									  + "rebuild=" 	  + codeblock.getConstantValue(rebuild) ; }
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(),  direction, progress);
		codeblock.addCode(fixedpoint);
		codeblock.addCode(rebuild);
		
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineVisit(((IBool) codeblock.getConstantValue(direction)).getValue(),
				((IBool) codeblock.getConstantValue(progress)).getValue(),
				((IBool) codeblock.getConstantValue(fixedpoint)).getValue(),
				((IBool) codeblock.getConstantValue(rebuild)).getValue());
	}
}
