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

public interface IRecoverer<P> {
    /**
     * reviveStacks is called when the parser is unable to make more progress and the end-of-input
     * has not been reached. The parameters provide insight into the current state of the parser
     * and some of its history. With this information new stack nodes may be generated for the parser
     * to continue with. It is up to the reviveStacks method to make sure the parser still generates
     * derivation trees that cover the entire input.
     * 
     * @param input                  the 24-bit unicode input character array
     * @param location               the current character offset in the input that the parser got stuck on
     * @param unexpandableNodes      these are non-terminals that were predicted at this location but did not fly
     * @param unmatchableLeafNodes   these are the terminals that were predicted but did not fly
     * @param unmatchableMidProductionNodes these are quasi-non-terminals due to prefix sharing that did not fly
     * @param filteredNodes          these are non-terminals nodes that did not fly due to a disambiguation filter
     * @return                       a list of new predictions for the parser to continue with
     */
	DoubleArrayList<AbstractStackNode<P>, AbstractNode> reviveStacks(int[] input,
			int location,
			Stack<AbstractStackNode<P>> unexpandableNodes,
			Stack<AbstractStackNode<P>> unmatchableLeafNodes,
			DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>,
			AbstractStackNode<P>> unmatchableMidProductionNodes,
			DoubleStack<AbstractStackNode<P>, AbstractNode> filteredNodes);
}
