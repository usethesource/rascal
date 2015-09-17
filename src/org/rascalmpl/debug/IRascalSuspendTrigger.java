/*******************************************************************************
 * Copyright (c) 2012-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI  
*******************************************************************************/
package org.rascalmpl.debug;

public interface IRascalSuspendTrigger {
	
	public void addSuspendTriggerListener(IRascalSuspendTriggerListener listener);
	
	public void removeSuspendTriggerListener(IRascalSuspendTriggerListener listener);
		
}
