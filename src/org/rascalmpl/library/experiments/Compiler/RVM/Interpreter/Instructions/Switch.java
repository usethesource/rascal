package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;

public class Switch extends Instruction {
	IMap caseLabels;
	String caseDefault;
	boolean useConcreteFingerprint;
	
	public Switch(CodeBlock ins, IMap caseLabels, String caseDefault, boolean useConcreteFingerprint) {
		super(ins, Opcode.SWITCH);
		this.caseLabels = caseLabels;
		this.caseDefault = caseDefault;
		this.useConcreteFingerprint = useConcreteFingerprint;
	}

	public String toString() { 
		String res = "SWITCH (";
		String sep = "";
		for(IValue key : caseLabels){
			String label = ((IString)caseLabels.get(key)).getValue();
			res += sep + key + ": " + label;
			sep = ", ";
		}
		res += ", " + useConcreteFingerprint;
		res += "), " + caseDefault;
		return res;
	}
	
	public void generate(){
		IMapWriter w = codeblock.vf.mapWriter();
		for(IValue key : caseLabels){
			String label = ((IString)caseLabels.get(key)).getValue();
			w.put(key, codeblock.vf.integer(codeblock.getLabelPC(label)));
		}
		codeblock.addCode2(opcode.getOpcode(), 
							codeblock.getConstantIndex(w.done()), 
							codeblock.getLabelPC(caseDefault));
		codeblock.addCode(useConcreteFingerprint ? 1 : 0);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall(opcode.name());

		codeEmittor.emitInlineSwitch(caseLabels, caseDefault, useConcreteFingerprint) ;
	}
}
