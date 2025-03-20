/*******************************************************************************
 * Copyright (c) 2012-2024 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.debug;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.Stack;

public interface IDebugListener<P>{
	void shifting(int offset, int[] input, PositionStore positionStore);
	
	void iterating();
	
	void matched(AbstractStackNode<P> node, AbstractNode result);
	
	void failedToMatch(AbstractStackNode<P> node);

	void expanding(AbstractStackNode<P> node);

	void expanded(AbstractStackNode<P> node, AbstractStackNode<P> child);

	void foundIterationCachedNullableResult(AbstractStackNode<P> node);

	void moving(AbstractStackNode<P> node, AbstractNode result);

	void progressed(AbstractStackNode<P> node, AbstractNode result, AbstractStackNode<P> next);

	void propagated(AbstractStackNode<P> node, AbstractNode nodeResult, AbstractStackNode<P> next);

	void reducing(AbstractStackNode<P> node, Link resultLink, EdgesSet<P> edges);

	void reduced(AbstractStackNode<P> parent);

	void filteredByNestingRestriction(AbstractStackNode<P> parent);

	void filteredByEnterFilter(AbstractStackNode<P> node);

	void filteredByCompletionFilter(AbstractStackNode<P> node, AbstractNode result);

	void reviving(int[] input,
			int location,
			Stack<AbstractStackNode<P>> unexpandableNodes,
			Stack<AbstractStackNode<P>> unmatchableLeafNodes,
			DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>,
			AbstractStackNode<P>> unmatchableMidProductionNodes,
			DoubleStack<AbstractStackNode<P>, AbstractNode> filteredNodes);

	void revived(DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes);
}
