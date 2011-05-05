/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
*******************************************************************************/
package org.rascalmpl.interpreter.debug;

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
