/*******************************************************************************
 * Copyright (c) 2009-2025 CWI
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
 * SkippedNode flattening is cached seperately because skipped nodes are special:
 * nodes for the same piece of text can be created by different error recovery attempts.
 * By caching them here, the nodes are reused and shared instead of created anew each time.
 */
public class SkippedNodeFlattener<T, P> {
	private static class MemoKey {
		private int offset;
		private int length;
		private int errorPos;

		public MemoKey(SkippedNode node) {
			this.offset = node.getOffset();
			this.length = node.getLength();
			this.errorPos = node.getErrorPosition();
		}

		@Override
		public boolean equals(Object peer) {
			if (!(peer instanceof MemoKey)) {
				return false;
			}
			MemoKey peerKey = (MemoKey) peer;
			return offset == peerKey.offset && length == peerKey.length && errorPos == peerKey.errorPos;
		}

		@Override
		public int hashCode() {
			return Objects.hash(offset, length, errorPos);
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

		// Add source and parse error locations
		if (node.getInputUri() != null) {
			int startOffset = node.getOffset();
			int endOffset = startOffset + node.getLength();
			P sourceLocation = nodeConstructorFactory.createPositionInformation(node.getInputUri(), startOffset, endOffset, positionStore);
			result = nodeConstructorFactory.addPositionInformation(result, sourceLocation);

			int errorPos = node.getErrorPosition();
			P errorLocation = nodeConstructorFactory.createPositionInformation(node.getInputUri(), errorPos, errorPos+1, positionStore);
			result = nodeConstructorFactory.addParseErrorPosition(result, errorLocation);
		}

		memoTable.put(key, result);

		return result;
	}
}
