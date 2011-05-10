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

import org.rascalmpl.interpreter.IRascalMonitor;

public interface ITaskRegistry<K, N, V> extends ITask<K, N, V> {

	public abstract ITask<K, N, V> getProducer(K key, N name);

	public abstract boolean produce(IRascalMonitor monitor,
			ITransaction<K, N, V> tr, K key, N name);

	public abstract void registerProducer(ITask<K, N, V> producer);

	public abstract void unregisterProducer(ITask<K, N, V> producer);

	public abstract void setRefPolicy(K key, RefFactPolicy policy);

	public abstract void setDepPolicy(K key, DepFactPolicy policy);

	public abstract DepFactPolicy getDepPolicy(K key);

	public abstract RefFactPolicy getRefPolicy(K key);

	public abstract Collection<K> getKeys();

	public abstract void clear();

}
