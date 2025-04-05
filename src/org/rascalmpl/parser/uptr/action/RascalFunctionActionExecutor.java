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
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.JavaMethod;
import org.rascalmpl.interpreter.result.OverloadedFunction;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.ISet;

/**
 * This is the way of executing filters for Rascal syntax definitions. 
 * A set of "filter" functions is provided which are called at every
 * level in the tree with as parameter the tree to be constructed.
 * If the filter functions do nothing, nothing happens. If they 
 * return a new tree, the old tree is substituted by the new tree.
 * If they throw the `filter` exception, then the branch up to
 * the first surrounding ambiguity cluster is filtered.
 */
public class RascalFunctionActionExecutor extends AbstractFunctionActionExecutor {
	private final boolean isPure;
	private @Nullable OverloadedFunction filters;

	public RascalFunctionActionExecutor(ISet functions, boolean isPure) {
		this.isPure = isPure;
		this.filters = constructFilterFunction(functions);
	}
	
	private OverloadedFunction constructFilterFunction(ISet functions) {
		List<AbstractFunction> alts = functions.stream()
			.map(v -> (AbstractFunction) v)
			.collect(Collectors.toList());
		
		return new OverloadedFunction("filters", alts);
	}

	public boolean isImpure(Object rhs) {
		return !isPure;
	}

	@Override
	protected ITree filter(ITree tree) {
		return filters.call(tree);
	}
}
