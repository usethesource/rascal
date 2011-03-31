package org.rascalmpl.tasks;

import org.rascalmpl.tasks.DepFactPolicy;
import org.rascalmpl.tasks.IFact;
import org.rascalmpl.tasks.RefFactPolicy;
import org.rascalmpl.tasks.internal.FineGrainedStrongFact;

public class FactFactory {
	
	public static <V,K> IFact<V> fact(Class<V> cls, Object key, DepFactPolicy depPolicy, RefFactPolicy refPolicy) {
		return new FineGrainedStrongFact<V>(key);
	}
}
