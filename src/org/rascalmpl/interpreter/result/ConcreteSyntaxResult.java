/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Bas Basten - Bas.Basten@cwi.nl (CWI)
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ConcreteSyntaxResult extends ConstructorResult {

	public ConcreteSyntaxResult(Type type, IConstructor cons,
			IEvaluatorContext ctx) {
		super(type, cons, ctx);
	}
	
	@Override
	public Result<IBool> is(Name name) {
		if (TreeAdapter.isAppl((ITree) getValue())) {
			String consName = TreeAdapter.getConstructorName((ITree) getValue());
			if (consName != null) {
				return ResultFactory.bool(Names.name(name).equals(consName), ctx);
			}
		}
		return ResultFactory.bool(false, ctx);
	}
	
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
		ITree tree = (ITree) getValue();
		
		if (TreeAdapter.isAppl(tree)) {
			int found = -1;
			IConstructor foundType = null;
			IConstructor prod = TreeAdapter.getProduction(tree);
			
			if (!ProductionAdapter.isRegular(prod)) {
				IList syms = ProductionAdapter.getSymbols(prod);

				// TODO: find deeper into optionals, checking the actual arguments for presence/absence of optional trees.
				for (int i = 0; i < syms.length(); i++) {
					IConstructor sym = (IConstructor) syms.get(i);
					
					while (SymbolAdapter.isConditional(sym)) {
						sym = SymbolAdapter.getSymbol(sym);
					}
					if (SymbolAdapter.isLabel(sym)) {
						if (SymbolAdapter.getLabel(sym).equals(name)) {
							found = i;
							foundType = SymbolAdapter.delabel(sym);
						}
					}
				}

				if (found != -1) {
					Type nont = RascalTypeFactory.getInstance().nonTerminalType(foundType);
					IValue child = TreeAdapter.getArgs(tree).get(found);
					return makeResult(nont, child, ctx);
				}
			}
		}
		
		return new ConstructorResult(RascalValueFactory.Tree, tree, ctx).fieldAccess(name, store);
//		if (tree.getConstructorType().hasField(name)) {
//			return makeResult(tree.getConstructorType().getFieldType(name), tree.get(name), ctx);
//		}
//		
//		throw RuntimeExceptionFactory.noSuchField(name, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(
			String name, Result<V> repl, TypeStore store) {
		ITree tree = (ITree) getValue();
		
		if (TreeAdapter.isAppl(tree)) {
			int found = -1;
			IConstructor foundType = null;
			IConstructor prod = TreeAdapter.getProduction(tree);
			IList syms = ProductionAdapter.getSymbols(prod);
			
			// TODO: find deeper into optionals, alternatives and sequences checking the actual arguments for presence/absence of optional trees.
			for (int i = 0; i < syms.length(); i++) {
				IConstructor sym = (IConstructor) syms.get(i);
				if (SymbolAdapter.isLabel(sym)) {
					if (SymbolAdapter.getLabel(sym).equals(name)) {
						found = i;
						foundType = SymbolAdapter.delabel(sym);
						break;
					}
				}
			}
			
			if (found != -1) {
				Type nont = RascalTypeFactory.getInstance().nonTerminalType(foundType);
				if (repl.getType().isSubtypeOf(nont)) {
					IList args = TreeAdapter.getArgs(tree).put(found, repl.getValue());
					return makeResult(getType(), tree.set("args", args), ctx);
				}
				throw new UnexpectedType(nont, repl.getType(), ctx.getCurrentAST());
			}
			
			if (RascalValueFactory.Tree_Appl.hasField(name)) {
				Type fieldType = RascalValueFactory.Tree_Appl.getFieldType(name);
				if (repl.getType().isSubtypeOf(fieldType)) {
					throw new UnsupportedOperation("changing " + name + " in concrete tree", ctx.getCurrentAST());
				}
				throw new UnexpectedType(fieldType, repl.getType(), ctx.getCurrentAST());
			}

			throw RuntimeExceptionFactory.noSuchField(name, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		throw new UnsupportedOperation("field update", ctx.getCurrentAST());
	}
	
	@Override
	public ITree getValue() {
		return (ITree) super.getValue();
	}
	
	@Override
	public Result<IBool> has(Name name) {
		if (TreeAdapter.isAppl((ITree) getValue())) {
			IConstructor prod = TreeAdapter.getProduction((ITree) getValue());
			if(ProductionAdapter.isDefault(prod)){
				IList syms = ProductionAdapter.getSymbols(prod);
				String tmp = Names.name(name);

				// TODO: find deeper into optionals, checking the actual arguments for presence/absence of optional trees.

				for (IValue sym : syms) {
					if (SymbolAdapter.isLabel((IConstructor) sym)) {
						if (SymbolAdapter.getLabel((IConstructor) sym).equals(tmp)) {
							return ResultFactory.bool(true, ctx);
						}
					}
				}
			}
		}
		return super.has(name);
	}
	
	@Override
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return that.equalToConcreteSyntax(this);
	}

	@Override
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) {
		return that.nonEqualToConcreteSyntax(this);
	}
	
	@Override
	protected Result<IBool> nonEqualToConcreteSyntax(
			ConcreteSyntaxResult that) {
		return equalToConcreteSyntax(that).negate();
	}
	
	@Override
	protected Result<IBool> equalToConcreteSyntax(ConcreteSyntaxResult that) {
		ITree left = this.getValue();
		ITree right = that.getValue();
		
		if (TreeAdapter.isLayout(left) && TreeAdapter.isLayout(right)) {
			return bool(true, ctx);
		}
		
		if (TreeAdapter.isAppl(left) && TreeAdapter.isAppl(right)) {
			IConstructor p1 = TreeAdapter.getProduction(left);
			IConstructor p2 = TreeAdapter.getProduction(right);
			
			if (!p1.isEqual(p2)) {
				return bool(false, ctx);
			}
			
			IList l1 = TreeAdapter.getArgs(left);
			IList l2 = TreeAdapter.getArgs(right);
			
			if (l1.length() != l2.length()) {
				return bool(false, ctx);
			}
			for (int i = 0; i < l1.length(); i++) {
				IValue kid1 = l1.get(i);
				IValue kid2 = l2.get(i);
				// Recurse here on kids to reuse layout handling etc.
				Result<IBool> result = makeResult(kid1.getType(), kid1, ctx).equals(makeResult(kid2.getType(), kid2, ctx));
				if (!result.getValue().getValue()) {
					return bool(false, ctx);
				}
				if (TreeAdapter.isContextFree(left)) {
					i++; // skip layout
				}
			}
			return bool(true, ctx);
		}
		
		
		if (TreeAdapter.isChar(left) && TreeAdapter.isChar(right)) {
			return bool((TreeAdapter.getCharacter(left) == TreeAdapter.getCharacter(right)), ctx);
		}
		
		if (TreeAdapter.isAmb(left) && TreeAdapter.isAmb(right)) {
			ISet alts1 = TreeAdapter.getAlternatives(left);
			ISet alts2 = TreeAdapter.getAlternatives(right);

			if (alts1.size() != alts2.size()) {
				return bool(false, ctx);
			}
			
			// TODO: this is very inefficient
			again: for (IValue alt1: alts1) {
				for (IValue alt2: alts2) {
					Result<IBool> result = makeResult(alt1.getType(), alt1, ctx).equals(makeResult(alt2.getType(), alt2, ctx));
					if (result.getValue().getValue()) {
						// As soon an alt1 is equal to an alt2
						// continue the outer loop.
						continue again;
					}
				}
				// If an alt1 is not equal to any of the the alt2's return false;
				return bool(false, ctx);
			}
			return bool(true, ctx);
		}

		return bool(false, ctx);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <U extends IValue> Result<U> addString(StringResult that) {
		// Note the reverse concat.
		return (Result<U>) new ConcatStringResult(getType(), that, 
				new StringResult(that.getType(),ctx.getValueFactory().string(TreeAdapter.yield(getValue())), ctx), ctx);
	}

	@Override
	public String toString() {
	  return super.toString() + "\n"  + TreeAdapter.yield(getValue());
	}
}
