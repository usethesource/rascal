package org.rascalmpl.interpreter.control_exceptions;

/**
 * Exception thrown to request a frame restart in the debugger.
 * This exception carries the target frame ID and propagates up the call stack
 * until it reaches the appropriate frame that should be restarted.
 */
public class RestartFrameException extends ControlException {
	private final static long serialVersionUID = 1L;
	private final int targetFrameId;
	
	public RestartFrameException(int targetFrameId) {
		super("Restart frame: " + targetFrameId);
		this.targetFrameId = targetFrameId;
	}
	
	public int getTargetFrameId() {
		return targetFrameId;
	}
}
