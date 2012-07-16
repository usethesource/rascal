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

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.interpreter.control_exceptions.QuitException;

public interface IDebugger {

	public boolean isTerminated();

	public void notifySuspend(DebugSuspendMode mode) throws QuitException;

	public void notifyResume(DebugResumeMode mode);
	
	public boolean hasEnabledBreakpoint(ISourceLocation sourceLocation);
	
	public boolean isStepping();

	public void stopStepping();

	public void destroy();

}
