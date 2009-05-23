package org.meta_environment.rascal.interpreter;

public interface IDebugger {
	
	public boolean isStepping();

	public void notifySuspend();

}
