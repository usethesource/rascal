package org.rascalmpl.library.experiments.CoreRascal.RVM;

public class Coroutine {
	
	final Frame start; // stack frame of the main coroutine function 
	Frame frame; // the current active stack frame of the coroutine
	
	boolean suspended = false;
	
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
		this.frame = current; // sets the current stack frame of the active co-routine
		this.suspended = true;
	}
	
	public boolean hasNext() {
		return suspended;
	}
	
}
