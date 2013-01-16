/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

public interface IBooleanResult {
	public void init();
	
	/**
	 * @returns true iff next will return true, but will not actually advance the iterator or have any side-effects
	 */
	public boolean hasNext();
	
	/**
	 * @return true iff the current boolean value returns true, and advances the iterator to a next assignment
	 */
	public boolean next();
}
