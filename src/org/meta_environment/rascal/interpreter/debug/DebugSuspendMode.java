package org.meta_environment.rascal.interpreter.debug;

public enum DebugSuspendMode {

	/**
	 * Indicates the debugger was suspended due
	 * to the completion of a step action.
	 */
	STEP_END,

	/**
	 * Indicates a thread was suspended by
	 * a breakpoint.
	 */
	BREAKPOINT,

	/**
	 * Indicates the debugger was suspended due
	 * to a client request.
	 */
	CLIENT_REQUEST
}
