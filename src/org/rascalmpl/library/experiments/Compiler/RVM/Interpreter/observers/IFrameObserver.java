package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug.BreakPointManager;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IList;

public interface IFrameObserver {
	
	default IList getData() { return ValueFactoryFactory.getValueFactory().list(); }
	
	default void startObserving() { }
	
	default void stopObserving() { }
	
	default void report() { }
	
    default void report(IList data) { }
	
    default void setRVM(RVMCore rvm) { }
	
	default RVMCore getRVM() { throw new RuntimeException("No access to RVM availabe"); }
	
    default boolean observe(Frame frame) { return true; }
	
    default boolean observeRVM(RVMCore rvm, Frame frame, int pc, Object[] stack, int sp, Object accu) { return true; }
	
    default boolean enter(Frame frame) { return true; }
	
    default boolean leave(Frame frame, Object rval) { return true; }
	
    default boolean exception(Frame frame, Thrown thrown) { throw thrown; }
    
    default IFrameObserver getObserverWhenActiveBreakpoints() { return null; }

    default BreakPointManager getBreakPointManager() { return null; }
}
