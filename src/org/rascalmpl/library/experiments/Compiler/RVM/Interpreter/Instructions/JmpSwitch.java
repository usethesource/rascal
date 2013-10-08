package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class JmpSwitch extends Instruction {

	IList labels;

	public JmpSwitch(CodeBlock ins, IList labels){
		super(ins, Opcode.JMPSWITCH);
		this.labels = labels;
	}
	
	public String toString() { 
		String res = "JMPSWITCH ";
		String sep = "";
		for(IValue vlabel : labels){
			String label = ((IString) vlabel).getValue();
			res += sep + label;
			sep = ", ";
		}
		return res;
	}
	
	public void generate(){
		IListWriter w = codeblock.vf.listWriter();
		for(IValue vlabel : labels){
			String label = ((IString) vlabel).getValue();
			w.append(codeblock.vf.integer(codeblock.getLabelPC(label)));
		}
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getConstantIndex(w.done()));
	}
}
