package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class ValueSubtype extends Instruction {
	
	final int type;
	
	public ValueSubtype(CodeBlock ins, int type) {
		super(ins, Opcode.VALUESUBTYPE);
		this.type = type;
	}
	
	public String toString() { return "VALUESUBTYPE " + type + "[" + codeblock.getConstantType(type) + "]"; }
	
	public void generate(){
		codeblock.addCode(type);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
//		if ( debug ) {
//			codeEmittor.emitDebugCall(opcode.name());
//			codeEmittor.emitCallWithArgsSSFIII("insnCHECKARGTYPEANDCOPY",pos1,type,pos2,debug);
//		}
		codeEmittor.emitInlineValueSubtype(type, debug) ;
	}
}
