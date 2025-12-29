/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
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
