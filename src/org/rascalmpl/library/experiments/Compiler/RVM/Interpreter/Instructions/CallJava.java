package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CallJava extends Instruction {
	
	final int className;
	final int methodName;
	final int parameterTypes;
	final int keywordTypes;
	final int reflect;
	
	public CallJava(CodeBlock ins, int methodName, int className, int parameterTypes, int keywordTypes, int reflect) {
		super(ins, Opcode.CALLJAVA);
		this.className = className;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.keywordTypes = keywordTypes;
		this.reflect = reflect;
	}
	
	public String toString() { return "CALLJAVA " + codeblock.getConstantValue(methodName) + ", " 
												  + codeblock.getConstantValue(className) +  ", " 
												  +  codeblock.getConstantType(parameterTypes) + ", " 
												  +  codeblock.getConstantType(keywordTypes) + ", " 
												  + reflect; }
		
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(methodName);
		codeblock.addCode(className);
		codeblock.addCode(parameterTypes);
		codeblock.addCode(keywordTypes);
		codeblock.addCode(reflect);
	}

}
