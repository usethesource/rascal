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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.rascalmpl.tasks.IDependencyListener;
import org.rascalmpl.tasks.IFact;

public abstract class AbstractDepFact<V, ValueStoreType> extends
		AbstractFact<V, ValueStoreType> implements IDependencyListener {

	AbstractDepFact(Object key) {
		super(key);
	}

	protected final Set<IFact<?>> dependencies = new HashSet<IFact<?>>();


	@Override
	public void remove() {
		// TODO Auto-generated method stub
		for(IFact<?> f : dependencies) {
			f.unregisterListener(this);
		}
		dependencies.clear();
		super.remove();
		
	}

	@Override
	public synchronized void setDepends(Collection<IFact<V>> deps) {
		dependencies.clear();
		dependencies.addAll(deps);
	}
}
