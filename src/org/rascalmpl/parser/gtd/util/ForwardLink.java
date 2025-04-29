/*******************************************************************************
 * Copyright (c) 2011-2025 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.util;

import org.rascalmpl.parser.gtd.result.out.INodeFlattener;

@SuppressWarnings({"rawtypes","unchecked"})
public class ForwardLink<E>{
	public static final ForwardLink TERMINATOR = new ForwardLink();
	
	public final ForwardLink<E> next;
	public final int length;
	
	public final E element;
	public final INodeFlattener.CacheMode cacheMode;

	public final int maxAmbDepth;
	
	private ForwardLink(){
		super();
		
		this.next = null;
		this.length = 0;
		
		this.element = null;
		this.maxAmbDepth = 0;

		cacheMode = INodeFlattener.CacheMode.CACHE_MODE_NONE;
	}

	public ForwardLink(ForwardLink next, E element, int maxAmbDepth) {
		this(next, element, INodeFlattener.CacheMode.CACHE_MODE_NONE, maxAmbDepth);
	}

	public ForwardLink(ForwardLink next, E element, INodeFlattener.CacheMode cacheMode, int maxAmbDepth){
		super();
		
		this.next = next;
		this.length = next.length + 1;
		
		this.element = element;

		this.cacheMode = cacheMode;
		this.maxAmbDepth = maxAmbDepth;
	}

	// Copy constructor
	public ForwardLink(ForwardLink<E> original, int maxAmbDepth) {
		this.next = original.next;
		this.length = original.length;
		this.element = original.element;
		this.cacheMode = original.cacheMode;
		this.maxAmbDepth = maxAmbDepth;
	}
}
