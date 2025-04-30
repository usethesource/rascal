/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 */
package org.rascalmpl.parser.uptr.action;

import java.util.List;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.control_exceptions.Filtered;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.OverloadedFunction;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IConstructor;

/**
 * This is the way of executing filters for Rascal syntax definitions. 
 * A set of "filter" functions is provided which are called at every
 * level in the tree with as parameter the tree to be constructed.
 * If the filter functions do nothing, nothing happens. If they 
 * return a new tree, the old tree is substituted by the new tree.
 * If they throw the `filter` exception, then the branch up to
 * the first surrounding ambiguity cluster is filtered.
 */
public class RascalFunctionActionExecutor implements IActionExecutor<ITree> {
	private final boolean isPure;
	private @Nullable OverloadedFunction filters;

	public RascalFunctionActionExecutor(ISet functions, boolean isPure) {
		this.isPure = isPure;
		this.filters = !functions.isEmpty() ? constructFilterFunction(functions) : null;
	}
	
	private OverloadedFunction constructFilterFunction(ISet functions) {
		List<AbstractFunction> alts = functions.stream()
			.map(v -> (AbstractFunction) v)
			.collect(Collectors.toList());
		
		return new OverloadedFunction("filters", alts);
	}

	public void completed(Object environment, boolean filtered) {

	}

	public Object createRootEnvironment() {
		return new Object();
	}

	public Object enteringListNode(Object production, int index, Object environment) {
		return environment;
	}

	public Object enteringListProduction(Object production, Object env) {
		return env;
	}

	public Object enteringNode(Object production, int index, Object environment) {
		return environment;
	}

	public Object enteringProduction(Object production, Object env) {
		return env;
	}

	public void exitedListProduction(Object production, boolean filtered, Object environment) {

	}

	public void exitedProduction(Object production, boolean filtered, Object environment) {
	}

	public ITree filterAmbiguity(ITree ambCluster, Object environment) {
	    if (!TreeAdapter.isAmb(ambCluster)) {
	        // this may happen when due to filtering during the bottom-up
	        // construction of a tree a singleton amb cluster was build
	        // and then canonicalized to the single instance tree.
	        // nothing to worry about. 
	        return ambCluster;
	    }
		
		if (TreeAdapter.getAlternatives(ambCluster).size() == 0) {
			return null;
		}

		if (filters == null) {
			return ambCluster;
		}

		try {
			ITree result = filters.call(ambCluster);

			if (!TreeAdapter.isAmb(result)) {
				return result;
			}
			else if (TreeAdapter.getAlternatives(result).size() == 1) {
				return (ITree) TreeAdapter.getAlternatives(result).iterator().next();
			}
			else {
				return result;
			}
		} 
		catch (MatchFailed m) {
			return ambCluster;
		}
		catch (Throw t) {
			IValue exc = t.getException();

			if (exc.getType().isAbstractData() && ((IConstructor) exc).getConstructorType() == RuntimeExceptionFactory.CallFailed) {
				return ambCluster;
			}
			else {
				throw t;
			}
		}
		catch (Filtered f) {
			return null;
		}
	}

	@Override
	public ITree filterCycle(ITree cycle, Object environment) {
		return cycle;
	}

	@Override
	public ITree filterListAmbiguity(ITree ambCluster, Object environment) {
		return filterAmbiguity(ambCluster, environment);
	}

	@Override
	public ITree filterListCycle(ITree cycle, Object environment) {
		return cycle;
	}

	@Override
	public ITree filterListProduction(ITree tree, Object environment) {
		return tree;
	}

	@Override
	public ITree filterProduction(ITree tree, Object environment) {
		if (filters == null) {
			return tree;
		}

		try {
			ITree result = (ITree) filters.call(tree);

			if (!TreeAdapter.isAmb(result)) {
				return result;
			}
			else if (TreeAdapter.getAlternatives(result).size() == 1) {
				return (ITree) TreeAdapter.getAlternatives(result).iterator().next();
			}
			else {
				return result;
			}
		}
		catch (MatchFailed m) {	
			return tree;
		}
		catch (Throw t) {
			IValue exc = t.getException();

			if (exc.getType().isAbstractData() && ((IConstructor) exc).getConstructorType() == RuntimeExceptionFactory.CallFailed) {
				return tree;
			}
			else {
				throw t;
			}
		}
		catch (StaticError e) {
			throw e;
		}
		catch(Filtered f){
			return null;
		}
	}

	public boolean isImpure(Object rhs) {
		return !isPure;
	}
}
