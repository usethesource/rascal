/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.debug;

public interface IDebugger {

	/**
	 * Notification channel to inform the debugger about events in that happened
	 * in the runtime.
	 * 
	 * @param message containing notification details
	 */
	public void sendMessage(IDebugMessage message);
	
	@Deprecated
	public void notifyResume(DebugResumeMode mode);
	
	@Deprecated
	public boolean isStepping();

	@Deprecated
	public void stopStepping();

	@Deprecated
	public void destroy();

}
