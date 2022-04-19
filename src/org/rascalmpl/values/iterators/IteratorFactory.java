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
package org.rascalmpl.values.iterators;

import java.util.Iterator;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.NotEnumerable;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.TypeReachability;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class IteratorFactory {
	
	public static Type elementType(IEvaluatorContext ctx, Result<IValue> subject) {
		Type subjectType = subject.getStaticUnaliasedType();

		if (subjectType.isList() || subjectType.isSet()) {
			return subjectType.getElementType();
		} 
		else if (subjectType.isMap()){				
			return subjectType.getKeyType();
		} 
		else if (subjectType.isExternalType()){
			if (subjectType instanceof NonTerminalType){
				NonTerminalType nt = (NonTerminalType) subjectType;

				if (nt.isConcreteListType() || nt.isOptionalType()){
					IConstructor listSymbol = nt.getSymbol();
					return RascalTypeFactory.getInstance().nonTerminalType(SymbolAdapter.getSymbol(listSymbol));
				}
			}

			throw new NotEnumerable(subjectType.toString(), ctx.getCurrentAST());
		} 
		else if (subjectType.isNode() || subjectType.isAbstractData() || subjectType.isTuple()) {
			return TypeFactory.getInstance().valueType();			
		}
		
		throw new NotEnumerable(subjectType.toString(), ctx.getCurrentAST());
	}

	public static Iterator<IValue> make(IEvaluatorContext ctx, IMatchingResult matchPattern, 
			Result<IValue> subject, boolean shallow){

		Type subjectType = subject.getStaticUnaliasedType();
		IValue subjectValue = subject.getValue();
		Type patType = matchPattern.getType(ctx.getCurrentEnvt(), null);

		if (subjectType.isTop()) {
			System.err.println("???");
		}
		// TODO: this should be a visitor design as well..

		// List
		if(subjectType.isList()){
			//TODO: we could do this more precisely				
			if(shallow){
			    checkMayOccur(patType, subjectType.getElementType(), ctx);
				return ((IList) subjectValue).iterator();
			}
			return new DescendantReader(subjectValue, false);

			// Set
		} else 	if(subjectType.isSet()){				
			if (shallow){
				checkMayOccur(patType, subjectType.getElementType(), ctx);
				return ((ISet) subjectValue).iterator();
			}
			
			return new DescendantReader(subjectValue, false);

			// Map
		} else if(subjectType.isMap()){				
			if (shallow) {
				checkMayOccur(patType, subjectType.getKeyType(), ctx);
				return ((IMap) subjectValue).iterator();
			}
			return new DescendantReader(subjectValue, false);
		} else if (subjectType.isExternalType()) {
			if (subjectType instanceof NonTerminalType) {
				// NonTerminal (both pattern and subject are non-terminals, so we can skip layout and stuff)
				ITree tree = (ITree) subjectValue;
				NonTerminalType nt = (NonTerminalType) subjectType;

				if (!shallow) {
					return new DescendantReader(tree, patType instanceof NonTerminalType);
				}
				else {
					if (nt.isConcreteListType()){
						checkMayOccur(patType, subjectType, ctx);
						IConstructor ls = nt.getSymbol();

						int delta = SymbolAdapter.isSepList(ls) ? 
								(SymbolAdapter.getSeparators(ls).length() + 1) : 1;

						return new CFListIterator(TreeAdapter.getArgs(tree), delta);
					}
					else if (nt.isOptionalType()) {
						checkMayOccur(patType, subjectType, ctx);
						return new CFListIterator(TreeAdapter.getArgs(tree), 1);
					}
				}
			}

			throw new NotEnumerable(subjectType.toString(), ctx.getCurrentAST());
			// Node and ADT
		} else if(subjectType.isNode() || subjectType.isAbstractData()){			
			if (shallow){
				if(subjectType.isAbstractData()) 
					checkMayOccur(patType, subjectType, ctx);
				return new NodeChildIterator((INode) subjectValue);
			}
			return new DescendantReader(subjectValue, false);

		} else if(subjectType.isTuple()){
			if(shallow){
				Type lub = TypeFactory.getInstance().voidType();
				int nElems = subjectType.getArity();
				for(int i = 0; i < nElems; i++)
					lub = lub.lub(subjectType.getFieldType(i));
				if(!lub.comparable(patType))
					throw new UnexpectedType(patType, subjectType, ctx.getCurrentAST());	
				return new TupleElementIterator((ITuple)subjectValue);
			}
			return new DescendantReader(subjectValue, false);

		} else if(subjectType.isBool() ||
				subjectType.isInteger() ||
				subjectType.isReal() ||
				subjectType.isString() ||
				subjectType.isSourceLocation() ||
				subjectType.isRational() ||
				subjectType.isDateTime())
		{
			if (shallow) {
				throw new NotEnumerable(subjectType.toString(), ctx.getCurrentAST());
			}
			return new SingleIValueIterator(subjectValue);
		} else {
			throw new UnsupportedOperation("makeIterator", subjectType, ctx.getCurrentAST());
		}
	}

	private static void checkMayOccur(Type patType, Type rType, IEvaluatorContext ctx){
		if(!TypeReachability.mayOccurIn(patType, rType, ctx.getCurrentEnvt())) {
			throw new UnexpectedType(rType, patType, ctx.getCurrentAST());
		}
	}

}
