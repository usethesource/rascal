/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*******************************************************************************/
package org.rascalmpl.value.impl.util.sharing;

/**
 * Indicates that the implementing object is shareable.
 * 
 * @author Arnold Lankamp
 */
public interface IShareable{
	
	/**
	 * Computes the hashcode for 'this' object.
	 * 
	 * @return The hashcode
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	int hashCode();
	
	/**
	 * Checks if the given object is 'truely' equal to 'this' object.
	 * 
	 * @param shareable
	 *            The object to compare with.
	 * @return True if the given object is equal to 'this' object; false otherwise.
	 */
	boolean equivalent(IShareable shareable);
}
