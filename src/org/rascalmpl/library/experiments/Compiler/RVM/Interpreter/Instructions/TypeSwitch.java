package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class TypeSwitch extends Instruction {

	IList labels;

	public TypeSwitch(CodeBlock ins, IList labels){
		super(ins, Opcode.TYPESWITCH);
		this.labels = labels;
	}
	
	public String toString() { 
		String res = "TYPESWITCH ";
		String sep = "";
		for(IValue vlabel : labels){
			String label = ((IString) vlabel).getValue();
			res += sep + label;
			sep = ", ";
		}
		return res;
	}
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		IListWriter w = codeblock.vf.listWriter();
		for(IValue vlabel : labels){
			String label = ((IString) vlabel).getValue();
			w.append(codeblock.vf.integer(codeblock.getLabelPC(label)));
		}
		codeEmittor.emitInlineTypeSwitch(labels,dcode) ;
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstantIndex(w.done()));
	}
}
