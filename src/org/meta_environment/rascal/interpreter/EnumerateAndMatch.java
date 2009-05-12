package org.meta_environment.rascal.interpreter;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Strategy;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.BoolResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedOperationError;

/*
 * EnumeratorAndMatch enumerates values and matches them against a pattern.
 * 
 * It is used to implement an enumerator of the form
 * 	pattern <- Expression
 * that occurs in a comprehension. The expression produces a sequence of values and the
 * pattern is matched against each value.
 * 
 * It is also used for the implementation of the descendant operator in patterns
 */

public class EnumerateAndMatch extends Result<IValue> {
	private boolean firstTime = true;
	private boolean hasNext = true;
	private Environment pushedEnv;
	private MatchPattern pat;
	private java.util.List<String> patVars;
	private org.meta_environment.rascal.ast.Expression enumerator;
	private Evaluator evaluator;
	private Iterator<?> iterator;

	/*
	 * Constructor for a standard enumerator
	 */
	
	@SuppressWarnings("unchecked")
	EnumerateAndMatch(Expression enumerator, Evaluator ev){
		super(ev.tf.boolType(), ev.vf.bool(true), null);

		if(!(enumerator.isEnumerator() || enumerator.isEnumeratorWithStrategy())){
			throw new ImplementationError("EnumerateAndMatch");
		}	
		this.enumerator = enumerator;
		this.evaluator = ev;
		pushedEnv = ev.pushEnv();
		pat = ev.evalPattern(enumerator.getPattern());
		
		java.util.List<String> vars = pat.getVariables();
		
		patVars = new java.util.ArrayList<String>(vars.size());
		for(String name : vars){
			Result<IValue> vr = pushedEnv.getVariable(null, name);
			if(vr == null || vr.getValue() == null)
				patVars.add(name);
		}
	
		Result<IValue> r = enumerator.getExpression().accept(ev);
		makeIterator(r);
	}
	
	/*
	 * Constructor for separate pattern and expression (as used for matching the descendant pattern)
	 */
	
	EnumerateAndMatch(MatchPattern pattern, Result<IValue> subject, Evaluator ev){
		super(ev.tf.boolType(), ev.vf.bool(true), null);
		this.enumerator = null;
		this.evaluator = ev;
		pushedEnv = null;
		pat = pattern;
		
		java.util.List<String> vars = pat.getVariables();
		patVars = new java.util.ArrayList<String>(0);
		for(String name : vars){
			Result<IValue> vr = ev.peek().getVariable(null, name);
			if(vr == null || vr.getValue() == null)
				patVars.add(name);
		}
		makeIterator(subject);
	}
		
	private void makeIterator(Result<IValue> subject){
		System.err.println("makeIterator: " + subject.getValue());
		Type subjectType = subject.getType();
		IValue subjectValue = subject.getValue();
		Type patType = pat.getType(evaluator.peek());
		
		// List
		if(subjectType.isListType()){
			checkNoStrategy(subjectType);
			checkMayOccur(patType, subjectType.getElementType());
			iterator = ((IList) subjectValue).iterator();
			
		// Set
		} else 	if(subjectType.isSetType()){
			checkNoStrategy(subjectType);
			checkMayOccur(patType, subjectType.getElementType());
			iterator = ((ISet) subjectValue).iterator();
		
		// Map
		} else if(subjectType.isMapType()){
			checkNoStrategy(subjectType);
			checkMayOccur(patType, subjectType.getKeyType());
			iterator = ((IMap) subjectValue).iterator();
			
		// Node and ADT
		} else if(subjectType.isNodeType() || subjectType.isAbstractDataType()){
			boolean bottomup = true;
			if(enumerator != null && enumerator.hasStrategy()){
				Strategy strat = enumerator.getStrategy();

				if(strat.isTopDown()){
					bottomup = false;
				} else if(strat.isBottomUp()){
						bottomup = true;
				} else {
					throw new UnsupportedOperationError(enumerator.toString(), subjectType, enumerator);
				}
			}
			checkMayOccur(patType, subjectType);
			iterator = new NodeReader((INode) subjectValue, bottomup);
		} else if(subjectType.isStringType()){
			checkNoStrategy(subjectType);
			if(!subjectType.isSubtypeOf(patType))
				throw new UnexpectedTypeError(patType, subjectType, enumerator);
			iterator = new SingleIValueIterator(subjectValue);
		} else {
			throw new UnsupportedOperationError("EnumerateAndMatch", subjectType, enumerator);
		}
	}
	
	private void checkNoStrategy(Type rType){
		if(enumerator != null && enumerator.hasStrategy()) {
			throw new UnsupportedOperationError(enumerator.toString(), rType, enumerator);
		}
	}
	
	private void checkMayOccur(Type patType, Type rType){
		if(!evaluator.mayOccurIn(patType, rType))
			throw new UnexpectedTypeError(patType, rType, enumerator);
	}
	
	@Override
	public Type getType(){
		return TypeFactory.getInstance().boolType();
	}
	
	@Override
	public IValue getValue(){
		if(firstTime){
			firstTime = false;
			return next().getValue();
		}
		return evaluator.vf.bool(true);
	
	}
	
	@Override
	public boolean hasNext(){
		if(hasNext){
			boolean hn = pat.hasNext() || iterator.hasNext();
			if(!hn){
				hasNext = false;
				System.err.println("EnumerateAndMatch.hasNext = false");
				if(pushedEnv != null)
					evaluator.popUntil(pushedEnv);
			}
			return hn;
		}
		return false;
	}

	@Override
	public Result next(){
		//System.err.println("getNext, trying pat " + pat);
		/*
		 * First, explore alternatives that remain to be matched by the current pattern
		 */
		
		while(pat.hasNext()){
			if(pat.next()){
				//System.err.println("return true");
				return new BoolResult(true, this, null);
			}
		}
		
		/*
		 * Next, fetch a new data element (if any) and create a new pattern.
		 */
		
		while(iterator.hasNext()){
			// Nullify the variables set by the pattern
			for(String var : patVars){
				evaluator.peek().storeVariable(var,  ResultFactory.nothing());
			}
			IValue v = (IValue) iterator.next();
			///System.err.println("getNext, try next from value iterator: " + v);
			pat.initMatch(v, evaluator.peek());
			while(pat.hasNext()){
				if(pat.next()){
					//System.err.println("return true");
				return new BoolResult(true, this, null);						
				}	
			}
		}
		System.err.println("next returns false and pops env");
		hasNext = false;
		if(pushedEnv != null){
			evaluator.popUntil(pushedEnv);
		}
		return new BoolResult(false, null, null);
	}

	@Override
	public void remove() {
		throw new ImplementationError("remove() not implemented for EnumerateAndMatch");
	}
}

