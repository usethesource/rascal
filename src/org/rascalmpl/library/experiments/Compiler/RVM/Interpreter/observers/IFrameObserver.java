package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.value.IList;
import org.rascalmpl.values.ValueFactoryFactory;

public interface IFrameObserver {
	
	default IList getData() { return ValueFactoryFactory.getValueFactory().list(); }
	
	default void startObserving() { }
	
	default void stopObserving() { }
	
	default void report() { }
	
	default void report(IList data) { }
	
	default boolean observe(Frame frame) { return true; }
	
	default boolean observeRVM(RVM rvm, Frame frame, int pc, Object[] stack, int sp) { return true; }
	
	default boolean enter(Frame frame) { return true; }
	
	default boolean leave(Frame frame, Object rval) { return true; }
	
}
