package org.meta_environment.rascal.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Strategy;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedOperationError;

public class IteratorFactory {
	
	public static Iterator<IValue> make(IEvaluatorContext ctx, IMatchingResult matchPattern, 
			                              Result<IValue> subject, Strategy strategy, boolean shallow){
		
		Type subjectType = subject.getType();
		IValue subjectValue = subject.getValue();
		Type patType = matchPattern.getType(ctx.getCurrentEnvt());
		
		// TODO: this should be a visitor design as well..
		
		while (subjectType.isAliasType()) {
			subjectType = subjectType.getAliased();
		}
		
		// List
		if(subjectType.isListType()){
			checkNoStrategy(subjectType, strategy, ctx);
			//TODO: we could do this more precisely
			if(!subjectType.getElementType().isVoidType())
				checkMayOccur(patType, subjectType.getElementType(), ctx);
			return ((IList) subjectValue).iterator();
			
		// Set
		} else 	if(subjectType.isSetType()){
			checkNoStrategy(subjectType, strategy, ctx);
			if(!subjectType.getElementType().isVoidType())
				checkMayOccur(patType, subjectType.getElementType(), ctx);
			return ((ISet) subjectValue).iterator();
		
		// Map
		} else if(subjectType.isMapType()){
			checkNoStrategy(subjectType, strategy, ctx);
			if(!subjectType.getKeyType().isVoidType())
				checkMayOccur(patType, subjectType.getKeyType(), ctx);
			return ((IMap) subjectValue).iterator();
			
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
			checkMayOccur(patType, subjectType, ctx);
			if(shallow)
				return	new NodeChildIterator((INode) subjectValue);
			else
				return new NodeReader((INode) subjectValue, bottomup);
		} else if(subjectType.isTupleType()){
			checkNoStrategy(subjectType, strategy, ctx);
			int nElems = subjectType.getArity();
			for(int i = 0; i < nElems; i++){
				if(!subjectType.getFieldType(i).isSubtypeOf(patType)) {
					throw new UnexpectedTypeError(patType, subjectType.getFieldType(i), ctx.getCurrentAST());
				}
			}
			return new TupleElementIterator((ITuple)subjectValue);
			
		} else if(subjectType.isBoolType() ||
				subjectType.isIntegerType() ||
				subjectType.isRealType() ||
				subjectType.isStringType() ||
				subjectType.isSourceLocationType())
				{
			checkNoStrategy(subjectType, strategy, ctx);
			if(!subjectType.isSubtypeOf(patType)) {
				throw new UnexpectedTypeError(patType, subjectType, ctx.getCurrentAST());
			}
			return new SingleIValueIterator(subjectValue);
		} else {
			throw new UnsupportedOperationError("makeIterator", subjectType, ctx.getCurrentAST());
		}
	}
	
	private static void checkNoStrategy(Type rType, Strategy strategy, IEvaluatorContext ctx){
		if(strategy != null) {
			throw new UnsupportedOperationError(strategy.toString(), rType, ctx.getCurrentAST());
		}
	}
	
	private static void checkMayOccur(Type patType, Type rType, IEvaluatorContext ctx){
		if(!ctx.getEvaluator().mayOccurIn(patType, rType))
			throw new UnexpectedTypeError(patType, rType, ctx.getCurrentAST());
	}

	
}
