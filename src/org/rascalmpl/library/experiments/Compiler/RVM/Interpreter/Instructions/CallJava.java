package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CallJava extends Instruction {
	
	final int className;
	final int methodName;
	final int parameterTypes;
	
	public CallJava(CodeBlock ins, int methodName, int className, int i) {
		super(ins, Opcode.CALLJAVA);
		this.className = className;
		this.methodName = methodName;
		this.parameterTypes = i;
	}
	
	public String toString() { return "CALLJAVA " + codeblock.getConstantValue(methodName) + ", " + codeblock.getConstantValue(className) +
			                                      ", " +  codeblock.getConstantValue(parameterTypes); }
		
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(methodName);
		codeblock.addCode(className);
		codeblock.addCode(parameterTypes);
	}

}
