package org.rascalmpl.parser.uptr.action;

import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.values.parsetrees.ITree;

public class NoActionExecutor implements IActionExecutor<ITree> {

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
	public ITree filterProduction(ITree tree, Object environment) {
		return tree;
	}

	@Override
	public ITree filterListProduction(ITree tree, Object environment) {
		return tree;
	}

	@Override
	public ITree filterAmbiguity(ITree ambCluster, Object environment) {
		return ambCluster;
	}

	@Override
	public ITree filterListAmbiguity(ITree ambCluster, Object environment) {
		return ambCluster;
	}

	@Override
	public ITree filterCycle(ITree cycle, Object environment) {
		return cycle;
	}

	@Override
	public ITree filterListCycle(ITree cycle, Object environment) {
		return cycle;
	}

	@Override
	public boolean isImpure(Object rhs) {
		return false;
	}
}
