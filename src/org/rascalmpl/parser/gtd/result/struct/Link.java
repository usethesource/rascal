/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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

public class Link{
	public final ArrayList<Link> prefixes;
	public final AbstractNode node;
	
	public Link(ArrayList<Link> prefixes, AbstractNode node){
		super();
		
		this.prefixes = prefixes;
		this.node = node;
	}
}