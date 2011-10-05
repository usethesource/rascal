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

import java.util.HashMap;
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
	@SuppressWarnings("unused")
	private Type setSubjectType;				// Type of the subject
	@SuppressWarnings("unused")
	private Type setSubjectElementType;		    // Type of the elements of current subject

	private ISet fixedSetElements;				// The fixed, non-variable elements in the pattern
	private ISet availableSetElements;			// The elements in the subject that are available:
												// = setSubject - fixedSetElements
	/*
	 * The variables are indexed from 0, ..., nVar-1 in the order in which they occur in the pattern.
	 * There are three kinds:
	 * - a set variable
	 * - an element variable
	 * - a non-literal pattern that contains variables
	 */
	private int nVar;							// Number of variables
	private HashSet<String> patVars;            // List of names of variables at top-level of the pattern
	private HashMap<String,IVarPattern> allVars;// Map of names of all the variables in the pattern 
												// (including nested subpatterns)
	private String[] varName;					// Name of each variable
	private IValue[] varVal;				    // Value of each variable
	private IMatchingResult[] varPat;			// The pattern value for non-literal patterns
	private boolean[] isSetVar;				    // Is this a set variable?
	private boolean[] isBinding;                // Is this a binding variable occurrence?
	private Iterator<?>[] varGen;				// Value generator for this variable
	
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
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		if(patternSize == 0){
			return tf.setType(tf.voidType());
		}
		
		Type elemType = tf.voidType();
		for(int i = 0; i < patternSize; i++){
			Type childType = patternChildren.get(i).getType(env, patternVars);
			patternVars = merge(patternVars, patternChildren.get(i).getVariables());
			if(childType.isSetType()){
				elemType = elemType.lub(childType.getElementType());
			} else {
				elemType = elemType.lub(childType);
			}
		}
		return tf.setType(elemType);
	}
	
	@Override
	public List<IVarPattern> getVariables(){
		java.util.LinkedList<IVarPattern> res = new java.util.LinkedList<IVarPattern> ();
		for (int i = 0; i < patternChildren.size(); i++) {
			res.addAll(patternChildren.get(i).getVariables());
		 }
		return res;
	}
	
	private boolean isSetVar(int i){
		IVarPattern def = allVars.get(varName[i]);
		return isSetVar[i] || (def != null && def.getType().isSetType());
	}
	
	// Sort the variables: element variables and non-literal patterns should 
	// go before set variables since only set variables may be empty.
	
	private void sortVars(){
		String[] newVarName = new String[patternSize];
		IValue[]newVarVal= new ISet[patternSize];
		IMatchingResult[] newVarPat = new IMatchingResult[patternSize];
		boolean[] newIsSetVar = new boolean[patternSize];
		boolean[] newIsBinding = new boolean[patternSize];
		
		int nw = 0;
		for(int i = 0; i < nVar; i++){
			if(!isSetVar(i)){
				newVarName[nw] = varName[i];
				newVarVal[nw] = varVal[i];
				newVarPat[nw] = varPat[i];
				newIsSetVar[nw] = isSetVar[i];
				newIsBinding[nw] = isBinding[i];
				nw++;
			}
		}
		for(int i = 0; i < nVar; i++){
			if(isSetVar(i)){
				newVarName[nw] = varName[i];
				newVarVal[nw] = varVal[i];
				newVarPat[nw] = varPat[i];
				newIsSetVar[nw] = isSetVar[i];
				newIsBinding[nw] = isBinding[i];
				nw++;
			}
		}
		
		assert nw == nVar;
		for(int i = 0; i < nVar; i++){
			varName[i] = newVarName[i];
			varVal[i] = newVarVal[i];
			varPat[i] = newVarPat[i];
			isSetVar[i] = newIsSetVar[i];
			isBinding[i] = newIsBinding[i];
		}
	}
	
	private void printVars(){
		for(int i = 0; i < nVar; i++){
			System.err.printf("%d: %s, isSetVar=%b, isBinding=%b, pat=%s, val=%s\n", i, varName[i], isSetVar(i), isBinding[i], varPat[i], varVal[i]);
		}
	}
	
	private boolean declaredAsSetVar(String name){
		for(int i = 0; i < nVar; i++){
			if(varName[i].equals(name))
				return isSetVar(i);
		}
		return false;
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
		//fixedSetElements = ctx.getValueFactory().set(getType(env).getElementType());
		fixedSetElements = ctx.getValueFactory().set(setSubjectElementType);
		
		nVar = 0;
		patVars = new HashSet<String>();
		allVars = new HashMap<String, IVarPattern>();
		varName = new String[patternSize];  			// Some overestimations
		isSetVar = new boolean[patternSize];
		isBinding = new boolean[patternSize];
		varVal = new IValue[patternSize];
		varPat = new IMatchingResult[patternSize];
		varGen = new Iterator<?>[patternSize];
		/*
		 * Pass #1: determine the (ordinary and set) variables in the pattern
		 */
		for(int i = 0; i < patternSize; i++){
			IMatchingResult child = patternChildren.get(i);
			if(debug)System.err.println("child = " + child);
			if(child instanceof TypedVariablePattern){
				TypedVariablePattern patVar = (TypedVariablePattern) child;
				Type childType = child.getType(env, null);
				String name = ((TypedVariablePattern)child).getName();
				if(!patVar.isAnonymous() && allVars.containsKey(name)){
					throw new RedeclaredVariableError(name, getAST());
				}
				if(childType.comparable(staticSetSubjectType) || childType.comparable(staticSubjectElementType)){
					/*
					 * An explicitly declared set or element variable.
					 */
					if(!patVar.isAnonymous()){
						patVars.add(name);
						allVars.put(name, (IVarPattern)child);
					}
					varName[nVar] = name;
					varPat[nVar] = child;
					isSetVar[nVar] = childType.isSetType();
					isBinding[nVar] = true;
					nVar++;
				} else {
					hasNext = false;
					return;
					// We would like to throw new UnexpectedTypeError(setSubject.getType(), childType, getAST());
					// but we can't do this in the context of a visit, because we might actually visit another set!
				}
				
			} else if(child instanceof MultiVariablePattern){
				/*
				 * Explicitly declared set variable
				 */
				MultiVariablePattern multiVar = (MultiVariablePattern) child;
				String name = multiVar.getName();
				if(!multiVar.isAnonymous() && allVars.containsKey(name)){
					throw new RedeclaredVariableError(name, getAST());
				}
				varName[nVar] = name;
				varPat[nVar] = child;
				isSetVar[nVar] = true;
				isBinding[nVar] = true;
				nVar++;
			} else if(child instanceof QualifiedNamePattern){
				/*
				 * Use of a variable
				 */
				QualifiedNamePattern qualName = (QualifiedNamePattern) child;
				String name = qualName.getName();
				if (!qualName.isAnonymous() && allVars.containsKey(name)) {
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
						// If is was declared as set in the current pattern then we are sure it is a set variable,
						// otherwise we assume for now that it is not but we check this again later in matchVar.
						isSetVar[nVar] = declaredAsSetVar(name);
						isBinding[nVar] = false;
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
					isBinding[nVar] = false;
					nVar++;
				} else  {
					Result<IValue> varRes = env.getVariable(name);
					
					if(varRes == null || qualName.bindingInstance()){
						// Completely new variable that was not yet declared in this pattern or its subpatterns
						varName[nVar] = name;
						varPat[nVar] = child;
						isSetVar[nVar] = false;
						isBinding[nVar] = true;
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
					        	hasNext = false; 
					        	return;
					        	// can't throw type error: throw new UnexpectedTypeError(staticSetSubjectType,varType, getAST());
					        }
					    } 
					    else {
					    	// Support pre-declared list variables
					    
					    	if(varRes.getType().comparable(staticSetSubjectType) || varRes.getType().comparable(staticSubjectElementType)){
								/*
								 * An explicitly declared set or element variable.
								 */
								if(!name.equals("_")){
									patVars.add(name);
									allVars.put(name, (IVarPattern) child);
								}
								varName[nVar] = name;
								varPat[nVar] = child;
								isSetVar[nVar] = varRes.getType().isSetType();
								isBinding[nVar] = false;
								nVar++;
					    	}
					    }
				    }
				}
			} else if(child instanceof LiteralPattern){
				IValue lit = ((LiteralPattern) child).toIValue(env);
				Type childType = child.getType(env, null);
				if(!childType.comparable(staticSubjectElementType)){
					throw new UnexpectedTypeError(setSubject.getType(), childType, getAST());
				}
				fixedSetElements = fixedSetElements.insert(lit);
			} else {
				Type childType = child.getType(env, null);
				if(!childType.comparable(staticSubjectElementType)){
					hasNext = false;
					return;
					// can't throw type error: throw new UnexpectedTypeError(setSubject.getType(), childType, getAST());
				}
				java.util.List<IVarPattern> childVars = child.getVariables();
				if(!childVars.isEmpty()){ 
					for(IVarPattern vp : childVars){
						IVarPattern prev = allVars.get(vp.name());
						if(prev == null || vp.isVarIntroducing())
							allVars.put(vp.name(), vp);
					}
					varName[nVar] = child.toString();
					varPat[nVar] = child;
					isSetVar[nVar] = false;
					isBinding[nVar] = false;
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
		if(debug){
			printVars();
			System.err.printf("hasNext = %b\n", hasNext);
			System.err.printf("FixedSetElements: ");
			for(IValue v : fixedSetElements){
				System.err.printf("%s ", v);
			}
			System.err.printf("\n");
			System.err.printf("availableSetElements: ");
			for(IValue v : availableSetElements){
				System.err.printf("%s ", v);
			}
			System.err.printf("\n");
		}
	}
	
	@Override
	public boolean hasNext(){
		return initialized && hasNext;
	}
	
	/**
	 * @return the remaining subject set elements that are available to be matched
	 */
	private ISet available(){
		ISet avail = availableSetElements;
		for(int j = 0; j < currentVar; j++){
			if(isSetVar(j))
				avail = avail.subtract((ISet)varVal[j]);
			else
				avail = avail.delete(varVal[j]);
		}
		return avail;
	}
	
	
	/**
	 * Create a value generator for variable i.
	 * @param i			the variable
	 * @param elements	set elements that may be assigned to it
	 * @return			whether creation succeeded
	 */
	private boolean makeGen(int i, ISet elements) {
		if(debug){
			System.err.printf("makeGen(%s, {", varName[i]);
			for(IValue v : elements){
				System.err.printf("%s ", v);
			}
			System.err.printf("})\n");
		}
		Environment env = ctx.getCurrentEnvt();
		
		if(varPat[i] instanceof QualifiedNamePattern){
			QualifiedNamePattern qualName = (QualifiedNamePattern) varPat[i];
			String name = qualName.getName();
			if(qualName.isAnonymous() || env.getVariable(name) == null){
				if(isSetVar(i)){
					varGen[i] = new SubSetGenerator(elements, ctx);
				} else {
					if(elements.size() == 0)
						return false;
					varGen[i] = new SingleElementIterator(elements, ctx);
				}
			} else {
				IValue val = env.getVariable(name).getValue();
				// Variable has been set before, use its dynamic type to distinguish set variables.
				if(val.getType().isSetType()){
					isSetVar[i] = true;
					if(elements.equals(val)){
						varGen[i] = new SingleIValueIterator(val);
						return true;
					}
					return false;
				}
				if(elements.contains(val)){
					varGen[i] = new SingleIValueIterator(val);
				} else
					return false;
			}
			return true;
		}
		
		if(isSetVar(i)){
			varGen[i] = new SubSetGenerator(elements, ctx);
			return true;
		}
		if(elements.size() == 0)
			return false;
		varGen[i] = new SingleElementIterator(elements, ctx);
		return true;
	}
	
	private boolean matchPatternElement(int i, IValue elements){
		
		IValue elem ;
		if(isSetVar(i)){
			varVal[i] = (ISet) elements;
			elem = elements;
		} else {
			if(elements.getType().isSetType()){
				ISet set = (ISet) elements;
				assert set.size() == 1;
				varVal[i] = elem = set.iterator().next();
			} else {
				varVal[i] = elem = elements;
				//varVal[i] = ctx.getValueFactory().set(elem.getType()).insert(elem); // TODO: Should this be  a set?
			}
		}
		
		// TODO: see if we can use a static type here?!
		
//		if(!isBinding[i] && varPat[i] instanceof QualifiedNamePattern){
//			Result<IValue> r = ctx.getCurrentEnvt().getVariable(varName[i]);
//			return r.getValue().isEqual(elements);
//		}
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
				if(isSetVar(0) || availableSetElements.size() == 1){
					return matchPatternElement(0, availableSetElements);
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
			if(debug)System.err.println("currentVar=" + varName[currentVar] + "; nVar=" + nVar);
			while(varGen[currentVar].hasNext()){
				if(matchPatternElement(currentVar, (IValue) varGen[currentVar].next())){
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
