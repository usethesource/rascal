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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.utils;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Expression.Anti;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.ast.Expression.List;
import org.rascalmpl.ast.Expression.MultiVariable;
import org.rascalmpl.ast.Expression.QualifiedName;
import org.rascalmpl.ast.Expression.TypedVariable;
import org.rascalmpl.ast.Expression.TypedVariableBecomes;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName.Default;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.semantics.dynamic.Expression.Set;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValueFactory;

public class IUPTRAstToSymbolConstructor extends NullASTVisitor<IConstructor> {
	
	static public class NonGroundSymbolException extends RuntimeException {
		private static final long serialVersionUID = 2430739406856140650L;
	}

	private IValueFactory vf;

	public IUPTRAstToSymbolConstructor(IValueFactory vf) {
		this.vf = vf;
	}
	
	@Override
	public IConstructor visitQualifiedNameDefault(Default x) {
		throw new NonGroundSymbolException();
	}
	
	@Override
	public IConstructor visitExpressionMultiVariable(MultiVariable x) {
		throw new NonGroundSymbolException();
	}
	
	@Override
	public IConstructor visitExpressionQualifiedName(QualifiedName x) {
		throw new NonGroundSymbolException();
	}
	
	@Override
	public IConstructor visitExpressionTypedVariable(TypedVariable x) {
		throw new NonGroundSymbolException();
	}
	
	@Override
	public IConstructor visitExpressionTypedVariableBecomes(
			TypedVariableBecomes x) {
		throw new NonGroundSymbolException();
	}

	@Override
	public IConstructor visitExpressionAnti(Anti x) {
		throw new NonGroundSymbolException();
	}
	
