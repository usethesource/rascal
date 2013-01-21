/*******************************************************************************
 * Copyright (c) 2012-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.recovery;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.Stack;

public interface IRecoverer<P>{
	DoubleArrayList<AbstractStackNode<P>, AbstractNode> reviveStacks(int[] input,
			int location,
			Stack<AbstractStackNode<P>> unexpandableNodes,
			Stack<AbstractStackNode<P>> unmatchableLeafNodes,
			DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>,
			AbstractStackNode<P>> unmatchableMidProductionNodes,
			DoubleStack<AbstractStackNode<P>, AbstractNode> filteredNodes);
}
