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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public class TaskRegistry<K,N,V> implements ITaskRegistry<K, N, V> {
	private static final Map<ClassTriple,TaskRegistry<?,?,?>> schedulers =
			new HashMap<ClassTriple,TaskRegistry<?,?,?>>();
	protected Map<K,ITask<K,N,V>> producers = new HashMap<K,ITask<K,N,V>>();
	protected Map<K, DepFactPolicy> depPolicies = new HashMap<K, DepFactPolicy>();
	protected Map<K, RefFactPolicy> refPolicies = new HashMap<K, RefFactPolicy>();
	protected Lock lock = new ReentrantLock();

	@SuppressWarnings("unchecked")
	public static <K,N,V> ITaskRegistry<K, N, V> getScheduler(Class<K> keyType, Class<N> name, Class<V> value) {
		ClassTriple key = new ClassTriple(keyType, name, value);
		TaskRegistry<?, ?, ?> scheduler = schedulers.get(key);
		if(scheduler == null) {
			scheduler = new TaskRegistry<K,N,V>();
			schedulers.put(key, scheduler);
		}
		return (ITaskRegistry<K, N, V>) scheduler;
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#getProducer(K, N)
	 */
	public ITask<K,N,V> getProducer(K key, N name) {
		ITask<K, N, V> producer = producers.get(key);
		return producer;
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#produce(org.rascalmpl.interpreter.IRascalMonitor, org.rascalmpl.tasks.ITransaction, K, N)
	 */
	public boolean produce(IRascalMonitor monitor, ITransaction<K, N, V> tr, K key, N name) {
		ITask<K, N, V> producer = null;
		lock.lock();
		try {
			producer = getProducer(key, name);
		}
		finally {
			lock.unlock();
		}
		if(producer == null)
			throw new ImplementationError("No registered fact producer for " + key.toString());
		return producer.produce(monitor, tr, key, name);
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#registerProducer(org.rascalmpl.tasks.ITask)
	 */
	public void registerProducer(ITask<K,N,V> producer) {
		lock.lock();
		try {
			for(K key : producer.getKeys())
				producers.put(key, producer);
		}
		finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#unregisterProducer(org.rascalmpl.tasks.ITask)
	 */
	public void unregisterProducer(ITask<K,N,V> producer) {
		lock.lock();
		try {
			for(K key : producer.getKeys()) {
				if(producers.get(key) == producer)
					producers.remove(key);
			}
		}
		finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#setRefPolicy(K, org.rascalmpl.tasks.RefFactPolicy)
	 */
	public void setRefPolicy(K key, RefFactPolicy policy) {
		lock.lock();
		try {
			refPolicies.put(key, policy);
		}
		finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#setDepPolicy(K, org.rascalmpl.tasks.DepFactPolicy)
	 */
	public void setDepPolicy(K key, DepFactPolicy policy) {
		lock.lock();
		try {
			depPolicies.put(key, policy);
		}
		finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#getDepPolicy(K)
	 */
	public DepFactPolicy getDepPolicy(K key) {
		lock.lock();
		try {
			DepFactPolicy policy = depPolicies.get(key);
			if(policy == null)
				policy = DepFactPolicy.DEFAULT;
			return policy;
		}
		finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#getRefPolicy(K)
	 */
	public RefFactPolicy getRefPolicy(K key) {
		lock.lock();
		try {
			RefFactPolicy policy = refPolicies.get(key);
			if(policy == null)
				policy = RefFactPolicy.DEFAULT;
			return policy;
		}
		finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#getKeys()
	 */
	public Collection<K> getKeys() {
		lock.lock();
		try {
			return producers.keySet();
		}
		finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#clear()
	 */
	public void clear() {
		lock.lock();
		try {
			producers.clear();
			schedulers.clear();
			depPolicies.clear();
			refPolicies.clear();
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public void lock() {
		lock.lock();
	}

	@Override
	public void unlock() {
		lock.unlock();
	}
}


class ClassTriple {


	final private Class<?> first;
	final private Class<?> second;
	final private Class<?> third;
	public ClassTriple(Class<?> first, Class<?> second, Class<?> third) {
		super();
		this.first = first;
		this.second = second;
		this.third = third;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result + ((third == null) ? 0 : third.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassTriple other = (ClassTriple) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		if (third == null) {
			if (other.third != null)
				return false;
		} else if (!third.equals(other.third))
			return false;
		return true;
	}
}
