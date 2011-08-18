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
package org.rascalmpl.tasks.facts;

import static org.rascalmpl.tasks.IDependencyListener.Change.AVAILABLE;
import static org.rascalmpl.tasks.IDependencyListener.Change.CHANGED;
import static org.rascalmpl.tasks.IDependencyListener.Change.INVALIDATED;
import static org.rascalmpl.tasks.IDependencyListener.Change.REMOVED;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.rascalmpl.tasks.IDependencyListener;
import org.rascalmpl.tasks.IExpirationListener;
import org.rascalmpl.tasks.IFact;


public abstract class AbstractFact<V> implements IFact<V> {
	static final ReferenceQueue<Object> queue = new ReferenceQueue<Object>(); 
	protected V value = null;
	private IRef<V> valueRef = null;
	protected int status = IFact.FACT_DEPS_CHANGED;
	protected final Set<IDependencyListener> listeners = new HashSet<IDependencyListener>();
	protected final Set<IFact<?>> dependencies = new HashSet<IFact<?>>();
	protected final Object key;
	protected final String keyName;
	protected IExpirationListener<V> exp;

	protected AbstractFact(Object key, String keyName, IExpirationListener<V> exp) {
		this.key = key;
		this.keyName = keyName;
		this.exp = exp;
	}
	public boolean isValid() {
		return status == IFact.FACT_OK;
	}
	
	public void registerListener(IDependencyListener listener) {
		listeners.add(listener);
	}

	public void unregisterListener(IDependencyListener listener) {
		listeners.remove(listener);
	}

	public synchronized void remove() {
		for(IDependencyListener l : listeners) {
			l.changed(this, REMOVED, null);
		}
		listeners.clear();
		for(IFact<?> f : dependencies) {
			f.unregisterListener(this);
		}
		dependencies.clear();
		value = null;
		clearRef();
	}
	
	protected void notifyInvalidated() {
		for(IDependencyListener f : listeners) {
			f.changed(this, INVALIDATED, null);
		}
	}

	protected void notifyChanged() {
		for(IDependencyListener f : listeners) {
			f.changed(this, CHANGED, null);
		}
	}

	protected void notifyAvailable() {
		for(IDependencyListener f : listeners) {
			f.changed(this, AVAILABLE, null);
		}
	}
	
	public Collection<IDependencyListener> getListeners() {
		return Collections.unmodifiableCollection(listeners);
	}
	
	public Object getKey() {
		return key;
	}
	
	public String toString() {
		return keyName;
	}

	public synchronized void setDepends(Collection<IFact<V>> deps) {
		for(IFact<?> foo : dependencies) {
			if(!deps.contains(foo))
				foo.unregisterListener(this);
		}
		for(IFact<?> foo : deps) {
			if(!dependencies.contains(foo))
				foo.registerListener(this);
		}
		dependencies.clear();
		dependencies.addAll(deps);
	}

	public synchronized Collection<IFact<?>> getDepends() {
		return Collections.unmodifiableCollection(dependencies);
	}
	
	public int getStatus() {
		return status;
	}
	
	public synchronized void expire() {
		if(status != FACT_OK) {
			if(exp != null)
				exp.expire(key);
			for(IDependencyListener l : listeners) {
				l.changed(this, Change.EXPIRED, null);
			}
			listeners.clear();
			for(IFact<?> f : dependencies) {
				f.unregisterListener(this);
			}
			dependencies.clear();
			value = null;
			clearRef();
		}
		else
			valueRef = null;
	}
	
	protected void setRefWeak(V value) {
		valueRef = new WeakRef<V>(value, this);
	}

	protected void setRefSoft(V value) {
		valueRef = new SoftRef<V>(value, this);
	}
	
	protected void clearRef() {
		if(valueRef != null) {
			valueRef.clear();
			valueRef = null;
		}
	}
	
	protected V getRef() {
		if(valueRef != null)
			return valueRef.get();
		else
			return null;
	}

	public static void pruneExpired() {
		Object o = queue.poll();
		while(o != null) {
			AbstractFact<?> fact = ((IRef<?>) o).getFact();
			fact.expire();
			o = queue.poll();
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
		result = prime * result + status;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result
				+ ((valueRef == null) ? 0 : valueRef.hashCode());
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
		@SuppressWarnings("unchecked")
		AbstractFact<V> other = (AbstractFact<V>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (keyName == null) {
			if (other.keyName != null)
				return false;
		} else if (!keyName.equals(other.keyName))
			return false;
		if (status != other.status)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (valueRef == null) {
			if (other.valueRef != null)
				return false;
		} else if (!valueRef.equals(other.valueRef))
			return false;
		return true;
	}
}


interface IRef<V> {
	public AbstractFact<V> getFact();
	public V get();
	public void clear();
	public int hashCode();
}

class WeakRef<V> extends WeakReference<V> implements IRef<V> {
	private final AbstractFact<V> fact;

	WeakRef(V v, AbstractFact<V> fact) {
		super(v, AbstractFact.queue);
		this.fact = fact;
	}
	
	public AbstractFact<V> getFact() {
		return fact;
	}
}

class SoftRef<V> extends SoftReference<V> implements IRef<V> {
	private final AbstractFact<V> fact;

	SoftRef(V v, AbstractFact<V> fact) {
		super(v, AbstractFact.queue);
		this.fact = fact;
	}
	
	public AbstractFact<V> getFact() {
		return fact;
	}
}
