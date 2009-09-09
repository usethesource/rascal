package org.meta_environment.rascal.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Strategy;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedOperationError;

/** 
 * The Enumerator should not be a matcher, but because it traverses the subject and
 * because it is reused by DescandantPattern it must be for now.
 */
public class EnumeratorResult extends AbstractMatchingResult {
	private IMatchingResult pat;
	private Iterator<?> iterator;
	private Strategy strategy;
	private Expression expression;
	private boolean firstTime;

	/*
	 * Constructor for a standard enumerator
	 */
	
	public EnumeratorResult(IValueFactory vf, IEvaluatorContext ctx, IMatchingResult matchPattern, Strategy strategy, Expression expression){
		super(vf, ctx);
		this.pat = matchPattern;
		this.strategy = strategy;
		this.expression = expression;
	}
	
	@Override
	public void init() {
		super.init();
		firstTime = true;
	}
	
	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		if (subject == null && expression != null) {
			// this is needed because DescendantPattern reuses the EnumeratorPattern
			subject = expression.accept(ctx.getEvaluator());
			makeIterator(subject.getType(), subject.getValue());
		}
		else if (expression == null && subject != null) {
			makeIterator(subject.getType(), subject.getValue());
		}
		
		firstTime = true;
		hasNext = true;
	}
	
	private void makeIterator(Type subjectType, IValue subjectValue){
		Type patType = pat.getType(ctx.getCurrentEnvt());
		
		// TODO: this should be a visitor design as well..
		
		while (subjectType.isAliasType()) {
			subjectType = subjectType.getAliased();
		}
		
		// List
		if(subjectType.isListType()){
			checkNoStrategy(subjectType);
			//TODO: we could do this more precisely
			if(!subjectType.getElementType().isVoidType())
				checkMayOccur(patType, subjectType.getElementType());
			iterator = ((IList) subjectValue).iterator();
			
		// Set
		} else 	if(subjectType.isSetType()){
			checkNoStrategy(subjectType);
			if(!subjectType.getElementType().isVoidType())
				checkMayOccur(patType, subjectType.getElementType());
			iterator = ((ISet) subjectValue).iterator();
		
		// Map
		} else if(subjectType.isMapType()){
			checkNoStrategy(subjectType);
			if(!subjectType.getKeyType().isVoidType())
				checkMayOccur(patType, subjectType.getKeyType());
			iterator = ((IMap) subjectValue).iterator();
			
		// Node and ADT
		} else if(subjectType.isNodeType() || subjectType.isAbstractDataType()){
			boolean bottomup = true;
			if(strategy != null){
				if(strategy.isTopDown()){
					bottomup = false;
				} else if(strategy.isBottomUp()){
						bottomup = true;
				} else {
					throw new UnsupportedOperationError(strategy.toString(), subjectType, strategy);
				}
			}
			checkMayOccur(patType, subjectType);
			//iterator = new NodeChildGenerator((INode) subjectValue);
			iterator = new NodeReader((INode) subjectValue, bottomup);
		} else if(subjectType.isTupleType()){
			checkNoStrategy(subjectType);
			int nElems = subjectType.getArity();
			for(int i = 0; i < nElems; i++){
				if(!subjectType.getFieldType(i).isSubtypeOf(patType)) {
					throw new UnexpectedTypeError(patType, subjectType.getFieldType(i), ctx.getCurrentAST());
				}
			}
			iterator = new TupleElementGenerator((ITuple)subjectValue);
			
		} else if(subjectType.isBoolType() ||
				subjectType.isIntegerType() ||
				subjectType.isStringType() ||
				subjectType.isSourceLocationType())
				{
			checkNoStrategy(subjectType);
			if(!subjectType.isSubtypeOf(patType)) {
				throw new UnexpectedTypeError(patType, subjectType, ctx.getCurrentAST());
			}
			iterator = new SingleIValueIterator(subjectValue);
		} else {
			throw new UnsupportedOperationError("makeIterator", subjectType, ctx.getCurrentAST());
		}
	}
	
	private void checkNoStrategy(Type rType){
		if(strategy != null) {
			throw new UnsupportedOperationError(strategy.toString(), rType, ctx.getCurrentAST());
		}
	}
	
	private void checkMayOccur(Type patType, Type rType){
		if(!ctx.getEvaluator().mayOccurIn(patType, rType))
			throw new UnexpectedTypeError(patType, rType, ctx.getCurrentAST());
	}
	
	@Override
	public boolean hasNext(){
		if (firstTime) {
			hasNext = true;
			return true;
		}
		
		if(hasNext){
			boolean hn = pat.hasNext() || iterator.hasNext();
			if(!hn){
				hasNext = false;
			}
			return hn;
		}
		return false;
	}

	@Override
	public boolean next() {
		if (firstTime) {
			firstTime = false;
			
			if (subject == null && expression != null) {
				Result<IValue> result = expression.accept(ctx.getEvaluator());
				makeIterator(result.getType(), result.getValue());
			}
			else if (subject != null) {
				makeIterator(subject.getType(), subject.getValue());
			}
			
		}

		/*
		 * First, explore alternatives that remain to be matched by the current pattern
		 */
		while(pat.hasNext()){
			if(pat.next()){
				return true;
			}
		}
		
		/*
		 * Next, fetch a new data element (if any) and create a new pattern.
		 */
		
		while(iterator.hasNext()){
			IValue v = (IValue) iterator.next();
			
			// TODO: extract the proper static element type that will be generated
			pat.initMatch(ResultFactory.makeResult(v.getType(), v, ctx));
			while(pat.hasNext()){
				if(pat.next()){
					return true;						
				}	
			}
		}
		hasNext = false;
		return false;
	}

	@Override
	public Type getType(Environment env) {
		return TypeFactory.getInstance().boolType();
	}

	@Override
	public IValue toIValue(Environment env) {
		throw new ImplementationError("since enumerators can not occur on matching sides, you should not try to 'instantiate' them");
	}
}

