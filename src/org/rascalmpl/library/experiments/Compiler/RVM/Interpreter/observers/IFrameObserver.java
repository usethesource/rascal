package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import io.usethesource.vallang.IList;
import org.rascalmpl.values.ValueFactoryFactory;

public interface IFrameObserver {
	
	default IList getData() { return ValueFactoryFactory.getValueFactory().list(); }
	
	default void startObserving() { }
	
	default void stopObserving() { }
	
	default void report() { }
	
	@SuppressWarnings("unused")
    default void report(IList data) { }
	
	@SuppressWarnings("unused")
    default void setRVM(RVMCore rvm) { }
	
	default RVMCore getRVM() { throw new RuntimeException("No access to RVM availabe"); }
	
	@SuppressWarnings("unused")
    default boolean observe(Frame frame) { return true; }
	
	@SuppressWarnings("unused")
    default boolean observeRVM(RVMCore rvm, Frame frame, int pc, Object[] stack, int sp, Object accu) { return true; }
	
	@SuppressWarnings("unused")
    default boolean enter(Frame frame) { return true; }
	
	@SuppressWarnings("unused")
    default boolean leave(Frame frame, Object rval) { return true; }
	
	@SuppressWarnings("unused")
    default boolean exception(Frame frame, Thrown thrown) { throw thrown; }
}
