package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushVarRef extends Instruction {

	final String fuid;
	final int pos;

	public PushVarRef(CodeBlock ins, String fuid, int pos) {
		super(ins, Opcode.PUSHVARREF);
		this.fuid = fuid;
		this.pos = pos;
	}

	public String toString() {
		return "PUSHVARREF " + fuid + " [ " + codeblock.getFunctionIndex(fuid) + " ] " + ", " + pos;
	}

	public void generate() {
		int what = (pos == -1) ? codeblock.getConstantIndex(codeblock.vf.string(fuid)) : codeblock.getFunctionIndex(fuid) ;
		codeblock.addCode2(opcode.getOpcode(), what, pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		int what = (pos == -1) ? codeblock.getConstantIndex(codeblock.vf.string(fuid)) : codeblock.getFunctionIndex(fuid) ;
		
		if ( debug ) 
			codeEmittor.emitDebugCall2(opcode.name(), (pos == -1) ? fuid : codeblock.getFunctionName(fuid), pos);

		if (pos == -1) {
			codeEmittor.emitCallWithArgsSSFI_S("PUSHVARREFMODULE", what);
		} else {
			codeEmittor.emitCallWithArgsSSFII_S("PUSHVARREFSCOPED", what, pos);
		}
	}
}
