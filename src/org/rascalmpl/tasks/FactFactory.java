/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
*******************************************************************************/
package org.rascalmpl.tasks;

import org.rascalmpl.tasks.DepFactPolicy;
import org.rascalmpl.tasks.IFact;
import org.rascalmpl.tasks.RefFactPolicy;
import org.rascalmpl.tasks.facts.FineGrainedStrongFact;

public class FactFactory implements IFactFactory {
	private static IFactFactory factory;
	
	private FactFactory() {
	}
	
	public static IFactFactory getInstance() {
		if(factory == null)
			factory = new FactFactory();
		return factory;
	}
	
	public <V,K> IFact<V> fact(Class<V> cls, Object key, String keyName, IExpirationListener<V> lis, DepFactPolicy depPolicy, RefFactPolicy refPolicy) {
		return new FineGrainedStrongFact<V>(key, keyName, lis);
	}

}
