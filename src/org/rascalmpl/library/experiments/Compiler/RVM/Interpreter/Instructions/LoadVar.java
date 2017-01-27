package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadVar extends Instruction {

	final int pos;
	final String fuid;

	public LoadVar(CodeBlock ins, String fuid, int pos) {
		super(ins, Opcode.LOADVAR);
		this.fuid = fuid;
		this.pos = pos;
	}

	public String toString() {
		return "LOADVAR " + fuid + ", " + pos;
	}

	public void generate() {
		int what = (pos == -1) ? codeblock.getConstantIndex(codeblock.vf.string(fuid)) : codeblock.getFunctionIndex(fuid);
		codeblock.addCode2(opcode.getOpcode(), what, pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		
		if (pos == -1) {
			if (debug)
				codeEmittor.emitDebugCall2(opcode.name(), fuid, pos);
			
			codeEmittor.emitCallWithArgsFI_A("LOADVARMODULE", codeblock.getConstantIndex(codeblock.vf.string(fuid)));
		} else {
			if (debug)
				codeEmittor.emitDebugCall2(opcode.name(), codeblock.getFunctionName(fuid), pos);
			
			codeEmittor.emitCallWithArgsFII_A("LOADVARSCOPED", codeblock.getFunctionIndex(fuid), pos);
		}

	}
}
