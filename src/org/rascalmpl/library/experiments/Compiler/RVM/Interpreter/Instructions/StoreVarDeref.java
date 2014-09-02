package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class StoreVarDeref extends Instruction {
	
	final String fuid;
	final int pos;
	
	public StoreVarDeref(CodeBlock ins, String fuid, int pos) {
		super(ins, Opcode.STOREVARDEREF);
		this.fuid = fuid;
		this.pos = pos;
	}

	public String toString() { return "STOREVARDEREF " + fuid + " [ " + codeblock.getFunctionIndex(fuid) + " ] " + ", " + pos; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){

		codeEmittor.emitCallWithArgsSSFII("insnSTOREVARDEREF", codeblock.getFunctionIndex(fuid), pos,dcode);

		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), pos);
	}
}
