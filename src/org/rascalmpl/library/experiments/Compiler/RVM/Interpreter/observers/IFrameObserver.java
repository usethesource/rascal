package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.value.IList;
import org.rascalmpl.values.ValueFactoryFactory;

public interface IFrameObserver {
	
	default IList getData() { return ValueFactoryFactory.getValueFactory().list(); }
	
	default void startObserving() { }
	
	default void stopObserving() { }
	
	default void report() { }
	
	default void report(IList data) { }
	
	default void setRVM(RVM rvm) { }
	
	default RVM getRVM() { throw new RuntimeException("No access to RVM availabe"); }
	
	default boolean observe(Frame frame) { return true; }
	
	default boolean observeRVM(RVM rvm, Frame frame, int pc, Object[] stack, int sp, Object accu) { return true; }
	
	default boolean enter(Frame frame) { return true; }
	
	default boolean leave(Frame frame, Object rval) { return true; }
	
	default boolean exception(Frame frame, Thrown thrown) { return false; }
}
