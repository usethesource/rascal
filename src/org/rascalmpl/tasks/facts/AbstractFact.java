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

import java.util.HashSet;
import java.util.Set;

import org.rascalmpl.tasks.IDependencyListener;
import org.rascalmpl.tasks.IFact;


public abstract class AbstractFact<V,ValueStoreType> implements IFact<V> {
	public static final int FACT_OK = 0;
	public static final int FACT_DEPS_INVALID = 1;
	public static final int FACT_DEPS_CHANGED = 2;

	protected ValueStoreType value = null;
	protected int status = FACT_DEPS_CHANGED;
	protected final Set<IDependencyListener> listeners = new HashSet<IDependencyListener>();
	protected final Object key;

	protected AbstractFact(Object key) {
		this.key = key;
	}
	@Override
	public boolean isValid() {
		return status == FACT_OK;
	}
	
	@Override
	public void registerListener(IDependencyListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void unregisterListener(IDependencyListener listener) {
		listeners.add(listener);
	}

	@Override
	public void remove() {
		for(IDependencyListener l : listeners) {
			l.changed(this, REMOVED);
		}
		listeners.clear();
		value = null;
	}
	
	protected void notifyInvalidated() {
		for(IDependencyListener f : listeners) {
			f.changed(this, INVALIDATED);
		}
	}

	protected void notifyChanged() {
		for(IDependencyListener f : listeners) {
			f.changed(this, CHANGED);
		}
	}

	protected void notifyAvailable() {
		for(IDependencyListener f : listeners) {
			f.changed(this, AVAILABLE);
		}
	}
	
	public Object getKey() {
		return key;
	}
}
