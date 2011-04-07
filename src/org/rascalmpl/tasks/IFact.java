/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anya Helene Bagge - A.H.S.Bagge@cwi.nl (Univ. Bergen)
*******************************************************************************/
package org.rascalmpl.tasks;

import java.util.Collection;

import org.rascalmpl.tasks.IDependencyListener;
import org.rascalmpl.tasks.IFact;

public interface IFact<V> {
	

	/**
	 *  Returns true if a fact's valid is valid.
	 * 
	 *  Facts can become invalid if the invalidate() method is called, or if the the
	 *  value is weakly referenced and disappears from memory.
	 */
	public abstract boolean isValid();
	
	/**
	 *  Return the value of this fact.
	 *  
	 *  Returns null iff !isValid()
	 */
	public abstract V getValue();
	
	/**
	 *  Set the value to 'val'.
	 *  
	 *  If the fact supports dependency tracking, this will cause dependents to be notified
	 *  (if the value has changed).
	 *  
	 * @param val The new value
	 * @return true if the value was changed
	 */
	public abstract boolean setValue(V val);

	/**
	 * Removing a fact means removing it from the dependency chain, and
	 * disposing of the data. This is done when the fact is no longer useful
	 * (for example, if the underlying entity has been removed).
	 */
	public abstract void remove();
	
	public abstract void registerListener(IDependencyListener listener);
	
	public abstract void unregisterListener(IDependencyListener listener);
	
	public abstract void setDepends(Collection<IFact<V>> deps);

	/**
	 * @param fact
	 * @return True if the fact was actually changed
	 */
	public abstract boolean updateFrom(IFact<V> fact);
	public abstract Object getKey();

}