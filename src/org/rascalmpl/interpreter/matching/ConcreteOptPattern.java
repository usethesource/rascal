/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.semantics.dynamic.Tree;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalTypeFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

public class ConcreteOptPattern extends AbstractMatchingResult {
	private enum Opt { Exist, NotExist, MayExist }
	private final Opt type;
	private final IConstructor production;
	private final IMatchingResult optArg;

	public ConcreteOptPattern(IEvaluatorContext ctx, Tree.Appl x, List<IMatchingResult> list) {
		super(ctx, x);
		
		// retrieve the static value of the production of this pattern
		this.production = x.getProduction();
		
		if (list.size() == 0) {
			type = Opt.NotExist;
			optArg = null;
		}
		else if (list.size() == 1) {
			optArg = list.get(0);
			NonTerminalType nont = (NonTerminalType) optArg.getType(ctx.getCurrentEnvt(), null);
			if (SymbolAdapter.isOpt(nont.getSymbol())) {
				// I think this can only happen when a variable of type opt is there
				type = Opt.MayExist;
			}
			else {
				type = Opt.Exist;
			}
		}
		else {
			throw new ImplementationError("optional with more than one element???", x.getLocation());
		}
	}

	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		
		if (!subject.getStaticType().isSubtypeOf(RascalValueFactory.Tree)) {
			hasNext = false;
			return;
		}
		org.rascalmpl.values.parsetrees.ITree tree = (org.rascalmpl.values.parsetrees.ITree) subject.getValue();
		
		if (tree.getConstructorType() != RascalValueFactory.Tree_Appl) {
			hasNext = false;
			return;
		}
		
		IConstructor prod = TreeAdapter.getProduction(tree);
		
		if (!prod.equals(production)) {
			hasNext = false;
			return;
		}
		
		IList args = TreeAdapter.getArgs(tree);
		
		switch (type) {
		case MayExist:
			optArg.initMatch(subject);
			hasNext = optArg.hasNext();
			return;
		case Exist:
			if (args.length() == 1) {
				IConstructor arg = (IConstructor) args.get(0);
				Type argType = RascalTypeFactory.getInstance().nonTerminalType(arg);
				Result<IValue> argResult = ResultFactory.makeResult(argType, arg, ctx);
				optArg.initMatch(argResult);
				hasNext = optArg.hasNext();
				return;
			}
			
			hasNext = false;
			return;
		case NotExist:
			hasNext = args.length() == 0;
			return;
		}
		
	}
	
	@Override
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		return RascalTypeFactory.getInstance().nonTerminalType(ProductionAdapter.getType(production));
	}

	@Override
	public boolean hasNext() {
		if (!hasNext) {
			return false;
		}
		
		switch (type) {
		case MayExist:
			return optArg.hasNext();
		case Exist:
			return optArg.hasNext();
		case NotExist:
			return true;
		default:
			return false;
		}
	}
	
	@Override
	public boolean next() {
		if (!hasNext()) {
			return false;
		}
		
		if (optArg != null) {
			return optArg.next();
		}
		
		hasNext = false;
		return true;
	}

	@Override
	public List<IVarPattern> getVariables() {
		if (optArg != null) {
			return optArg.getVariables();
		}
		
		return Collections.emptyList();
	}
}
