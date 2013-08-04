package org.rascalmpl.library.experiments.CoreRascal.RVM;

public class Frame {
	final int scope;
    final Frame previous;
	final Object[] stack;
	int sp;
	int pc;
	final Function function;
	
	Frame(int scope, Frame previous,  int stackSize, Function function){
		this.scope = scope;
		this.previous = previous;
		this.stack = new Object[stackSize];
		this.pc = 0;
		this.sp = 0;
		this.function = function;
	}
}
