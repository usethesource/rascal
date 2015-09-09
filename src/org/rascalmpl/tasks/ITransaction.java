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

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.debug.IRascalMonitor;

public interface ITransaction<K,N,V> {

	IFact<V> setFact(K key, N name, V value);

	IFact<V> setFact(K key, N name, V value, Collection<IFact<V>> deps);

	IFact<V> setFact(K key, N name, V value, Collection<IFact<V>> deps, IFactFactory factory);
	
	//V getFact(K key, N name);

	V getFact(IRascalMonitor monitor, K key, N name);

	/**
	 * This method does *not* trigger fact production
	 * @param key
	 * @param name
	 * @return The fact's value, if it exists
	 */
	V queryFact(K key, N name);
	
	/**
	 * This method does *not* trigger fact production
	 * @param key
	 * @param name
	 * @return The fact itself, if it exists
	 */
	IFact<V> findFact(K key, N name);

	void removeFact(K key, N name);

	void abandon();
	
	void commit();

	void commit(Collection<IFact<V>> deps);

	void registerListener(IDependencyListener listener, K key);

	void unregisterListener(IDependencyListener listener, K key);

	IFact<IValue> setFact(K key, V name, IFact<V> fact);
}
