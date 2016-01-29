package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;

public class JmpIndexed extends Instruction {

	IList labels;

	public JmpIndexed(CodeBlock ins, IList labels){
		super(ins, Opcode.JMPINDEXED);
		this.labels = labels;
	}
	
	public String toString() { 
		String res = "JMPINDEXED ";
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
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstantIndex(w.done()));
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());

		IListWriter w = codeblock.vf.listWriter();
		for(IValue vlabel : labels){
			String label = ((IString) vlabel).getValue();
			w.append(codeblock.vf.integer(codeblock.getLabelPC(label)));
		}
		
		codeEmittor.emitInlineJmpIndexed(labels , debug);
	}
}
