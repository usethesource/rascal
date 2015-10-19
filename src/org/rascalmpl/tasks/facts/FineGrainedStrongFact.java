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
package org.rascalmpl.tasks.facts;

import static org.rascalmpl.tasks.IDependencyListener.Change.MOVED_TO;
import static org.rascalmpl.tasks.IDependencyListener.Change.REMOVED;

import java.util.Collection;
import java.util.Iterator;

import org.rascalmpl.tasks.IDependencyListener;
import org.rascalmpl.tasks.IExpirationListener;
import org.rascalmpl.tasks.IFact;
import org.rascalmpl.value.IValue;
/**
 * This class implements fact storage for strongly referenced facts (i.e., a fact will never be removed
 * by the garbarge collector unless it is explicitly removed from the database) with fine-grained
 * tracking of dependencies.
 * 
 * @author anya
 *
 */
public class FineGrainedStrongFact<V> extends AbstractFact<V> {

	public FineGrainedStrongFact(Object key, String keyName, IExpirationListener<V> exp) {
		super(key, keyName, exp);
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.eclipse.db.IFact#setValue(org.rascalmpl.value.T)
	 */
	public synchronized boolean setValue(V val) {
		V oldValue = value;
		if(oldValue == null)
			oldValue = getRef();
		clearRef();
		
		value = val;
		status = IFact.FACT_OK;
		if(oldValue != null &&
			!(value instanceof IValue ? ((IValue)oldValue).isEqual((IValue)val) : oldValue.equals(value))) {
					notifyChanged();
					return true;
		}
		else
			return false;
	}

	@SuppressWarnings("incomplete-switch")
	public synchronized void changed(IFact<?> fact, Change change, Object moreInfo) {
		switch(change) {
		case CHANGED:
			if(status < IFact.FACT_DEPS_CHANGED) {
				System.out.println("CHANGED: " + fact + " recv by " + this);
				setRefWeak(value);
				value = null;
				status = IFact.FACT_DEPS_CHANGED;
				notifyInvalidated();
			}
			break;
		case INVALIDATED:
			if(status < IFact.FACT_DEPS_INVALID) {
				System.out.println("INVALID: " + fact + " recv by " + this);
				status = IFact.FACT_DEPS_INVALID;
				notifyInvalidated();
			}
			break;
		case REMOVED:
			if(status < IFact.FACT_DEPS_CHANGED) {
				dependencies.remove(fact);
				setRefWeak(value);
				value = null;
				status = IFact.FACT_DEPS_CHANGED;
				notifyInvalidated();
			}
			break;
		case MOVED_TO:
			if(dependencies.remove(fact)) {
				dependencies.add((IFact<?>) moreInfo);
			}
			break;
		case EXPIRED:
			if(dependencies.remove(fact)) {
				Collection<IFact<?>> deps = fact.getDepends();
				dependencies.addAll(deps);
				for(IFact<?> dep : deps)
					dep.registerListener(this);
			}
			break;
		}
	}



	/**
	 * Disposing of a fact means telling all our dependencies to notify our
	 * dependents instead about any changes. This is done when the fact itself
	 * is still useful, but we don't want to keep it in memory anymore. (We
	 * basically link ourselves out of the dependency chain)
	 */
	public void dispose() {
		for(IFact<?> f : dependencies) {
			f.unregisterListener(this);
		}
		for(IDependencyListener l : listeners) {
			l.changed(this, REMOVED, null);
		}

		dependencies.clear();
		listeners.clear();
		value = null;
		clearRef();
	}

	public synchronized V getValue() {
		if(status == IFact.FACT_OK)
			return value;
		else
			return null;
	}



	@SuppressWarnings("unchecked")
	public synchronized boolean updateFrom(IFact<V> fact) {
		boolean result = false;
		synchronized(fact) {
				if(fact instanceof AbstractFact<?>) {
				AbstractFact<?> f = (AbstractFact<?>)fact;
				int oldStatus = status;
				status = f.status;
				V oldValue = value;
				if(oldValue == null)
					oldValue = getRef();

				if (status == FACT_OK) {
					value = (V) f.value;
					if (oldValue != null
							&& !(oldValue instanceof IValue ? ((IValue) oldValue)
									.isEqual((IValue) value) : oldValue
									.equals(value))) {
						notifyChanged();
						result = true;
					}
				}
				else if (oldStatus == FACT_OK) {
					notifyInvalidated();
					value = null;
					clearRef();
				}

				//else
				//	throw new ImplementationError("Trying to update from fact with incompatible value types");
				Iterator<IFact<?>> iterator = dependencies.iterator();
				while(iterator.hasNext()) {
					IFact<?> df = iterator.next();
					if(!f.dependencies.contains(df)) {
						iterator.remove();
						df.unregisterListener(this);
					}
				}
				for(IFact<?> df : f.dependencies) {
					if(!dependencies.contains(df)) {
						dependencies.add(df);
						df.registerListener(this);
					}
				}
				for(IDependencyListener dl : f.listeners)
					dl.changed(f, MOVED_TO, this);
				listeners.addAll(f.listeners);
			}
			

		}
		return result;
	}

}
