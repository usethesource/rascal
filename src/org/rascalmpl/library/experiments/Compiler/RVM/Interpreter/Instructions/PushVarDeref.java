package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushVarDeref extends Instruction {

	final String fuid;
	final int pos;

	public PushVarDeref(CodeBlock ins, String fuid, int pos) {
		super(ins, Opcode.PUSHVARDEREF);
		this.fuid = fuid;
		this.pos = pos;
	}

	public String toString() {
		return "PUSHVARDEREF " + fuid + " [ " + codeblock.getFunctionIndex(fuid) + " ] " + ", " + pos;
	}

	public void generate() {
		int what = (pos == -1) ? codeblock.getConstantIndex(codeblock.vf.string(fuid)) : codeblock.getFunctionIndex(fuid);
		codeblock.addCode2(opcode.getOpcode(), what, pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		int what = (pos == -1) ? codeblock.getConstantIndex(codeblock.vf.string(fuid)) : codeblock.getFunctionIndex(fuid);
		
		if (debug)
			codeEmittor.emitDebugCall2(opcode.name(), (pos == -1) ? fuid : codeblock.getFunctionName(fuid), pos);

		codeEmittor.emitCallWithArgsSSFII_S("PUSHVARDEREF", what, pos);
	}
}
