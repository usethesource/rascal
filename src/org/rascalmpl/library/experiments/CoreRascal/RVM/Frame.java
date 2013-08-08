package org.rascalmpl.library.experiments.CoreRascal.RVM;

public class Frame {
	int scopeId;
    Frame previousCallFrame;
    final Frame previousScope;
	final Object[] stack;
	int sp;
	int pc;
	final Function function;
		
	Frame(int scopeId, Frame previousCallFrame, int stackSize, Function function){
		this(scopeId, previousCallFrame, previousCallFrame, stackSize, function);
	}
	
	Frame(int scopeId, Frame previousCallFrame, Frame previousScope, int stackSize, Function function){
		this.scopeId = scopeId;
		this.previousCallFrame = previousCallFrame;
		this.previousScope = previousScope;
		this.stack = new Object[stackSize];
		this.pc = 0;
		this.sp = 0;
		this.function = function;
	}
	
}
