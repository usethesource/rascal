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
package org.rascalmpl.parser.gtd.result;

import java.net.URI;

/**
 * A 'expandable' result node.
 * Expandables, like sorts, are a types of containers.
 * With expandables we refer to lists, optional and sequences and such.
 */
public class ExpandableContainerNode<P> extends AbstractContainerNode<P>{
	public final static int ID = 5;
	
	public ExpandableContainerNode(URI input, int offset, int endOffset, boolean isNullable, boolean isSeparator, boolean isLayout){
		super(input, offset, endOffset, isNullable, isSeparator, isLayout);
	}
	
	public int getTypeIdentifier(){
		return ID;
	}

	@Override
	public String toString() {
		return "ExpandableContainerNode[" + super.toString() + "]";
	}
}
