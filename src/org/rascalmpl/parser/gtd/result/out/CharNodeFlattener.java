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

import org.rascalmpl.parser.gtd.result.CharNode;

/**
 * A converter for character result nodes.
 */
@SuppressWarnings("unchecked")
public class CharNodeFlattener<T, P>{
	private final T[] cache = (T[]) new Object[128];
	
	public CharNodeFlattener(){
		super();
	}
	
	/**
	 * Converts the given character result node to the UPTR format.
	 */
	public T convertToUPTR(INodeConstructorFactory<T, P> nodeConstructorFactory, CharNode node){
		int charNumber = node.getCharacter();
		
		// Cache 7-bit ASCII character results.
		if(charNumber < 128){
			T result = cache[charNumber];
			if(result != null) return result;
			
			result = nodeConstructorFactory.createCharNode(charNumber);
			cache[charNumber] = result;
		}
		
		return nodeConstructorFactory.createCharNode(charNumber);
	}
}
