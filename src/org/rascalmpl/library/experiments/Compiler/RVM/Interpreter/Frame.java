package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class Frame {
	int scopeId;
    Frame previousCallFrame;
    final Frame previousScope;
	final Object[] stack;
	int sp;
	int pc;
	final Function function;
	
	final boolean isCoroutine;
		
	public Frame(int scopeId, Frame previousCallFrame, int stackSize, Function function){
		this(scopeId, previousCallFrame, previousCallFrame, stackSize, function);
	}
	
	public Frame(int scopeId, Frame previousCallFrame, Frame previousScope, int stackSize, Function function){
		this(scopeId, previousCallFrame, previousScope, function, new Object[stackSize]);
	}
	
	private Frame(int scopeId, Frame previousCallFrame, Frame previousScope, Function function, final Object[] stack) {
		this.scopeId = scopeId;
		this.previousCallFrame = previousCallFrame;
		this.previousScope = previousScope;
		this.stack = stack;
		this.pc = 0;
		this.sp = 0;
		this.function = function;
		this.isCoroutine = function.isCoroutine;
	}
	
	public Frame copy() {
		if(pc != 0)
			throw new RuntimeException("Copying the frame with certain instructions having been already executed.");
		Frame newFrame = new Frame(scopeId, previousCallFrame, previousScope, function, stack.clone());
		newFrame.sp = sp; 
		return newFrame;
	}
	
	
}
