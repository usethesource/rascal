package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadVarRef extends Instruction {

	final String fuid;
	final int pos;

	public LoadVarRef(CodeBlock ins, String fuid, int pos) {
		super(ins, Opcode.LOADVARREF);
		this.fuid = fuid;
		this.pos = pos;
	}

	public String toString() {
		return "LOADVARREF " + fuid + " [ " + codeblock.getFunctionIndex(fuid) + " ] " + ", " + pos;
	}

	public void generate() {
		int what = (pos == -1) ? codeblock.getConstantIndex(codeblock.vf.string(fuid)) : codeblock.getFunctionIndex(fuid) ;
		codeblock.addCode2(opcode.getOpcode(), what, pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		int what = (pos == -1) ? codeblock.getConstantIndex(codeblock.vf.string(fuid)) : codeblock.getFunctionIndex(fuid) ;
		
		if (pos == -1) {
			if ( debug ) 
				codeEmittor.emitDebugCall2(opcode.name(), fuid, pos);
			
			codeEmittor.emitCallWithArgsFI_A("LOADVARREFMODULE", what);
		} else {
			if ( debug ) 
				codeEmittor.emitDebugCall2(opcode.name(), codeblock.getFunctionName(fuid), pos);
			
			codeEmittor.emitCallWithArgsFII_A("LOADVARREFSCOPED", what, pos);
		}
	}
}
