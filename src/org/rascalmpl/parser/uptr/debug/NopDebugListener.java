package org.rascalmpl.parser.uptr.debug;

import org.rascalmpl.parser.gtd.debug.IDebugListener;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.Stack;

public class NopDebugListener<P> implements IDebugListener<P> {

    @Override
    public void shifting(int offset, int[] input, PositionStore positionStore) {
        // Do nothing
    }

    @Override
    public void iterating() {
        // Do nothing
    }

    @Override
    public void matched(AbstractStackNode<P> node, AbstractNode result) {
        // Do nothing
    }

    @Override
    public void failedToMatch(AbstractStackNode<P> node) {
        // Do nothing
    }

    @Override
    public void expanding(AbstractStackNode<P> node) {
        // Do nothing
    }

    @Override
    public void expanded(AbstractStackNode<P> node, AbstractStackNode<P> child) {
        // Do nothing
    }

    @Override
    public void foundIterationCachedNullableResult(AbstractStackNode<P> node) {
        // Do nothing
    }

    @Override
    public void moving(AbstractStackNode<P> node, AbstractNode result) {
        // Do nothing
    }

    @Override
    public void progressed(AbstractStackNode<P> node, AbstractNode result, AbstractStackNode<P> next) {
        // Do nothing
    }

    @Override
    public void propagated(AbstractStackNode<P> node, AbstractNode nodeResult, AbstractStackNode<P> next) {
        // Do nothing
    }

    @Override
    public void reducing(AbstractStackNode<P> node, Link resultLink, EdgesSet<P> edges) {
        // Do nothing
    }

    @Override
    public void reduced(AbstractStackNode<P> parent) {
        // Do nothing
    }

    @Override
    public void filteredByNestingRestriction(AbstractStackNode<P> parent) {
        // Do nothing
    }

    @Override
    public void filteredByEnterFilter(AbstractStackNode<P> node) {
        // Do nothing
    }

    @Override
    public void filteredByCompletionFilter(AbstractStackNode<P> node, AbstractNode result) {
        // Do nothing
    }

    @Override
    public void reviving(int[] input, int location, Stack<AbstractStackNode<P>> unexpandableNodes,
        Stack<AbstractStackNode<P>> unmatchableLeafNodes,
        DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>, AbstractStackNode<P>> unmatchableMidProductionNodes,
        DoubleStack<AbstractStackNode<P>, AbstractNode> filteredNodes) {
        // Do nothing
    }

    @Override
    public void revived(DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes) {
        // Do nothing
    }

}
