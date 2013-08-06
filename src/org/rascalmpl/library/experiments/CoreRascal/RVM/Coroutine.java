package org.rascalmpl.library.experiments.CoreRascal.RVM;

public class Coroutine {
	
	final Frame init; // stack frame of the main coroutine function 
	Frame frame; // the current active stack frame of the coroutine
	
	public Coroutine(Frame frame) {
		this.init = frame;
		this.frame = frame;
	}
	
	public void resume(Frame previousCallFrame) {
		this.init.previousCallFrame = previousCallFrame;
	}
	
	public void suspend(Frame current) {
		this.init.previousCallFrame = null;
		this.frame = current; // sets the current stack frame of the active co-routine
	}
	
}
