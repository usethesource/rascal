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


public interface IDependencyListener {
	enum Change {
		/**
		 *  Fact has changed.
		 *  
		 *  A subsequent call to getValue() may block, if the fact is being computed
		 *  asynchronously.
		 */
		CHANGED,
		/**
		 *  Fact has been invalidated, and *may* change when recomputed.
		 */
		INVALIDATED,
		
		/**
		 *  Fact has been removed. The listener will be unregistered automatically, so this
		 *  last notification that will be sent. 
		 */
		REMOVED,
		
		/**
		 *  The fact value is now available (after being computed asynchronously).
		 *  CHANGED will always be sent before AVAILABLE, so this notification can be safely 
		 *  ignored, unless one is interested in asynchronous updates.
		 *  
		 *  Subsequent calls to getValue() may still block, if the fact changes in between. 
		 */
		AVAILABLE,
		MOVED_TO, EXPIRED
	}
	void changed(IFact<?> f, Change c, Object moreInfo);
	
}
