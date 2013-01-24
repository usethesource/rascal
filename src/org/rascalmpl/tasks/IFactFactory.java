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


public interface IFactFactory {
	public <V,K> IFact<V> fact(Class<V> cls, Object key, String keyName, IExpirationListener<V> lis, DepFactPolicy depPolicy, RefFactPolicy refPolicy);
}
