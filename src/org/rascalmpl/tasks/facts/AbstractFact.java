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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.rascalmpl.tasks.IDependencyListener;
import org.rascalmpl.tasks.IFact;


public abstract class AbstractFact<V,ValueStoreType> implements IFact<V> {
	protected ValueStoreType value = null;
	protected int status = IFact.FACT_DEPS_CHANGED;
	protected final Set<IDependencyListener> listeners = new HashSet<IDependencyListener>();
	protected final Set<IFact<?>> dependencies = new HashSet<IFact<?>>();
	protected final Object key;
	protected final String keyName;

	protected AbstractFact(Object key, String keyName) {
		this.key = key;
		this.keyName = keyName;
	}
	@Override
	public boolean isValid() {
		return status == IFact.FACT_OK;
	}
	
	@Override
	public void registerListener(IDependencyListener listener) {
		listeners.add(listener);
	}

	@Override
	public void unregisterListener(IDependencyListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void remove() {
		for(IDependencyListener l : listeners) {
			l.changed(this, REMOVED, null);
		}
		listeners.clear();
		value = null;
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


	@Override
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

	@Override
	public synchronized Collection<IFact<?>> getDepends() {
		return Collections.unmodifiableCollection(dependencies);
	}
	
	@Override
	public int getStatus() {
		return status;
	}
}
