package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CheckArgTypeAndCopy extends Instruction {
	
	final int pos1;
	final int type;
	final int pos2;
	
	public CheckArgTypeAndCopy(CodeBlock ins, int pos1, int type, int pos2) {
		super(ins, Opcode.CHECKARGTYPEANDCOPY);
		this.pos1 = pos1;
		this.type = type;
		this.pos2 = pos2;
	}
	
	public String toString() { return "CHECKARGTYPEANDCOPY " + pos1 + ", " + type + "[" + codeblock.getConstantType(type) + "], " + pos2; }
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), pos1, type);
		codeblock.addCode(pos2);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) {
			codeEmittor.emitDebugCall2(opcode.name(), Integer.toString(pos1), type);
		}
		
		// TODO: determine which version is better
		//codeEmittor.emitCallWithArgsSSFIII_A("insnCHECKARGTYPEANDCOPY", pos1, type, pos2);
		codeEmittor.emitInlineCheckArgTypeAndCopy(pos1,type,pos2) ;
	}
}
