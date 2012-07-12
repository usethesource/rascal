package org.rascalmpl.parser.gtd.recovery;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.Stack;

public interface IRecoverer<P>{
	void reviveStacks(DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes,
			int[] input,
			int location,
			Stack<AbstractStackNode<P>> unexpandableNodes,
			Stack<AbstractStackNode<P>> unmatchableLeafNodes,
			DoubleStack<ArrayList<AbstractStackNode<P>>, AbstractStackNode<P>> unmatchableMidProductionNodes,
			DoubleStack<AbstractStackNode<P>, AbstractNode> filteredNodes);
}
