/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Arnold Lankamp - interfaces and implementation
 *******************************************************************************/
package org.rascalmpl.value.impl.primitive;

import java.math.BigInteger;

/**
 * Strangely named class, which defines that the implementor can convert something to a big integer.
 * 
 * @author Arnold Lankamp
 */
public interface ICanBecomeABigInteger{
	
	/**
	 * Returns the big integer.
	 * 
	 * @return The big integer.
	 */
	BigInteger toBigInteger();
}
