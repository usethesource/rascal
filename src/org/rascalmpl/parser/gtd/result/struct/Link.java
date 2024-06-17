/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.result.struct;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.util.ArrayList;

/**
 * A structure that links a result node to a set of prefixes.
 */
public class Link{
	private final ArrayList<Link> prefixes;
	private final AbstractNode node;
	
	public Link(ArrayList<Link> prefixes, AbstractNode node){
		super();
		
		this.prefixes = prefixes;
		this.node = node;
	}
	
	public ArrayList<Link> getPrefixes(){
		return prefixes;
	}
	
	public AbstractNode getNode(){
		return node;
	}

	public String toString() {
		return "Link[node=" + node + ", prefixes=" + (prefixes == null ? 0 : prefixes.size()) + "]";
	}
}
