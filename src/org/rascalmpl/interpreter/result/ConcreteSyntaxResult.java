/*******************************************************************************
 * Copyright (c) 2009-2018 CWI, NWO-I CWI
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
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter.FieldResult;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

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
	
	@SuppressWarnings("unchecked")
    @Override
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
	    ITree t = (ITree) getValue();
	    if (TreeAdapter.isList(t)) {
	        return (Result<U>) new ListResult(getTypeFactory().listType(RascalValueFactory.Tree), TreeAdapter.getListASTArgs(t), ctx).subscript(subscripts);
	    }
	    else {
	        return (Result<U>) new ListResult(getTypeFactory().listType(RascalValueFactory.Tree), TreeAdapter.getASTArgs(t), ctx).subscript(subscripts);
	    }
	}
	
	@Override
	public <U extends IValue> Result<U> fieldAccess(String name, TypeStore store) {
		ITree tree = (ITree) getValue();
		
		FieldResult field = TreeAdapter.getLabeledField(tree, name); 
		
		if (field != null) {
		    Type symbolType = RascalTypeFactory.getInstance().nonTerminalType(field.symbol);
		    return makeResult(symbolType, field.tree, ctx);
		}
		
		return new ConstructorResult(RascalValueFactory.Tree, tree, ctx).fieldAccess(name, store);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> fieldUpdate(String name, Result<V> repl, TypeStore store) {
		ITree tree = (ITree) getValue();
		FieldResult field = TreeAdapter.getLabeledField(tree, name);
		
		if (field != null) {
		    Type symbolType = RascalTypeFactory.getInstance().nonTerminalType(field.symbol);
		    
		    if (!repl.getStaticType().isSubtypeOf(symbolType)) {
		        throw new UnexpectedType(symbolType, repl.getStaticType(), ctx.getCurrentAST()); 
		    }
		    
		    ITree result = TreeAdapter.putLabeledField(tree, name, (ITree) repl.getValue());
		    
		    if (result != null) {
	            return makeResult(result.getType(), result, ctx);
	        }
		}
		
		if (TreeAdapter.isAppl(tree)) {
			if (RascalValueFactory.Tree_Appl.hasField(name)) {
				Type fieldType = RascalValueFactory.Tree_Appl.getFieldType(name);
				if (repl.getStaticType().isSubtypeOf(fieldType)) {
					throw new UnsupportedOperation("changing " + name + " in concrete tree", ctx.getCurrentAST());
				}
				throw new UnexpectedType(fieldType, repl.getStaticType(), ctx.getCurrentAST());
			}
			
			if (ctx.getCurrentEnvt().getStore().getKeywordParameterType(RascalValueFactory.Tree, name) != null) {
			    if (getValue().mayHaveKeywordParameters()) {
			        return makeResult(getStaticType(), getValue().asWithKeywordParameters().setParameter(name, repl.getValue()), ctx);
			    }
			    else {
			        throw RuntimeExceptionFactory.illegalArgument(getValueFactory().string("Can not set a keyword parameter on a tree which already has annotations"), ctx.getCurrentAST(), ctx.getStackTrace());
			    }
			}

			throw RuntimeExceptionFactory.noSuchField(name, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		return super.fieldUpdate(name, repl, store);
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
		IConstructor leftCons = this.getValue();
		IConstructor rightCons = that.getValue();
		
		if ((leftCons.mayHaveKeywordParameters() && !rightCons.mayHaveKeywordParameters())
		        || (!leftCons.mayHaveKeywordParameters() && rightCons.mayHaveKeywordParameters())) {
		    return bool(false, ctx);
		}
		
		if (leftCons.mayHaveKeywordParameters() && rightCons.mayHaveKeywordParameters()) {
		    if (!leftCons.asWithKeywordParameters().equalParameters(rightCons.asWithKeywordParameters())) {
		        return bool(false, ctx);
		    }
		}
		    
		ITree left = getTreeWithoutKeywordParameters(leftCons);
		ITree right = getTreeWithoutKeywordParameters(rightCons);
		
		if (TreeAdapter.isLayout(left) && TreeAdapter.isLayout(right)) {
			return bool(true, ctx);
		}
		
		if (TreeAdapter.isAppl(left) && TreeAdapter.isAppl(right)) {
			IConstructor p1 = TreeAdapter.getProduction(left);
			IConstructor p2 = TreeAdapter.getProduction(right);
			
			// TODO: max-sharing productions would reduce this to reference equality
			if (!p1.equals(p2)) {
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

    private ITree getTreeWithoutKeywordParameters(IConstructor leftCons) {
        return (ITree) (leftCons.mayHaveKeywordParameters() && !leftCons.asWithKeywordParameters().getParameters().isEmpty() ? leftCons.asWithKeywordParameters().unsetAll() : leftCons);
    }
	
	@Override
	protected <U extends IValue> Result<U> addString(StringResult that) {
        // Note the reverse concat.
	    return makeResult(that.getStaticType(), that.getValue().concat(ctx.getValueFactory().string(TreeAdapter.yield(getValue()))), ctx);
	}

	@Override
	public String toString() {
	  return super.toString() + "\n"  + TreeAdapter.yield(getValue());
	}
}
