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
 *   * Mark Hills - Mark.Hills@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariable;
import org.rascalmpl.values.iterators.SingleIValueIterator;

import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class SetPattern extends AbstractMatchingResult {
    private final boolean bindTypeParameters;
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
	private boolean[] isNested;		    		// Is this a nested pattern?
	private Iterator<?>[] varGen;				// Value generator for this variable
	
	private int currentVar;					    // The currently matched variable
    private boolean firstMatch;				    // First match of this pattern?
	
	private boolean debug = false;
	private Type staticSetSubjectType;
	private Type staticSubjectElementType;
	
	public SetPattern(IEvaluatorContext ctx, Expression x, List<IMatchingResult> list, boolean bindTypeParameters){
		super(ctx, x);
		this.bindTypeParameters = bindTypeParameters;
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
			IMatchingResult child = patternChildren.get(i);
			Type childType = child.getType(env, patternVars);
			patternVars = merge(patternVars, patternChildren.get(i).getVariables());
			if(debug)System.err.println(" i = " + i + ": " + patternChildren.get(i) + ", type = " + childType);
			boolean isMultiVar = child instanceof MultiVariablePattern || child instanceof DesignatedTypedMultiVariablePattern;
			  
			if(childType.isSet() && isMultiVar){
				elemType = elemType.lub(childType.getElementType());
			} else {
				elemType = elemType.lub(childType);
			}
		}
		if(debug)System.err.println("SetPattern.getType: " + this + " returns " + tf.setType(elemType));
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
		return isSetVar[i];
	}
	
	// Sort the variables: element variables and non-literal patterns should 
	// go before set variables since only set variables may be empty.
	
	private void sortVars(){
		String[] newVarName = new String[patternSize];
		IValue[]newVarVal= new ISet[patternSize];
		IMatchingResult[] newVarPat = new IMatchingResult[patternSize];
		boolean[] newIsSetVar = new boolean[patternSize];
		boolean[] newIsBinding = new boolean[patternSize];
		boolean[] newIsNested = new boolean[patternSize];
		
		int nw = 0;
		for(int i = 0; i < nVar; i++){
			if(!isSetVar(i)){
				newVarName[nw] = varName[i];
				newVarVal[nw] = varVal[i];
				newVarPat[nw] = varPat[i];
				newIsSetVar[nw] = isSetVar[i];
				newIsBinding[nw] = isBinding[i];
				newIsNested[nw] = isNested[i];
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
				newIsNested[nw] = isNested[i];
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
			isNested[i] = newIsNested[i];
		}
	}
	
	private void printVars(){
		for(int i = 0; i < nVar; i++){
			System.err.printf("%d: %s, isSetVar=%b, isBinding=%b, isNested=%b, val=%s\n", i, varName[i], isSetVar(i), isBinding[i], isNested[i], varVal[i]);
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
		
		if (!subject.getValue().getType().isSet()) {
			hasNext = false;
			return;
		}
		
		setSubject = (ISet) subject.getValue();
		
		if(debug) System.err.println("\n*** begin initMatch for " + this + " := " + setSubject);
		
		setSubjectType = setSubject.getType(); // have to use static type here
		staticSetSubjectType = subject.getStaticType();
		setSubjectElementType = setSubject.getElementType();
		staticSubjectElementType = staticSetSubjectType.isSet() ? staticSetSubjectType.getElementType() : tf.valueType();
		
		if(debug)System.err.println("setSubjectType = " + setSubjectType + ", staticSetSubjectType = " + staticSetSubjectType + ", setSubjectElementType = " + setSubjectElementType + ", staticSubjectElementType =" + staticSubjectElementType);
		Environment env = ctx.getCurrentEnvt();
		fixedSetElements = ctx.getValueFactory().set();
		
		nVar = 0;
		patVars = new HashSet<String>();
		allVars = new HashMap<String, IVarPattern>();
		varName = new String[patternSize];  			// Some overestimations
		isSetVar = new boolean[patternSize];
		isBinding = new boolean[patternSize];
		isNested = new boolean[patternSize];
		varVal = new IValue[patternSize];
		varPat = new IMatchingResult[patternSize];
		varGen = new Iterator<?>[patternSize];
		/*
		 * Pass #1: determine the (ordinary and set) variables in the pattern
		 */
		for(int i = 0; i < patternSize; i++){
			IMatchingResult child = patternChildren.get(i);
			if(debug)System.err.println("child = " + child);
			
			 if (child instanceof TypedMultiVariablePattern) {
		          TypedMultiVariablePattern tmv = (TypedMultiVariablePattern) child;
		          
		          // now we know what we are, a set multi variable!
		          child = new DesignatedTypedMultiVariablePattern(ctx, (Expression) tmv.getAST(), tf.setType(tmv.getType(env,  null)), tmv.getName(), bindTypeParameters); 
		          
		          // cache this information for the next round, we'll still be a list
		          patternChildren.set(i, child);
		      }
			 
			if (child instanceof DesignatedTypedMultiVariablePattern) {
				DesignatedTypedMultiVariablePattern tmvVar = (DesignatedTypedMultiVariablePattern) child;
				Type childType = child.getType(env, null);
				String name = tmvVar.getName();
				
				if (!tmvVar.isAnonymous() && allVars.containsKey(name)) {
					throw new RedeclaredVariable(name, getAST());
				}
				else if (childType.comparable(staticSetSubjectType)) {
					if (!tmvVar.isAnonymous()) {
						patVars.add(name);
						allVars.put(name,  (IVarPattern)child);
					}
					varName[nVar] = name;
					varPat[nVar] = child;
					isSetVar[nVar] = true;
					isBinding[nVar] = true;
					isNested[nVar] = false;
					++nVar;
				} else {
					hasNext = false;
					return;
				}
			} else if(child instanceof TypedVariablePattern){
				TypedVariablePattern patVar = (TypedVariablePattern) child;
				Type childType = child.getType(env, null);
				String name = ((TypedVariablePattern)child).getName();
				if(!patVar.isAnonymous() && allVars.containsKey(name)){
					throw new RedeclaredVariable(name, getAST());
				}
				if(childType.comparable(staticSubjectElementType)){
					/*
					 * An explicitly declared set or element variable.
					 */
					if(!patVar.isAnonymous()){
						patVars.add(name);
						allVars.put(name, (IVarPattern)child);
					}
					varName[nVar] = name;
					varPat[nVar] = child;
					isSetVar[nVar] = false;
					isBinding[nVar] = true;
					isNested[nVar] = false;
					nVar++;
				} else {
					hasNext = false;
					return;
					// We would like to throw new UnexpectedType(setSubject.getType(), childType, getAST());
					// but we can't do this in the context of a visit, because we might actually visit another set!
				}
				
			} else if(child instanceof MultiVariablePattern){
				/*
				 * Explicitly declared set variable
				 */
				MultiVariablePattern multiVar = (MultiVariablePattern) child;
				String name = multiVar.getName();

				varName[nVar] = name;
				varPat[nVar] = child;
				isSetVar[nVar] = true;
				isBinding[nVar] = true;
				isNested[nVar] = false;
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
						isNested[nVar] = false;
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
					isNested[nVar] = false;
					nVar++;
				} else  {
					/* 
					 * A non-anonymous variable, not seen before.
					 */
					if(debug)System.err.println("Non-anonymous var, not seen before: " + name);
					
					Result<IValue> varRes = env.getFrameVariable(name);
					
					if(varRes == null || qualName.bindingInstance()){
						// Completely new variable that was not yet declared in this pattern or its subpatterns
						varName[nVar] = name;
						varPat[nVar] = child;
						isSetVar[nVar] = false;
						isBinding[nVar] = true;
						isNested[nVar] = false;
						nVar++;
						// TODO: Why is this here? The pattern also declares the variable,
						// so this just causes errors when we use variables in set patterns.
						// env.declareVariable(staticSubjectElementType, name);
					} else {
					    if(varRes.getValue() != null){
					        Type varType = varRes.getStaticType();
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
					        	// can't throw type error: throw new UnexpectedType(staticSetSubjectType,varType, getAST());
					        }
					    } 
					    else {
					    	// Support pre-declared set variables
					    
					    	if(varRes.getStaticType().comparable(staticSetSubjectType) || varRes.getStaticType().comparable(staticSubjectElementType)){
								/*
								 * An explicitly declared set or element variable.
								 */
								if(!name.equals("_")){
									patVars.add(name);
									allVars.put(name, (IVarPattern) child);
								}
								varName[nVar] = name;
								varPat[nVar] = child;
								isSetVar[nVar] = varRes.getStaticType().isSet();
								isBinding[nVar] = false;
								isNested[nVar] = false;
								nVar++;
					    	}
					    }
				    }
				}
			} else if(child instanceof LiteralPattern){
				/*
				 * A literal pattern: add it to the set of fixed element
				 */
				IValue lit = ((LiteralPattern) child).toIValue(env);
				Type childType = child.getType(env, null);
				if(!childType.comparable(staticSubjectElementType)){
//					throw new UnexpectedType(setSubject.getType(), childType, getAST());
					hasNext = false;
					return;
				}
				fixedSetElements = fixedSetElements.insert(lit);
				if(debug)System.err.println("fixedSetElements => " + fixedSetElements);
			} else {
				/*
				 * All other cases of a nested pattern that is not a variable or a literal.
				 */
				Type childType = child.getType(env, null);
				if(!childType.comparable(staticSubjectElementType)){
					hasNext = false;
					return;
					// can't throw type error: throw new UnexpectedType(setSubject.getType(), childType, getAST());
				}
				java.util.List<IVarPattern> childVars = child.getVariables();
				if(!childVars.isEmpty()){
					boolean varIntro = false;
					for(IVarPattern vp : childVars){
						if(debug) System.err.println("var =" + vp);
						IVarPattern prev = allVars.get(vp.name());
						boolean vpVarIntro = vp.isVarIntroducing();
						varIntro = varIntro || vpVarIntro;
						if(prev == null || vpVarIntro)
							allVars.put(vp.name(), vp);
					}
					varName[nVar] = child.toString();
					varPat[nVar] = child;
					isSetVar[nVar] = false;
					isBinding[nVar] = varIntro;
					isNested[nVar] = true;
					nVar++;
				} else {
					// A nested pattern without variables: treat it as a literal
					fixedSetElements = fixedSetElements.insert(child.toIValue());
					if(debug)System.err.println("fixedSetElements => " + fixedSetElements);
				}
			} 
		}
		/*
		 * Pass #2: set up subset generation
		 */
		firstMatch = true;
		hasNext = fixedSetElements.isSubsetOf(setSubject);
		if(debug){
			System.err.println("fixedSetElements => " + fixedSetElements);
			System.err.println("setSubject => " + setSubject);
		}
		availableSetElements = setSubject.subtract(fixedSetElements);
		sortVars();
		if(debug){
			System.err.println("Pattern " + this);
			printVars();
			System.err.printf("hasNext = %b\n", hasNext);
			System.err.println("FixedSetElements: " + fixedSetElements);
			System.err.println("availableSetElements: " + availableSetElements);
			if(debug) System.err.println("*** end initMatch for " + this + "\n");
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
			if(isSetVar(j)){
				if(varVal[j] != null)
					avail = avail.subtract((ISet)varVal[j]);
			} else {
				if(varVal[j] != null)
					avail = avail.delete(varVal[j]);
			}
		}
		return avail;
	}
	
	private boolean unexploredAlternatives(){
		assert currentVar == nVar;
		for (int j = 0; j < nVar; j++) {
			if (isSetVar(j)) {
				if (varGen[j].hasNext()) {
					return true;
				}
			} 
			else if (isNested[j] && varPat[j].hasNext()) {
			    return true;
			}
			else if (varGen[j].hasNext()) {
			    return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Create a value generator for variable i.
	 * @param i			the variable
	 * @param elements	set elements that may be assigned to it
	 * @return			whether creation succeeded
	 */
	private boolean makeGen(int i, ISet elements) {
		if(debug) System.err.println("makeGen: " + i +", " + elements);
		
		Environment env = ctx.getCurrentEnvt();
		
		if(varPat[i] instanceof QualifiedNamePattern){
			QualifiedNamePattern qualName = (QualifiedNamePattern) varPat[i];
			String name = qualName.getName();
			// Binding occurrence of this variable?
			if(isBinding[i] || qualName.isAnonymous() || env.getFrameVariable(name) == null || env.getFrameVariable(name).getValue() == null){
				if(isSetVar(i)){
					varGen[i] = new SubSetGenerator(elements, ctx);
				} else {
					if(elements.size() == 0) {
						return false;
					}
					varGen[i] = elements.iterator();
				}
			} else {
				// Variable has been set before, use its dynamic type to distinguish set variables.
				IValue val = env.getFrameVariable(name).getValue();
				
				if (val != null) {
				  if (val != null && val.getType().isSet()){
				    isSetVar[i] = true;
				    if(elements.equals(val)){
				      varGen[i] = new SingleIValueIterator(val);
				      return true;
				    }
				    return false;
				  }
				  if (elements.contains(val)){
				    varGen[i] = new SingleIValueIterator(val);
				  } else {
				    return false;
				  }
				}
			}
			return true;
		}
		
		if(isSetVar(i)){
			varGen[i] = new SubSetGenerator(elements, ctx);
			return true;
		}
		if(elements.size() == 0) {
			return false;
		}
		varGen[i] = elements.iterator();
		return true;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		
		if(!hasNext)
			return false;
		
		if(firstMatch){
			firstMatch = hasNext = false;
			if(nVar == 0){
				return fixedSetElements.equals(setSubject);
			}
			if(!fixedSetElements.isSubsetOf(setSubject)){
				return false;
			}
			
			if(patternSize == 1){
			  if(isSetVar(0) || availableSetElements.size() == 1) {
			    IValue elem ;
          if(isSetVar(0)){
            // Var #i is a set variable: should match all elements
            varVal[0] = (ISet) availableSetElements;
            elem = availableSetElements;
          } 
          else {
            // Var #i is not a set variable.
            if(availableSetElements.getType().isSet()){
              // Var #i should match single element in elements
              ISet set = (ISet) availableSetElements;
              assert set.size() == 1;
              varVal[0] = elem = set.iterator().next();
            } else {
              varVal[0] = elem = availableSetElements;
              //varVal[i] = ctx.getValueFactory().set(elem.getType()).insert(elem); // TODO: Should this be  a set?
            }
          }

          varPat[0].initMatch(ResultFactory.makeResult(elem.getType(), elem, ctx));
          hasNext = varPat[0].hasNext();
			  }
			}
			
			currentVar = 0;
			if(!makeGen(currentVar, availableSetElements)){
				return false;
			}
		} else {
			currentVar = nVar - 1;
		}
		hasNext = true;

		if(debug)System.err.println("\nStart assigning Vars for " + this + ":= " + subject);

		if (patternSize == 1 && (isSetVar(0) || availableSetElements.size() == 1)) {
		    // it has matched, but should not produce more continuations
            hasNext = false;
            
		    if (varPat[0].hasNext() && varPat[0].next()) {
		        return true;
		    }

		    return false;
		}
		
		main: 
		do {
			if (ctx.isInterrupted()) {
				throw new InterruptException(ctx.getStackTrace(), ctx.getCurrentAST().getLocation());
			}
			
			if(debug)System.err.println("\n=== MAIN: Pattern = " + this +  ":= " + subject + "\ncurrentVar[" + currentVar + "]=" + varName[currentVar]);
			if(debug)printVars();
			IValue v = null;
			boolean b = false;
			if(isNested[currentVar]){
				if(varPat[currentVar].hasNext()){
					b = varPat[currentVar].next();
					if(b)
						v = varVal[currentVar];
				} else if(varGen[currentVar].hasNext()){
					v = (IValue) varGen[currentVar].next();
					Result<IValue> r = ResultFactory.makeResult(v.getType(), v, ctx);
					varPat[currentVar].initMatch(r);
					b = varPat[currentVar].next();
					if(b)
						varVal[currentVar] = v;
				}
			} else if(isSetVar[currentVar]){
					if(varGen[currentVar].hasNext()){
						v = (IValue) varGen[currentVar].next();
						Result<IValue> r = ResultFactory.makeResult(v.getType().lub(setSubjectType), v, ctx);
						varPat[currentVar].initMatch(r);
						b = varPat[currentVar].next();
						if(b){
							varVal[currentVar] = v;
							ctx.getCurrentEnvt().storeVariable(varName[currentVar], r);
							if(debug)System.err.println("Store in " + varName[currentVar] + ": " + r + " / " + v + " / " + v.getType() + " / " +
							ctx.getCurrentEnvt().getFrameVariable(varName[currentVar]).getStaticType());
						}
					}
			} else if(isBinding[currentVar]){
				if(varGen[currentVar].hasNext()){
					v = (IValue) varGen[currentVar].next();
					
					Result<IValue> r = ResultFactory.makeResult(v.getType(), v, ctx);
					varPat[currentVar].initMatch(r);
					b = varPat[currentVar].next();
					if(b){
						varVal[currentVar] = v;
						ctx.getCurrentEnvt().storeVariable(varName[currentVar], r);
						if(debug)System.err.println("Store in " + varName[currentVar] + ": " + r + " / " + v + " / " + v.getType() + " / " +
								ctx.getCurrentEnvt().getFrameVariable(varName[currentVar]).getStaticType());
					}
				}
			} else 	if(varPat[currentVar] instanceof QualifiedNamePattern && ((QualifiedNamePattern)varPat[currentVar] ).isAnonymous()){
					if(varGen[currentVar].hasNext()){
						v = (IValue) varGen[currentVar].next();
						Result<IValue> r = ResultFactory.makeResult(v.getType(), v, ctx);
						varPat[currentVar].initMatch(r);
						b = varPat[currentVar].next();
						if(b){
							varVal[currentVar] = v;
						}
					}
			} else if(!isBinding[currentVar] && (varPat[currentVar] instanceof QualifiedNamePattern || varPat[currentVar] instanceof TypedVariablePattern)  && varGen[currentVar].hasNext()){
					v = (IValue) varGen[currentVar].next();
					Result<IValue> r = ResultFactory.makeResult(v.getType(), v, ctx);
					varPat[currentVar].initMatch(r);
					b = varPat[currentVar].next();
					if (debug) System.err.println("Try match " + varName[currentVar] + ": " + r + " / " + v + " / " + v.getType());
					if(b){
						varVal[currentVar] = v;
						if (debug) System.err.println("Matches " + varName[currentVar] + ": " + r + " / " + v + " / " + v.getType());
					}
				
			} else {
				if (debug) System.err.println("CANNOT HANDLE THIS 2");
			}
			
			//if(debug) System.err.println("currentVar[" + currentVar + "] = " +  varName[currentVar] + "; v = " + v + "; type: " + v.getType() + "; matchPatternElement = " + b);
			if(b){
				currentVar++;
				ISet avail = available();
				if(currentVar <= nVar - 1){
					if(!makeGen(currentVar, avail)){
						varGen[currentVar] = null;
						currentVar--;
					}
					continue main;
				} else {
					if(!avail.isEmpty()){
						if(debug)System.err.println("nomatch: currentVar = " + currentVar + " avail = " + available());
						currentVar--;
						continue main;
					}
					hasNext = unexploredAlternatives();
					if(debug) System.err.println("currentVar > nVar -1\n" + this + ":= " + subject + " returns true, hasNext = " + hasNext + ", available = " + available());
					return true;
				}
				
			}
			if(!varGen[currentVar].hasNext()){
				varGen[currentVar] = null;
				currentVar--;
			}
			if(debug)System.err.println("currentVar becomes: " + currentVar);
			continue main;
		} while(currentVar >= 0 && currentVar < nVar);


		if(currentVar < 0){
			hasNext = false;
			if(debug) System.err.println("Pattern " + this + " := " + subject + " returns false");
			return false;
		}
		hasNext = unexploredAlternatives();
		if(debug) System.err.println("Pattern " + this +  " := " + subject + " returns true, hasNext = " + hasNext + ", available = " + available());
		return available().isEmpty();
	}	
	
	@Override
	public String toString(){
		StringBuilder res = new StringBuilder();
		res.append("{");
		String sep = "";
		for (IBooleanResult mp : patternChildren){
			res.append(sep);
			sep = ", ";
			res.append(mp.toString());
		}
		res.append("}");
		
		return res.toString();
	}
}
