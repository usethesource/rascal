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
package org.rascalmpl.parser.gtd.result.out;

import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.util.PointerKeyedHashMap;

/**
 * A converter for literal result nodes.
 */
public class LiteralNodeFlattener<T, P>{
	private final PointerKeyedHashMap<LiteralNode, T> cache;
	
	public LiteralNodeFlattener(){
		super();
		
		cache = new PointerKeyedHashMap<LiteralNode, T>();
	}
	
	/**
	 * Converts the given literal result node to the UPTR format.
	 */
	public T convertToUPTR(INodeConstructorFactory<T, P> nodeConstructorFactory, LiteralNode node){
		T result = cache.get(node);
		if(result != null) return result;
		
		result = nodeConstructorFactory.createLiteralNode(node.getContent(), node.getProduction());
		
		cache.putUnsafe(node, result);
		
		return result;
	}
}