	@Override
	public IConstructor visitExpressionCallOrTree(CallOrTree x) {
		Expression namePart = x.getExpression();
		if (!namePart.isQualifiedName()) {
			throw new ImplementationError("weird AST");
		}
		String name = ((org.rascalmpl.semantics.dynamic.QualifiedName.Default) namePart.getQualifiedName()).lastName();
		
		if (name.equals("lit")) {
			StringConstant.Lexical arg = 
				(org.rascalmpl.ast.StringConstant.Lexical) x.getArguments().get(0).getLiteral().getStringLiteral().getConstant();
			// TODO: escaping etc.
			String str = arg.getString();
			str = str.substring(1, str.length() - 1);
			return vf.constructor(RascalValueFactory.Symbol_Lit, vf.string(str));
		}
		
		if (name.equals("cilit")) {
			StringConstant.Lexical arg = (org.rascalmpl.ast.StringConstant.Lexical) x.getArguments().get(0).getLiteral().getStringLiteral().getConstant();
			// TODO: escaping etc.
			String str = arg.getString();
			str = str.substring(1, str.length() - 1);
			return vf.constructor(RascalValueFactory.Symbol_Cilit, vf.string(str));
		}
		
		if (name.equals("empty")) {
			return vf.constructor(RascalValueFactory.Symbol_Empty);	
		}
		
		if (name.equals("seq")) {
			IList list = vf.list();
			Expression.List arg = (List) x.getArguments().get(0);
			for (Expression y: arg.getElements0()) {
				list = list.append(y.accept(this));
			}
			return vf.constructor(RascalValueFactory.Symbol_Seq, list);
			
		}
		
		if (name.equals("opt")) {
			IConstructor arg = x.getArguments().get(0).accept(this);
			return vf.constructor(RascalValueFactory.Symbol_Opt, arg);
		}
		
		if (name.equals("alt")) {
			ISet set = vf.set();
			Expression.Set arg = (Set) x.getArguments().get(0);
			for(Expression y: arg.getElements0()){
				set = set.insert(y.accept(this));
			}
			return vf.constructor(RascalValueFactory.Symbol_Alt, set);
		}
		
		if (name.equals("tuple")) {
			java.util.List<Expression> args = x.getArguments();
			IConstructor head = args.get(0).accept(this); 
			IList rest = vf.list();
			for (Expression arg: ((Expression.List)args.get(1)).getElements0()) {
				rest = rest.append(arg.accept(this));
			}
			return vf.constructor(RascalValueFactory.Symbol_Tuple, head, rest);
		}
		
		if (name.equals("sort")) {
			StringConstant.Lexical arg = (org.rascalmpl.ast.StringConstant.Lexical) 
				x.getArguments().get(0).getLiteral().getStringLiteral().getConstant();
			String str = arg.getString();
			str = str.substring(1, str.length() - 1);
			return vf.constructor(RascalValueFactory.Symbol_Sort, vf.string(str));
		}
		
		if (name.equals("layouts")) {
			StringConstant.Lexical arg = (org.rascalmpl.ast.StringConstant.Lexical) 
				x.getArguments().get(0).getLiteral().getStringLiteral().getConstant();
			String str = arg.getString();
			str = str.substring(1, str.length() - 1);
			return vf.constructor(RascalValueFactory.Symbol_Layouts, vf.string(str));
		}
		

		if (name.equals("iter")) {
			IConstructor arg = x.getArguments().get(0).accept(this);
			return vf.constructor(RascalValueFactory.Symbol_Iter, arg);
		}
		
		if (name.equals("iter-star")) {
			IConstructor arg = x.getArguments().get(0).accept(this);
			return vf.constructor(RascalValueFactory.Symbol_IterStar, arg);
		}
		
		if (name.equals("iter-star-seps")) {
			IConstructor arg = x.getArguments().get(0).accept(this);
			Expression.List args = (Expression.List) x.getArguments().get(1);
			IList seps = vf.list();
			for (Expression elem: args.getElements0()) {
				seps = seps.append(elem.accept(this));
			}
			return vf.constructor(RascalValueFactory.Symbol_IterStarSeps, arg, seps);
		}
		
		if (name.equals("iter-seps")) {
			IConstructor arg = x.getArguments().get(0).accept(this);
			Expression.List args = (Expression.List) x.getArguments().get(1);
			IList seps = vf.list();
			for (Expression elem: args.getElements0()) {
				seps = seps.append(elem.accept(this));
			}
			return vf.constructor(RascalValueFactory.Symbol_IterSeps, arg, seps);
		}

		if (name.equals("parameterized-sort")) {
			java.util.List<Expression> args = x.getArguments();
			StringConstant.Lexical sort = (org.rascalmpl.ast.StringConstant.Lexical) 
				x.getArguments().get(0).getLiteral().getStringLiteral().getConstant();
			IList rest = vf.list();
			for (Expression arg: ((Expression.List)args.get(1)).getElements0()) {
				rest = rest.append(arg.accept(this));
			}
			return vf.constructor(RascalValueFactory.Symbol_ParameterizedSort, vf.string(sort.getString()), rest);
			
		}
		
		if (name.equals("lit")) {
			StringConstant.Lexical arg = (org.rascalmpl.ast.StringConstant.Lexical) 
				x.getArguments().get(0).getLiteral().getStringLiteral().getConstant();
			// TODO: escaping etc.
			return vf.constructor(RascalValueFactory.Symbol_Lit, vf.string(arg.getString()));
		}
		
		if (name.equals("char-class")) {
			java.util.List<Expression> args = x.getArguments();
			IList ranges = vf.list();
			for (Expression arg: ((Expression.List)args.get(0)).getElements0()) {
				ranges = ranges.append(arg.accept(this));
			}
			return vf.constructor(RascalValueFactory.Symbol_CharClass, ranges);
		}
		
		if (name.equals("single")) {
			org.rascalmpl.ast.DecimalIntegerLiteral.Lexical arg1 = (org.rascalmpl.ast.DecimalIntegerLiteral.Lexical) x.getArguments().get(0).getLiteral().getIntegerLiteral().getDecimal();
			IInteger num = vf.integer(java.lang.Integer.parseInt(arg1.getString())); 
			return vf.constructor(RascalValueFactory.CharRange_Single, num);
		}
		
		if (name.equals("range")) {
			org.rascalmpl.ast.DecimalIntegerLiteral.Lexical arg1 = (org.rascalmpl.ast.DecimalIntegerLiteral.Lexical) x.getArguments().get(0).getLiteral().getIntegerLiteral().getDecimal();
			IInteger num1 = vf.integer(java.lang.Integer.parseInt(arg1.getString())); 
			org.rascalmpl.ast.DecimalIntegerLiteral.Lexical arg2 = (org.rascalmpl.ast.DecimalIntegerLiteral.Lexical) x.getArguments().get(1).getLiteral().getIntegerLiteral().getDecimal();
			IInteger num2 = vf.integer(java.lang.Integer.parseInt(arg2.getString())); 
			return vf.constructor(RascalValueFactory.CharRange_Range, num1, num2);
		}

		throw new ImplementationError("Non-IUPTR AST expression: " + name + ", " + x);
	}
}
