package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class Coroutine {
	
	final Frame start; // Stack frame of the main coroutine function 
	public Frame frame;       // Current active stack frame of the coroutine
	
	public boolean suspended = false;
	public boolean isInitialized = false;
	public Frame entryFrame;
	
	public Coroutine(Frame frame) {
		this.start = frame;
		this.frame = frame;
	}
	
	public void next(Frame previousCallFrame) {
		this.suspended = false;
		this.start.previousCallFrame = previousCallFrame;
	}
	
	public void suspend(Frame current) {
		this.start.previousCallFrame = null;
		this.frame = current; // Sets the current stack frame of the active co-routine
		this.suspended = true;
	}
	
	public boolean isInitialized() {
		return this.isInitialized;
	}
	
	public boolean hasNext() {
		return suspended;
	}
	
	public Coroutine copy() {
		if(suspended || start.pc != 0) {
			throw new InternalCompilerError("copying suspended or active coroutine is not allowed.", frame);
		}
		return new Coroutine(start.copy());
	}
	
}
