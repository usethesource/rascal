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

import org.rascalmpl.debug.IRascalMonitor;

public interface ITaskRegistry<K, N, V> extends ITask<K, N, V> {

	ITask<K, N, V> getProducer(K key, N name);

	boolean produce(IRascalMonitor monitor,
			ITransaction<K, N, V> tr, K key, N name);

	void registerProducer(ITask<K, N, V> producer);

	void unregisterProducer(ITask<K, N, V> producer);

	void setRefPolicy(K key, RefFactPolicy policy);

	void setDepPolicy(K key, DepFactPolicy policy);

	DepFactPolicy getDepPolicy(K key);

	RefFactPolicy getRefPolicy(K key);

	Collection<K> getKeys();

	void clear();
	
	/**
	 *  Lock the registry, prior to adding a series of related producers.
	 */
	void lock();
	
	/**
	 *  Unlock the registry.
	 */
	void unlock();
}
