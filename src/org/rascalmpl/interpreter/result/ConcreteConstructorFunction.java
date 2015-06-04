/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.Collections;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ConcreteConstructorFunction extends ConstructorFunction {

	public ConcreteConstructorFunction(AbstractAST ast, Type constructorType, IEvaluator<Result<IValue>> eval, Environment env) {
		super(ast, eval, env, constructorType, Collections.<KeywordFormal>emptyList());
	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals, Map<String, IValue> keyArgValues) {
		if (constructorType == RascalValueFactory.Tree_Appl) {
			IConstructor prod = (IConstructor) actuals[0];
			IList args = (IList) actuals[1];

			if (ProductionAdapter.isList(prod)) {
				actuals[1] = flatten(prod, args);
			}
		}

		IConstructor newAppl = getValueFactory().constructor(constructorType, actuals);

		NonTerminalType concreteType = (NonTerminalType) RascalTypeFactory.getInstance().nonTerminalType(newAppl);

		return makeResult(concreteType, newAppl, ctx);
	}

	private IValue flatten(IConstructor prod, IList args) {
		IListWriter result = vf.listWriter();
		int delta = getDelta(prod);
		
		for (int i = 0; i < args.length(); i+=(delta + 1)) {
			ITree tree = (ITree) args.get(i);
			if (TreeAdapter.isList(tree) && TreeAdapter.isAppl(tree)) {
				if (ProductionAdapter.shouldFlatten(prod, TreeAdapter.getProduction(tree))) {
					IList nestedArgs = TreeAdapter.getArgs(tree);
					if (nestedArgs.length() > 0) {
						appendSeparators(args, result, delta, i);
						result.appendAll(nestedArgs);
					}
					else {
						// skip following separators
						i += delta;
					}
				}
				else {
					appendSeparators(args, result, delta, i);
					result.append(tree);
				}
			}
			else {
				appendSeparators(args, result, delta, i);
				result.append(tree);
			}
		}
		
		return result.done();
	}

	private void appendSeparators(IList args, IListWriter result, int delta, int i) {
		for (int j = i - delta; j > 0 && j < i; j++) {
			result.append(args.get(j));
		}
	}

	

	private int getDelta(IConstructor prod) {
		IConstructor rhs = ProductionAdapter.getType(prod);
		
		if (SymbolAdapter.isIterPlusSeps(rhs) || SymbolAdapter.isIterStarSeps(rhs)) {
			return SymbolAdapter.getSeparators(rhs).length();
		}
		
		return 0;
	}
}
