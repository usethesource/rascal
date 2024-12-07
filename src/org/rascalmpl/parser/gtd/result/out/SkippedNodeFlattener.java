/*******************************************************************************
 * Copyright (c) 2009-2024 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.result.out;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.SkippedNode;

/**
 * A converter for result nodes that contain skipped characters for error recovery
 */
public class SkippedNodeFlattener<T, P>{
	private static class MemoKey {
		private int offset;
		private int length;

		public MemoKey(SkippedNode node) {
			this.offset = node.getOffset();
			this.length = node.getLength();
		}

		@Override
		public boolean equals(Object peer) {
			MemoKey peerKey = (MemoKey) peer;
			return offset == peerKey.offset && length == peerKey.length;
		}

		@Override
		public int hashCode() {
			return Objects.hash(offset, length);
		}
	}

	private Map<MemoKey, T> memoTable;

	public SkippedNodeFlattener(){
		super();
		
		memoTable = new HashMap<>();
	}
	
	public T convertToUPTR(INodeConstructorFactory<T, P> nodeConstructorFactory, SkippedNode node, PositionStore positionStore){
		MemoKey key = new MemoKey(node);

		T result = memoTable.get(key);

		if (result != null) {
			return result;
		}

		result = nodeConstructorFactory.createSkippedNode(node.getSkippedChars());

		// Add source location
		if (node.getInputUri() != null) {
			int startOffset = node.getOffset();
			int endOffset = startOffset + node.getLength();
			P sourceLocation = nodeConstructorFactory.createPositionInformation(node.getInputUri(), startOffset, endOffset, positionStore);
			result = nodeConstructorFactory.addPositionInformation(result, sourceLocation);
		}

		memoTable.put(key, result);

		return result;
	}
}
