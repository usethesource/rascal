package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadVarKwp extends Instruction {

	final String fuid;
	final String name;

	public LoadVarKwp(CodeBlock ins, String fuid, String name) {
		super(ins, Opcode.LOADVARKWP);
		this.fuid = fuid;
		this.name = name;
	}

	public String toString() {
		return "LOADVARKWP " + fuid + ", " + name + " [" + codeblock.getFunctionIndex(fuid) + ", " + codeblock.getConstantIndex(codeblock.vf.string(name)) + "]";
	}

	public void generate(BytecodeGenerator codeEmittor, boolean dcode) {
		if (!dcode)
			codeEmittor.emitDebugCall(opcode.name());

		codeEmittor.emitCall("insnLOADVARKWP", codeblock.getFunctionIndex(fuid), codeblock.getConstantIndex(codeblock.vf.string(name)));

		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), codeblock.getConstantIndex(codeblock.vf.string(name)));
	}

}
