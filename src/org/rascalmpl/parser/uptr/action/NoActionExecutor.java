package org.rascalmpl.parser.uptr.action;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;

public class NoActionExecutor implements IActionExecutor<IConstructor> {

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
	public IConstructor filterProduction(IConstructor tree, Object environment) {
		return tree;
	}

	@Override
	public IConstructor filterListProduction(IConstructor tree,
			Object environment) {
		return tree;
	}

	@Override
	public IConstructor filterAmbiguity(IConstructor ambCluster,
			Object environment) {
		return ambCluster;
	}

	@Override
	public IConstructor filterListAmbiguity(IConstructor ambCluster,
			Object environment) {
		return ambCluster;
	}

	@Override
	public IConstructor filterCycle(IConstructor cycle, Object environment) {
		return cycle;
	}

	@Override
	public IConstructor filterListCycle(IConstructor cycle, Object environment) {
		return cycle;
	}

	@Override
	public boolean isImpure(Object rhs) {
		return false;
	}

}
