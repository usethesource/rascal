/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;

public class SetPattern extends AbstractMatchingResult {
	private List<IMatchingResult> patternChildren; // The elements of the set pattern
	private int patternSize;					// Number of elements in the set pattern
	private ISet setSubject;					// Current subject	
	private Type setSubjectType;				// Type of the subject
	private Type setSubjectElementType;		    // Type of the elements of current subject

	private ISet fixedSetElements;				// The fixed, non-variable elements in the pattern
	private ISet availableSetElements;			// The elements in the subject that are available:
												// = setSubject - fixedSetElements
	/*
	 * The variables are indexed from 0, ..., nVar-1 in the order in which they occur in the pattern.
	 * There are three kinds:
	 * - a list variable
	 * - an element variable
	 * - a non-literal pattern that contains variables
	 */
	private int nVar;							// Number of variables
	private HashSet<String> patVars;            // List of names of variables at top-level of the pattern
	private HashSet<String> allVars;			// List of names of all the variables in the pattern 
												// (including nested subpatterns)
	private String[] varName;					// Name of each variable
	private ISet[] varVal;						// Value of each variable
	private IMatchingResult[] varPat;			// The pattern value for non-literal patterns
	private boolean[] isSetVar;				    // Is this a set variables?			
	private Iterator<?>[] varGen;				// Value generator for this variables
	
	private int currentVar;					    // The currently matched variable
    private boolean firstMatch;				    // First match of this pattern?
	
	private boolean debug = false;
	private Type staticSetSubjectType;
	private Type staticSubjectElementType;
	
	public SetPattern(IEvaluatorContext ctx, Expression x, List<IMatchingResult> list){
		super(ctx, x);
		this.patternChildren = list;
		this.patternSize = list.size();
	}
	
	@Override
	public Type getType(Environment env) {
		if(patternSize == 0){
			return tf.setType(tf.voidType());
		}
		
		Type elemType = tf.voidType();
		for(int i = 0; i < patternSize; i++){
			Type childType = patternChildren.get(i).getType(env);
			if(childType.isSetType()){
				elemType = elemType.lub(childType.getElementType());
			} else {
				elemType = elemType.lub(childType);
			}
		}
		return tf.setType(elemType);
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String> ();
		for (int i = 0; i < patternChildren.size(); i++) {
			res.addAll(patternChildren.get(i).getVariables());
		 }
		return res;
	}
	
	// Sort the variables: element variables and non-literal patterns should 
	// go before list variables since only set variables may be empty.
	
	private void sortVars(){
		String[] newVarName = new String[patternSize];
		ISet[]newVarVal= new ISet[patternSize];
		IMatchingResult[] newVarPat = new IMatchingResult[patternSize];
		boolean[] newIsSetVar = new boolean[patternSize];
		
		int nw = 0;
		for(int i = 0; i < nVar; i++){
			if(!isSetVar[i]){
				newVarName[nw] = varName[i];
				newVarVal[nw] = varVal[i];
				newVarPat[nw] = varPat[i];
				newIsSetVar[nw] = isSetVar[i];
				nw++;
			}
		}
		for(int i = 0; i < nVar; i++){
			if(isSetVar[i]){
				newVarName[nw] = varName[i];
				newVarVal[nw] = varVal[i];
				newVarPat[nw] = varPat[i];
				newIsSetVar[nw] = isSetVar[i];
				nw++;
			}
		}
		
		assert nw == nVar;
		for(int i = 0; i < nVar; i++){
			varName[i] = newVarName[i];
			varVal[i] = newVarVal[i];
			varPat[i] = newVarPat[i];
			isSetVar[i] = newIsSetVar[i];
		}
	}
	
	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		
		if (!subject.getValue().getType().isSetType()) {
			hasNext = false;
			return;
		}
		
		setSubject = (ISet) subject.getValue();
		setSubjectType = setSubject.getType(); // have to use static type here
		staticSetSubjectType = subject.getType();
		setSubjectElementType = setSubject.getElementType();
		staticSubjectElementType = staticSetSubjectType.isSetType() ? staticSetSubjectType.getElementType() : tf.valueType();
		Environment env = ctx.getCurrentEnvt();
		fixedSetElements = ctx.getValueFactory().set(getType(env).getElementType());
		
