/*******************************************************************************
 * Copyright (c) 2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI  
*******************************************************************************/
package org.rascalmpl.interpreter.debug;

import org.rascalmpl.interpreter.AbstractInterpreterEventTrigger;

public interface IDebugHandler extends IDebugSupport,
		IRascalSuspendTriggerListener {

	AbstractInterpreterEventTrigger getEventTrigger();
	
}
