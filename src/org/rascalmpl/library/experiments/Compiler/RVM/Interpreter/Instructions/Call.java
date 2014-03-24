package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Call extends Instruction {

	final String fuid;
	final int arity;

	public Call(CodeBlock ins, String fuid, int arity) {
		super(ins, Opcode.CALL);
		this.fuid = fuid;
		this.arity = arity;
	}

	public String toString() {
		return "CALL " + fuid + ", " + arity + " [ " + codeblock.getFunctionIndex(fuid) + " ]";
	}

	public void generate(Generator codeEmittor, boolean dcode) {
		// TODO this is wrong !!!
		// Call function directly needs name demangling, and stack creation
		if (dcode)
			codeEmittor.emitCall("dinsnCALL", codeblock.getFunctionIndex(fuid));

		codeEmittor.emitCall("insnCALL", codeblock.getFunctionIndex(fuid), arity);
		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), arity);
	}

}
