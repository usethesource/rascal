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

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.SkippedNode;

/**
 * A converter for result nodes that contain skipped characters for error recovery
 */
public class RecoveryNodeFlattener<T, P>{
	
	public RecoveryNodeFlattener(){
		super();
	}
	
	public T convertToUPTR(INodeConstructorFactory<T, P> nodeConstructorFactory, SkippedNode node, PositionStore positionStore){
		T result = nodeConstructorFactory.createSkippedNode(node.getSkippedChars());

		// Add source location
		int startOffset = node.getOffset();
		int endOffset = startOffset + node.getLength();
		P sourceLocation = nodeConstructorFactory.createPositionInformation(node.getInput(), startOffset, endOffset, positionStore);
		result = nodeConstructorFactory.addPositionInformation(result, sourceLocation);

		return result;
	}
}
