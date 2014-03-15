package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class CallJava extends Instruction {
	
	final int className;
	final int methodName;
	final int parameterTypes;
	final int reflect;
	
	public CallJava(CodeBlock ins, int methodName, int className, int i, int reflect) {
		super(ins, Opcode.CALLJAVA);
		this.className = className;
		this.methodName = methodName;
		this.parameterTypes = i;
		this.reflect = reflect;
	}
	
	public String toString() { return "CALLJAVA " + codeblock.getConstantValue(methodName) + ", " + codeblock.getConstantValue(className) +
			                                      ", " +  codeblock.getConstantType(parameterTypes) + ", " + reflect; }
		
	public void generate(Generator codeEmittor){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(methodName);
		codeblock.addCode(className);
		codeblock.addCode(parameterTypes);
		codeblock.addCode(reflect);
	}

}
