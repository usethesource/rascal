package org.meta_environment.rascal.interpreter;

//TODO: need to use an other way to identify a location that does not depend of eclipse
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.meta_environment.rascal.interpreter.control_exceptions.QuitException;

public interface IDebugger {

	public boolean isTerminated();

	public void notifySuspend(DebugSuspendMode mode) throws QuitException;
	
	public boolean hasEnabledBreakpoint(ISourceLocation sourceLocation);
	
	public boolean isStepping();

	public void stopStepping();

	public void destroy();

}
