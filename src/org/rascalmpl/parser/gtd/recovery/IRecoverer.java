package org.rascalmpl.parser.gtd.recovery;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.Stack;

public interface IRecoverer {
	void reviveStacks(DoubleArrayList<AbstractStackNode, AbstractNode> recoveredNodes,
			int[] input,
			int location,
			Stack<AbstractStackNode> unexpandableNodes,
			Stack<AbstractStackNode> unmatchableLeafNodes,
			DoubleStack<ArrayList<AbstractStackNode>, AbstractStackNode> unmatchableMidProductionNodes,
			DoubleStack<AbstractStackNode, AbstractNode> filteredNodes);
}
