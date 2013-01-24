/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
*******************************************************************************/
package org.rascalmpl.tasks;

import java.util.Collection;

public interface IFact<V> extends IDependencyListener {
	

	public static final int FACT_OK = 0;
	public static final int FACT_DEPS_INVALID = 1;
	public static final int FACT_DEPS_CHANGED = 2;
	public static final int FACT_ERROR = 3;

	/**
	 *  Returns true if a fact's valid is valid.
	 * 
	 *  Facts can become invalid if the invalidate() method is called, or if the the
	 *  value is weakly referenced and disappears from memory.
	 */
	boolean isValid();
	
	/**
	 *  Return the value of this fact.
	 *  
	 *  Returns null iff !isValid()
	 */
	V getValue();
	
	/**
	 *  Set the value to 'val'.
	 *  
	 *  If the fact supports dependency tracking, this will cause dependents to be notified
	 *  (if the value has changed).
	 *  
	 * @param val The new value
	 * @return true if the value was changed
	 */
	boolean setValue(V val);

	/**
	 * Removing a fact means removing it from the dependency chain, and
	 * disposing of the data. This is done when the fact is no longer useful
	 * (for example, if the underlying entity has been removed).
	 */
	void remove();
	
	void registerListener(IDependencyListener listener);
	
	void unregisterListener(IDependencyListener listener);
	
	Collection<IDependencyListener> getListeners();
	
	void setDepends(Collection<IFact<V>> deps);

	Collection<IFact<?>> getDepends();

	/**
	 * @param fact
	 * @return True if the fact was actually changed
	 */
	boolean updateFrom(IFact<V> fact);
	Object getKey();

	int getStatus();

}
