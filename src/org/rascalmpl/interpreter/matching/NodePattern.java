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
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ConstructorFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UninitializedPatternMatch;
import org.rascalmpl.interpreter.utils.Cases;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.TreeAsNode;
import org.rascalmpl.types.RascalType;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class NodePattern extends AbstractMatchingResult {
	private final TypeFactory tf = TypeFactory.getInstance();
	private Type type;
	private final Type patternConstructorType;
	private final QualifiedName qName;
	private final List<IMatchingResult> patternChildren;
	private INode subject;
	private int nextChild;
	private final IMatchingResult namePattern;
	private final Map<String, IMatchingResult> keywordParameters;
	private final boolean matchUPTR;

	public NodePattern(IEvaluatorContext ctx, Expression x, IMatchingResult matchPattern, QualifiedName name, Type constructorType, List<IMatchingResult> list, Map<String,IMatchingResult> keywordParameters){
		super(ctx, x);
		this.patternConstructorType = constructorType;
		this.patternChildren = list;
		this.keywordParameters = keywordParameters;

		if (matchPattern != null) {
			namePattern = matchPattern;
			matchUPTR = false;
			qName = null;
		}
		else if (name != null) {
			namePattern = null;
			qName = name;
			matchUPTR = Cases.IUPTR_NAMES.contains(Names.fullName(qName));
		}
		else {
			namePattern = null;
			qName = null;
			matchUPTR = false;
		}
	}

	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		hasNext = false;

		if (subject.isVoid()) {
			throw new UninitializedPatternMatch("Uninitialized pattern match: trying to match a value of the type 'void'", ctx.getCurrentAST());
		}

		if (!subject.getValue().getType().isSubtypeOf(tf.nodeType())) {
			return;
		}

		if (!matchUPTR && RascalType.isNonterminal(subject.getDynamicType()) && TreeAdapter.isAppl((ITree) subject.getValue())) {
			this.subject = new TreeAsNode((ITree) subject.getValue());
		}
		else {
			this.subject = (INode) subject.getValue();
		}

		String sname = this.subject.getName();
		if(qName != null){
			if(!((org.rascalmpl.semantics.dynamic.QualifiedName.Default) qName).lastName().equals(sname)){
				return;
			}
		} else {
			IString nameVal = ctx.getValueFactory().string(sname);

			namePattern.initMatch(ResultFactory.makeResult(tf.stringType(), nameVal, ctx));
			if(!(namePattern.hasNext() && namePattern.next())){
				return; // TODO What if the name has alternatives?
			}	
		}

		// Determine type compatibility between pattern and subject
		Type patternType = getType(ctx.getCurrentEnvt(), null);
		Type subjectType = this.subject.getType();

		if (subjectType.isAbstractData()) {
			subjectType = ((IConstructor) this.subject).getConstructorType();
		} 

		INode node = ((INode) this.subject);
		if (node.arity() != patternChildren.size()) {
			return; // that can never match
		}

		if (patternType.comparable(subjectType)) {
			hasNext = true;
		} else {
			return;
		}

		IValue[] subjectChildren = new IValue[patternChildren.size()];
		for (int i = 0; i < patternChildren.size(); i++){
			subjectChildren[i] = this.subject.get(i);
			IMatchingResult patternChild = patternChildren.get(i);

			patternChild.initMatch(ResultFactory.makeResult(subjectChildren[i].getType(), subjectChildren[i], ctx));
			hasNext = patternChild.hasNext();

			if (!hasNext) {
				break; // saves time!
			}
		}

		if (ctx.getCurrentEnvt().getStore().hasKeywordParameters(type)) {
			ConstructorFunction func = ctx.getCurrentEnvt().getConstructorFunction(type);
			Map<String, IValue> kwArgs = ((INode) subject).asWithKeywordParameters().getParameters();

			Map<String, Type> kwFormals = ctx.getCurrentEnvt().getStore().getKeywordParameters(type);
			
			for (String kwLabel : keywordParameters.keySet()) {
				IValue subjectParam = kwArgs.get(kwLabel);
				if (subjectParam == null) {
					subjectParam = func.computeDefaultKeywordParameter(kwLabel, (IConstructor) subject.getValue(), ctx.getCurrentEnvt()).getValue();
				}
				IMatchingResult matcher = keywordParameters.get(kwLabel);
				matcher.initMatch(ResultFactory.makeResult(kwFormals.get(kwLabel), subjectParam, ctx));
				
				if (!matcher.hasNext()) {
					// the matcher can never work, so we can skip initializing the rest
					hasNext = false;
					break;
				}
			}
		}
		else if (this.subject.mayHaveKeywordParameters()) {
			Map<String, IValue> kwArgs = this.subject.asWithKeywordParameters().getParameters();
			ConstructorFunction func = null;
			Type paramType = tf.valueType();
			
			if (this.subject.getType().isAbstractData()) {
				func = ctx.getCurrentEnvt().getConstructorFunction(((IConstructor) this.subject).getConstructorType());
			
			}
			
			for (Entry<String,IMatchingResult> entry : keywordParameters.entrySet()) {
				IValue subjectParam = kwArgs.get(entry.getKey());
				
				if (subjectParam == null && func != null) { // we may have a default available
					subjectParam = func.computeDefaultKeywordParameter(entry.getKey(), (IConstructor) subject.getValue(), ctx.getCurrentEnvt()).getValue();
				}
				
				if (func != null) {
					// this works around the problem of imprecisely resolved types for constructors in patterns
					paramType = ctx.getCurrentEnvt().getStore().getKeywordParameterType(func.getConstructorType(), entry.getKey());
				}
				
				if (subjectParam != null) {
					// we are matching a keyword parameter, and indeed the subject has one
					IMatchingResult matcher = entry.getValue();
					matcher.initMatch(ResultFactory.makeResult(paramType, subjectParam, ctx));
					
					if (!matcher.hasNext()) {
						// the subject parameter can never match so we bail out early
						hasNext = false;
						break;
					}
				} else {
					// we are matching a keyword parameter, but the subject has none.
					hasNext = false;
					break;
				}
			}
		}

		nextChild = 0;
	}

	@Override
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		if (type == null) {
			type = getConstructorType(env);

			if (type != null && type.isConstructor()) {
				type = getConstructorType(env).getAbstractDataType();
			}

			if (type == null) {
				type = TypeFactory.getInstance().nodeType();
			}
		}
		return type;
	}

	public Type getConstructorType(Environment env) {
		return patternConstructorType;
	}

	@Override
	public List<IVarPattern> getVariables(){
		java.util.LinkedList<IVarPattern> res = new java.util.LinkedList<IVarPattern> ();

		for (IMatchingResult c : patternChildren) {
			res.addAll(c.getVariables());
		}

		for (IMatchingResult v : keywordParameters.values()) {
		    res.addAll(v.getVariables());
		}
		
		if (res.isEmpty()) {
		    // nodes may have ignorable keyword parameters which we simulate here
		    // by adding a dummy variable to indicate this pattern is not constant. The reason
		    // is other containing patterns may treat a constructor without parameters as
		    // constant and simulate matching with isEqual otherwise.
		    res.add(new IVarPattern() {
                @Override
                public String name() {
                    return "_";
                }
                
                @Override
                public boolean isVarIntroducing() {
                    return false;
                }
                
                @Override
                public Type getType() {
                    return tf.voidType();
                }
            });
		}
		
		return res;
	}

	@Override
	public boolean next(){
		checkInitialized();

		if (!hasNext) {
			return false;
		}

		if (patternChildren.size() == 0) {
			hasNext = false;
			return nextKeywordParameters();
		}

		while (nextChild >= 0) {
			IMatchingResult nextPattern = patternChildren.get(nextChild);

			if (nextPattern.hasNext() && nextPattern.next()) {
				if (nextChild == patternChildren.size() - 1) {
					// We need to make sure if there are no
					// more possible matches for any of the tuple's fields, 
					// then the next call to hasNext() will definitely returns false.
					hasNext = false;
					for (int i = nextChild; i >= 0; i--) {
						IMatchingResult child = patternChildren.get(i);
						hasNext |= child.hasNext();
						if (patternConstructorType != null && !patternConstructorType.isNode() && hasNext) {
							// This code should disappear as soon as we have a type checker. 
							// Since constructors give us specific type contexts, an inferred type
							// for a child pattern variable should get this specific type. A type
							// checker would have inferred that already, but now we just push
							// the information down to all children of the constructor.
							child.updateType(patternConstructorType.getFieldType(i++));
						}
					}

					return nextKeywordParameters();
				}

				nextChild++;
			}
			else {
				nextChild--;

				if (nextChild >= 0) {
					for (int i = nextChild + 1; i < patternChildren.size(); i++) {
						IValue childValue = subject.get(i);
						IMatchingResult tailChild = patternChildren.get(i);
						tailChild.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
					}
				}
			}
		}
		hasNext = false;
		return false;
	}

	private boolean nextKeywordParameters() {
		for (Entry<String,IMatchingResult> entry : keywordParameters.entrySet()) {
			if (!entry.getValue().next()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString(){
		int n = patternChildren.size();
		StringBuilder res = new StringBuilder(qName != null ? Names.fullName(qName) : namePattern.toString());
		
		res.append("(");
		
		if (n == 1) {
			res.append(")");
			return res.toString() ;
		}
		
		String sep = "";

		for (IMatchingResult c : patternChildren){
			res.append(sep);
			sep = ", ";
			res.append(c.toString());
		}

		for (Entry<String,IMatchingResult> e : keywordParameters.entrySet()) {
			res.append(sep);
			sep = ", ";
			res.append(e.getKey());
			res.append("=");
			res.append(e.getValue().toString());
		}
		res.append(")");

		return res.toString();
	}
}


