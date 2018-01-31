package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushNestedFun extends Instruction {

	final String fuid;
	final String scopeIn;
	
	public PushNestedFun(CodeBlock ins, String fuid, String scopeIn) {
		super(ins, Opcode.PUSH_NESTED_FUN);
		this.fuid = fuid;
		this.scopeIn = scopeIn;
	}
	
	public String toString() { return "PUSH_NESTED_FUN " + fuid + ", " + scopeIn + " [ " + codeblock.getFunctionIndex(fuid) + ", " + codeblock.getFunctionIndex(scopeIn) + " ]"; }
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), codeblock.getFunctionIndex(scopeIn));
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall2(opcode.name(), codeblock.getFunctionName(fuid), codeblock.getFunctionIndex(scopeIn));
		
		codeEmittor.emitCallWithArgsSSFII_S("insnPUSH_NESTED_FUN", codeblock.getFunctionIndex(fuid), codeblock.getFunctionIndex(scopeIn));
	}
}
