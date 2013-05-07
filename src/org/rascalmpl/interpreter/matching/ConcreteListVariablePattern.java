/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariable;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ConcreteListVariablePattern extends AbstractMatchingResult implements IVarPattern {
	private String name;
	private NonTerminalType declaredType;
	
	private boolean anonymous = false;
	private boolean debug = false;
	private boolean iDeclaredItMyself;
	
	public ConcreteListVariablePattern(IEvaluatorContext ctx, AbstractAST x,
			org.eclipse.imp.pdb.facts.type.Type type, org.rascalmpl.ast.Name name) {
		super(ctx, x);
		this.name = Names.name(name);
		this.declaredType = (NonTerminalType) type;
		this.anonymous = Names.name(name).equals("_");
		this.iDeclaredItMyself = false;
		//System.err.println("ConcreteListVariablePattern");
	}

	public ConcreteListVariablePattern(IEvaluatorContext ctx, AbstractAST x,
			org.eclipse.imp.pdb.facts.type.Type type, String name) {
		super(ctx, x);
		this.name = name;
		this.declaredType = (NonTerminalType) type;
		this.anonymous = name.equals("_");
		this.iDeclaredItMyself = false;
		//System.err.println("ConcreteListVariablePattern");
	}
	
	@Override
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		return declaredType;
	}

	@Override
	public List<IVarPattern> getVariables() {
		java.util.LinkedList<IVarPattern> res = new java.util.LinkedList<IVarPattern>();
		res.addFirst(this);
		return res;
	}

	public String getName() {
		return name;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	@Override
	public boolean next() {
		if (debug) {
			System.err.println("AbstractConcreteSyntaxListVariable.next");
		}
		checkInitialized();
		if (!hasNext)
			return false;
		hasNext = false;
		
		
		if (debug) {
			System.err.println("Subject: " + subject + " name: " + name
					+ " getType: ");
			
			System.err.println("AbstractConcreteSyntaxListVariable.next: " + subject
					+ "(type=" + subject.getType() + ") with " + declaredType
					+ " " + name);
		}
	
		if (!anonymous && !iDeclaredItMyself && !ctx.getCurrentEnvt().declareVariable(declaredType, name)) {
			throw new RedeclaredVariable(name, ctx.getCurrentAST());
		}
		
		iDeclaredItMyself = true;
		
		if (subject.getType().isSubtypeOf(Factory.Args)) {
			if (((IList)subject.getValue()).isEmpty()) {
				IConstructor sym =declaredType.getSymbol();
				if (SymbolAdapter.isIterPlus(sym) || SymbolAdapter.isIterPlusSeps(sym)) {
					return false;
				}
			}
			if (!anonymous)
				ctx.getCurrentEnvt().storeVariable(name, makeResult(declaredType,
						wrapWithListProd(subject.getValue()), ctx));
			if (debug) {
				System.err.println("matches");
			}
			return true;
		}
		
		IConstructor subjectTree = (IConstructor) subject.getValue();
		if (TreeAdapter.isList(subjectTree)) {
			if ((TreeAdapter.getArgs(subjectTree)).isEmpty()) {
				IConstructor sym = declaredType.getSymbol();
				if (SymbolAdapter.isIterPlus(sym)  || (SymbolAdapter.isIterPlusSeps(sym))) {
					return false;
				}
			}
			
			// This code makes sure that the call is allowed. It verifies first that either the declared type
			// is *, with the subject then either + or *; or that the declared type is +, with the subject
			// then forced to be +. Second, it checks that both either use, or do not use, separators, so
			// lists where one does and one does not use separators will not match. Finally, it makes sure
			// both use the same iterated symbol.
			IConstructor subjectListType = TreeAdapter.getType(subjectTree);
			IConstructor declaredListType = declaredType.getSymbol();
			if ( (SymbolAdapter.isStarList(declaredListType) && SymbolAdapter.isAnyList(subjectListType)) || (SymbolAdapter.isPlusList(declaredListType) && SymbolAdapter.isPlusList(subjectListType))) {
				if (SymbolAdapter.isSepList(declaredListType) == SymbolAdapter.isSepList(subjectListType)) {
					if (SymbolAdapter.getSymbol(subjectListType).equals(SymbolAdapter.getSymbol(declaredListType))) {
						ctx.getCurrentEnvt().storeVariable(name, ResultFactory.makeResult(declaredType, subject.getValue(), ctx));
					}
				}
			}
			
//			if (ProductionAdapter.getRhs(TreeAdapter.getProduction(subjectTree)).isEqual(declaredType.getSymbol())) {
//				ctx.getCurrentEnvt().storeVariable(name, ResultFactory.makeResult(declaredType, subject.getValue(), ctx));
//			}
			if (debug)
				System.err.println("matches");
			return true;
		}
		
// 		if (debug)
//			System.err.println("no match");
//		 return false;
		return true;
	}


	private IValue wrapWithListProd(IValue subject) {
		IList args = (IList) subject;
		IValue prod = ctx.getValueFactory().constructor(Factory.Production_Regular, declaredType.getSymbol());
		
		if (args.length() == 1) {
			IConstructor arg = (IConstructor) args.get(0);
			
			if (TreeAdapter.isList(arg) && TreeAdapter.getProduction(arg).isEqual(prod)) {
				return arg;
			}
		}
		
		return ctx.getValueFactory().constructor(Factory.Tree_Appl, prod, subject);
	}

	@Override
	public String toString() {
		return declaredType + " " + name + ":=" + subject;
	}

	@Override
	public boolean isVarIntroducing() {
		return iDeclaredItMyself;
	}

	@Override
	public String name() {
		return getName();
	}

	@Override
	public Type getType() {
		return declaredType;
	}
}
