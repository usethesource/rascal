package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadNestedFun extends Instruction {

	final String fuid;
	final String scopeIn;
	
	public LoadNestedFun(CodeBlock ins, String fuid, String scopeIn) {
		super(ins, Opcode.LOAD_NESTED_FUN);
		this.fuid = fuid;
		this.scopeIn = scopeIn;
	}
	
	public String toString() { return "LOAD_NESTED_FUN " + fuid + ", " + scopeIn + " [ " + codeblock.getFunctionIndex(fuid) + ", " + codeblock.getFunctionIndex(scopeIn) + " ]"; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		codeEmittor.emitCallWithArgsSSFII("insnLOAD_NESTED_FUN", codeblock.getFunctionIndex(fuid), codeblock.getFunctionIndex(scopeIn),dcode);
		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), codeblock.getFunctionIndex(scopeIn));
	}
}
