package org.rascalmpl.tasks.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.rascalmpl.tasks.IDependencyListener;
import org.rascalmpl.tasks.IFact;
import org.rascalmpl.tasks.internal.AbstractFact;

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

	@Override
	public void updateFrom(IFact<V> fact) {
		// TODO Auto-generated method stub

	}

}
