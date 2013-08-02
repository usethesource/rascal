package org.rascalmpl.library.experiments.CoreRascal.RVM;

import org.eclipse.imp.pdb.facts.IValue;

public class Frame {
	final int scope;
    final Frame previous;
	final IValue[] stack;
	int sp;
	int pc;
	final Function function;
	
	Frame(int scope, Frame previous,  int stackSize, Function function){
		this.scope = scope;
		this.previous = previous;
		this.stack = new IValue[stackSize];
		this.pc = 0;
		this.sp = 0;
		this.function = function;
	}
}
