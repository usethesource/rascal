package org.rascalmpl.parser.gtd.debug;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;

public interface IDebugListener<P>{
	void shifting(int location, int[] input);
	
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
}