		nVar = 0;
		patVars = new HashSet<String>();
		allVars = new HashSet<String>();
		varName = new String[patternSize];  			// Some overestimations
		isSetVar = new boolean[patternSize];
		varVal = new ISet[patternSize];
		varPat = new IMatchingResult[patternSize];
		varGen = new Iterator<?>[patternSize];
		/*
		 * Pass #1: determine the (ordinary and set) variables in the pattern
		 */
		for(int i = 0; i < patternSize; i++){
			IMatchingResult child = patternChildren.get(i);
			if(child instanceof TypedVariablePattern){
				TypedVariablePattern patVar = (TypedVariablePattern) child;
				Type childType = child.getType(env);
				String name = ((TypedVariablePattern)child).getName();
				if(!patVar.isAnonymous() && allVars.contains(name)){
					throw new RedeclaredVariableError(name, getAST());
				}
				if(childType.comparable(staticSetSubjectType) || childType.comparable(staticSubjectElementType)){
					/*
					 * An explicitly declared set or element variable.
					 */
					if(!patVar.isAnonymous()){
						patVars.add(name);
						allVars.add(name);
					}
					varName[nVar] = name;
					varPat[nVar] = child;
					isSetVar[nVar] = childType.isSetType();
					nVar++;
				} else {
					hasNext = false;
					return;
					// you cant do this in the context of a visit, because you might actually visit another set!
//					throw new UnexpectedTypeError(setSubject.getType(), childType, getAST());
				}
				
			} else if(child instanceof MultiVariablePattern){
				MultiVariablePattern multiVar = (MultiVariablePattern) child;
				String name = multiVar.getName();
				if(!multiVar.isAnonymous() && allVars.contains(name)){
					throw new RedeclaredVariableError(name, getAST());
				}
				varName[nVar] = name;
				varPat[nVar] = child;
				isSetVar[nVar] = true;
				nVar++;
			} else if(child instanceof QualifiedNamePattern){
				QualifiedNamePattern qualName = (QualifiedNamePattern) child;
				String name = qualName.getName();
				if (!qualName.isAnonymous() && allVars.contains(name)) {
					/*
					 * A set/element variable that was declared earlier in the pattern itself,
					 * or in a preceding nested pattern element.
					 */
					if(!patVars.contains(name)){
						/*
						 * It occurred in an earlier nested subpattern.
						 */
						varName[nVar] = name;
						varPat[nVar] = child;
						isSetVar[nVar] = true; //TODO: childType.isSetType();
						nVar++;
					} else {
						/*
						 * Ignore it (we are dealing with sets, remember).
						 */
					}
				} else if(qualName.isAnonymous()){
					varName[nVar] = name;
					varPat[nVar] = child;
					isSetVar[nVar] = false;
					nVar++;
				} else  {
					Result<IValue> varRes = env.getVariable(name);
					
					if(varRes == null || qualName.bindingInstance()){
						// Completely new variable
						varName[nVar] = name;
						varPat[nVar] = child;
						isSetVar[nVar] = false;
						nVar++;
						// TODO: Why is this here? The pattern also declares the variable,
						// so this just causes errors when we use variables in set patterns.
						// env.declareVariable(staticSubjectElementType, name);
					} else {
					    if(varRes.getValue() != null){
					        Type varType = varRes.getType();
					        if (varType.comparable(staticSetSubjectType)){
					        	/*
					        	 * A set variable declared in the current scope: add its elements
					        	 */
					        	fixedSetElements = fixedSetElements.union((ISet)varRes.getValue());
					        } else if(varType.comparable(staticSubjectElementType)){
					        	/*
					        	 * An element variable in the current scope, add its value.
					        	 */
					        	fixedSetElements = fixedSetElements.insert(varRes.getValue());
					        } else {
					        	hasNext = false; // cant throw type error 
					        	return;
//					        	throw new UnexpectedTypeError(staticSetSubjectType,varType, getAST());
					        }
					    } 
					    else {
					    	// JURGEN added this to support pre-declared list variables
					    
					    	if(varRes.getType().comparable(staticSetSubjectType) || varRes.getType().comparable(staticSubjectElementType)){
								/*
								 * An explicitly declared set or element variable.
								 */
								if(!name.equals("_")){
									patVars.add(name);
									allVars.add(name);
								}
								varName[nVar] = name;
								varPat[nVar] = child;
								isSetVar[nVar] = varRes.getType().isSetType();
								nVar++;
					    	}
					    }
				    }
				}
			} else if(child instanceof LiteralPattern){
				IValue lit = ((LiteralPattern) child).toIValue(env);
				Type childType = child.getType(env);
				if(!childType.comparable(staticSubjectElementType)){
					throw new UnexpectedTypeError(setSubject.getType(), childType, getAST());
				}
				fixedSetElements = fixedSetElements.insert(lit);
			} else {
				Type childType = child.getType(env);
				if(!childType.comparable(staticSubjectElementType)){
					hasNext = false;
					return;
//					throw new UnexpectedTypeError(setSubject.getType(), childType, getAST());
				}
				java.util.List<String> childVars = child.getVariables();
				if(!childVars.isEmpty()){ 
					allVars.addAll(childVars);
					varName[nVar] = child.toString();
					varPat[nVar] = child;
					isSetVar[nVar] = false;
					nVar++;
				} else {
					// TODO: this should check for isConstant or something, which includes a check for anti patterns and deep patterns
					fixedSetElements = fixedSetElements.insert(child.toIValue());
					// TODO: Paul, not all other patterns have to be fixedElements? right?
				}
			} 
		}
		/*
		 * Pass #2: set up subset generation
		 */
		firstMatch = true;
		hasNext = fixedSetElements.isSubsetOf(setSubject);
		availableSetElements = setSubject.subtract(fixedSetElements);
		sortVars();
	}
	
	@Override
	public boolean hasNext(){
		return initialized && hasNext;
	}
	
	private ISet available(){
		ISet avail = availableSetElements;
		for(int j = 0; j < currentVar; j++){
			avail = avail.subtract(varVal[j]);
		}
		return avail;
	}
	
	private boolean makeGen(int i, ISet elements) {
		Environment env = ctx.getCurrentEnvt();
		
		if(varPat[i] instanceof QualifiedNamePattern){
			QualifiedNamePattern qualName = (QualifiedNamePattern) varPat[i];
			String name = qualName.getName();
			if(qualName.isAnonymous()){
				varGen[i] = new SingleElementIterator(elements, ctx);
			} else if(env.getVariable(name) == null){
				varGen[i] = new SingleElementIterator(elements, ctx);
			} else {
				varGen[i] = new SingleIValueIterator(env.getVariable(name).getValue());
			}
		}
		if(isSetVar[i]){
			varGen[i] = new SubSetGenerator(elements, ctx);
		} else {
			if(elements.size() == 0)
				return false;
			varGen[i] = new SingleElementIterator(elements, ctx);
		}
		return true;
	}
	
	private boolean matchVar(int i, ISet elements){
		varVal[i] = elements;
		IValue elem ;
		if(isSetVar[i]){
			elem = elements;
		} else {
			assert elements.size() == 1;
			elem = elements.iterator().next();
		}
		
		// TODO: see if we can use a static ttype here?!
		varPat[i].initMatch(ResultFactory.makeResult(elem.getType(), elem, ctx));
		return varPat[i].next();
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		
		if(!hasNext)
			return false;
		
		if(firstMatch){
			firstMatch = hasNext = false;
			if(nVar == 0){
				return fixedSetElements.isEqual(setSubject);
			}
			if(!fixedSetElements.isSubsetOf(setSubject)){
				return false;
			}
			
			if(nVar == 1){
				if(isSetVar[0] || availableSetElements.size() == 1){
					return matchVar(0, availableSetElements);
				}
				return false;
			}
			
			currentVar = 0;
			if(!makeGen(currentVar, availableSetElements)){
				return false;
			}
		} else {
			currentVar = nVar - 2;
		}
		hasNext = true;

		if(debug)System.err.println("start assigning Vars");

		main: 
		do {
			if(debug)System.err.println("currentVar=" + currentVar + "; nVar=" + nVar);
			while(varGen[currentVar].hasNext()){
				if(matchVar(currentVar, (ISet)varGen[currentVar].next())){
					currentVar++;
					if(currentVar <= nVar - 1){
						if(!makeGen(currentVar, available())){
							varGen[currentVar] = null;
							currentVar--;
						}
					}
					continue main;
				}
			}
			varGen[currentVar] = null;
			currentVar--;
		} while(currentVar >= 0 && currentVar < nVar);


		if(currentVar < 0){
			hasNext = false;
			return false;
		}
		return available().isEmpty();
	}			
}
