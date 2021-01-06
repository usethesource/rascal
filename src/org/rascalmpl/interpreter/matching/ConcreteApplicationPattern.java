/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.semantics.dynamic.Tree;
import org.rascalmpl.types.RascalType;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;

public class ConcreteApplicationPattern extends AbstractMatchingResult {
	private IList subjectArgs;
	private final IMatchingResult tupleMatcher;
	private IConstructor production;
	private final ITuple tupleSubject;
	private final Type myType;
	private boolean isLiteral;

	public ConcreteApplicationPattern(IEvaluatorContext ctx, Tree.Appl x, List<IMatchingResult> list) {
		super(ctx, x);
		
		// retrieve the static value of the production of this pattern
		this.production = x.getProduction();
		
		// use a tuple pattern to match the children of this pattern
		this.tupleMatcher = new TuplePattern(ctx, x, list);
		
		// this prototype can be used for every subject that comes through initMatch
		this.tupleSubject = new TreeAsTuple();
		
		// save the type of this tree
		this.myType = x.getConcreteSyntaxType();
	}
	
	public List<IVarPattern> getVariables() {
		return tupleMatcher.getVariables();
	}
	
	private class TreeAsTuple implements ITuple {
		
		@Override
		public int arity() {
			return subjectArgs.length();
		}

		@Override
		public IValue get(int i) throws IndexOutOfBoundsException {
			return subjectArgs.get(i);
		}

		@Override
		public Type getType() {
			Type[] fields = new Type[arity()];
			for (int i = 0; i < fields.length; i++) {
				fields[i] = get(i).getType();
			}
			return tf.tupleType(fields);
		}

		@Override
		public Iterator<IValue> iterator() {
			return new Iterator<IValue>() {
				int currentIndex = 0;

				public boolean hasNext() {
					return currentIndex < arity();
				}

				public IValue next() {
					return get(currentIndex++);
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public IValue get(String label) throws FactTypeUseException {
			throw new UnsupportedOperationException();
		}

		@Override
		public IValue select(int... fields) throws IndexOutOfBoundsException {
			throw new UnsupportedOperationException();
		}

		@Override
		public IValue selectByFieldNames(String... fields) throws FactTypeUseException {
			throw new UnsupportedOperationException();
		}

		@Override
		public ITuple set(int i, IValue arg) throws IndexOutOfBoundsException {
			throw new UnsupportedOperationException();
		}

		@Override
		public ITuple set(String label, IValue arg) throws FactTypeUseException {
			throw new UnsupportedOperationException();
		}
	}
	

	@Override
	public void initMatch(Result<IValue> subject) {
		hasNext = false;
		Type subjectType = subject.getValue().getType();
		super.initMatch(subject);

		if(subjectType.isExternalType() && ((RascalType) subjectType).isNonterminal() && subject.getValue() instanceof ITree) {
			org.rascalmpl.values.parsetrees.ITree treeSubject = (org.rascalmpl.values.parsetrees.ITree)subject.getValue();
		
			if (!TreeAdapter.isAppl(treeSubject)) {
				// fail early if the subject is an ambiguity cluster
				hasNext = false;
				return;
			}

			if (!TreeAdapter.getProduction(treeSubject).equals(production)) {
				// fail early if the subject's production is not the same
				hasNext = false;
				return;
			}
			
			if (!SymbolAdapter.isLiteral(ProductionAdapter.getType(production))) {
				this.subjectArgs = TreeAdapter.getNonLayoutArgs(treeSubject);
				tupleMatcher.initMatch(ResultFactory.makeResult(tupleSubject.getType(), tupleSubject, ctx));
			
				hasNext = tupleMatcher.hasNext();
				isLiteral = false;
			}
			else {
				isLiteral = true;
				hasNext = true;
			}
		}
	}
	
	@Override
	public boolean hasNext() {
		if (!isLiteral) {
			return tupleMatcher.hasNext();
		}
		
		return true;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		
		if (hasNext && !isLiteral) {
			return tupleMatcher.next();
		}
		else if (hasNext){
			hasNext = false;
			return true;
		}
		
		return false;
	}

	@Override
	public Type getType(Environment env,
			HashMap<String, IVarPattern> patternVars) {
		return myType;
	}
	
	@Override
	public String toString() {
	  return production.toString();
	}
	
}
