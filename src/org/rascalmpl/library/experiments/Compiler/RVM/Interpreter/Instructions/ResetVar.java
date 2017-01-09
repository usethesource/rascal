package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class ResetVar extends Instruction {

	final int pos;
	final String fuid;

	public ResetVar(CodeBlock ins, String fuid, int pos) {
		super(ins, Opcode.RESETVAR);
		this.fuid = fuid;
		this.pos = pos;
	}

	public String toString() {
		return "RESETVAR " + fuid + ", " + pos;
	}

	public void generate() {
		int what = (pos == -1) ? codeblock.getConstantIndex(codeblock.vf.string(fuid)) : codeblock.getFunctionIndex(fuid);
		codeblock.addCode2(opcode.getOpcode(), what, pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		int what = (pos == -1) ? codeblock.getConstantIndex(codeblock.vf.string(fuid)) : codeblock.getFunctionIndex(fuid);
		
		if (debug)
			codeEmittor.emitDebugCall2(opcode.name(), (pos == -1) ? fuid : codeblock.getFunctionName(fuid), pos);
		
		codeEmittor.emitVoidCallWithArgsFII("RESETVAR", what, pos);
	}
}
