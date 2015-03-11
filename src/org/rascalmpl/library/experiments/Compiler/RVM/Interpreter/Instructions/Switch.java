package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Switch extends Instruction {
	IMap caseLabels;
	String caseDefault;
	boolean concretePatterns;
	
	public Switch(CodeBlock ins, IMap caseLabels, String caseDefault, boolean concretePatterns) {
		super(ins, Opcode.SWITCH);
		this.caseLabels = caseLabels;
		this.caseDefault = caseDefault;
		this.concretePatterns = concretePatterns;
	}

	public String toString() { 
		String res = "SWITCH (";
		String sep = "";
		for(IValue key : caseLabels){
			String label = ((IString)caseLabels.get(key)).getValue();
			res += sep + key + ": " + label;
			sep = ", ";
		}
		res += ", " + concretePatterns;
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
		codeblock.addCode(concretePatterns ? 1 : 0);
	}
}
