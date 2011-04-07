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

import org.rascalmpl.tasks.DepFactPolicy;
import org.rascalmpl.tasks.IFact;
import org.rascalmpl.tasks.RefFactPolicy;
import org.rascalmpl.tasks.facts.FineGrainedStrongFact;

public class FactFactory {
	
	public static <V,K> IFact<V> fact(Class<V> cls, Object key, DepFactPolicy depPolicy, RefFactPolicy refPolicy) {
		return new FineGrainedStrongFact<V>(key);
	}
}
