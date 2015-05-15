package org.rascalmpl.parser.uptr.action;

import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.values.uptr.RascalValueFactory.Tree;

public class NoActionExecutor implements IActionExecutor<Tree> {

	@Override
	public Object createRootEnvironment() {
		return new Object();
	}

	@Override
	public void completed(Object environment, boolean filtered) {
	}

	@Override
	public Object enteringProduction(Object production, Object environment) {
		return environment;
	}

	@Override
	public Object enteringListProduction(Object production, Object environment) {
		return environment;
	}

	@Override
	public Object enteringNode(Object production, int index, Object environment) {
		return environment;
	}

	@Override
	public Object enteringListNode(Object production, int index, Object environment) {
		return environment;
	}

	@Override
	public void exitedProduction(Object production, boolean filtered,
			Object environment) {
	}

	@Override
	public void exitedListProduction(Object production, boolean filtered,
			Object environment) {
	}

	@Override
	public Tree filterProduction(Tree tree, Object environment) {
		return tree;
	}

	@Override
	public Tree filterListProduction(Tree tree,
			Object environment) {
		return tree;
	}

	@Override
	public Tree filterAmbiguity(Tree ambCluster,
			Object environment) {
		return ambCluster;
	}

	@Override
	public Tree filterListAmbiguity(Tree ambCluster,
			Object environment) {
		return ambCluster;
	}

	@Override
	public Tree filterCycle(Tree cycle, Object environment) {
		return cycle;
	}

	@Override
	public Tree filterListCycle(Tree cycle, Object environment) {
		return cycle;
	}

	@Override
	public boolean isImpure(Object rhs) {
		return false;
	}

}
