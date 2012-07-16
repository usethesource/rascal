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

import org.rascalmpl.interpreter.IInterpreterEventListener;

public interface IDebugSupport {

	public void addInterpreterEventListener(IInterpreterEventListener listener);

	public void removeInterpreterEventListener(IInterpreterEventListener listener);

	/**
	 * Message exchange channel between the debugger and the runtime.
	 * 
	 * @param message the message to be transmitted
	 */
	public void processMessage(IDebugMessage message);

}
