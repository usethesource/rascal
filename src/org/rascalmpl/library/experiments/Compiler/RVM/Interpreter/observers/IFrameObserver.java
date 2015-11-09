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
	
	default void observe(Frame frame) { }
	
	default void observeRVM(RVM rvm, Frame frame, int pc, Object[] stack, int sp) { }
	
	default void enter(Frame frame) { }
	
	default void leave(Frame frame, Object rval) { }
	
}
