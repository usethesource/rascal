package org.meta_environment.rascal.interpreter;

//TODO: need to use an other way to identify a location that does not depend of eclipse
import org.eclipse.imp.pdb.facts.ISourceLocation;

public interface IDebugger {
	
	public boolean isStepping();

	public void notifySuspend();
	
	public boolean hasEnabledBreakpoint(ISourceLocation sourceLocation);

}
