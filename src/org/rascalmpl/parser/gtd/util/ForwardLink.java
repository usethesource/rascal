/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.util;

@SuppressWarnings({"rawtypes","unchecked"})
public class ForwardLink<E>{
	public final static ForwardLink TERMINATOR = new ForwardLink();
	
	public final ForwardLink<E> next;
	public final int length;
	
	public final E element;
	public boolean cacheable;
	
	private ForwardLink(){
		super();
		
		this.next = null;
		this.length = 0;
		
		this.element = null;
	}
	
	public ForwardLink(ForwardLink next, E element, boolean cacheable){
		super();
		
		this.next = next;
		this.length = next.length + 1;
		
		this.element = element;

		this.cacheable = cacheable;
	}
}
