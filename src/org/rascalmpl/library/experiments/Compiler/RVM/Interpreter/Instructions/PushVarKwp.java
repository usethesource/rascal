package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushVarKwp extends Instruction {

	final String fuid;
	final String name;

	public PushVarKwp(CodeBlock ins, String fuid, String name) {
		super(ins, Opcode.PUSHVARKWP);
		this.fuid = fuid;
		this.name = name;
	}

    public String toString() { return "PUSHVARKWP " + fuid + ", " + name + " [" + codeblock.getFunctionIndex(fuid) + ", " + codeblock.getConstantIndex(codeblock.vf.string(name)) + "]"; }

	public void generate() {
        codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), codeblock.getConstantIndex(codeblock.vf.string(name)));
    }

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if (debug)
			codeEmittor.emitDebugCall2(opcode.name(), fuid, codeblock.getConstantIndex(codeblock.vf.string(name)));
		
		codeEmittor.emitCallWithArgsSSFII_S("PUSHVARKWP", codeblock.getFunctionIndex(fuid), codeblock.getConstantIndex(codeblock.vf.string(name)));
    }
}
