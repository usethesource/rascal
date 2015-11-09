package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

public class NullFrameObserver implements IFrameObserver {
	
	private static final NullFrameObserver instance = new NullFrameObserver();
	
	private NullFrameObserver() { }
	
	public static NullFrameObserver getInstance() {
		return instance;
	}

}
