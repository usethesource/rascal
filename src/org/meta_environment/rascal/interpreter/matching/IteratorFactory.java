package org.meta_environment.rascal.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedOperationError;
import org.meta_environment.rascal.interpreter.types.NonTerminalType;
import org.meta_environment.uptr.SymbolAdapter;

public class IteratorFactory {
	
	public static Iterator<IValue> make(IEvaluatorContext ctx, IMatchingResult matchPattern, 
			                              Result<IValue> subject, boolean shallow){
		
		Type subjectType = subject.getType();
		IValue subjectValue = subject.getValue();
		Type patType = matchPattern.getType(ctx.getCurrentEnvt());
		
		// TODO: this should be a visitor design as well..
		
		//TODO: why should be managed by the getAliased() method or 
		// directly by the constructor of AliasType?
		while (subjectType.isAliasType()) {
			subjectType = subjectType.getAliased();
		}
		
		// List
		if(subjectType.isListType()){
			//TODO: we could do this more precisely
			if(!subjectType.getElementType().isVoidType())
				checkMayOccur(patType, subjectType.getElementType(), ctx, shallow);
			if(shallow)
				return ((IList) subjectValue).iterator();
			return new DescendantReader(subjectValue);
			
		// Set
		} else 	if(subjectType.isSetType()){
			if(!subjectType.getElementType().isVoidType())
				checkMayOccur(patType, subjectType.getElementType(), ctx, shallow);
			if(shallow)
				return ((ISet) subjectValue).iterator();
			return new DescendantReader(subjectValue);
		
		// Map
		} else if(subjectType.isMapType()){
			if(!subjectType.getKeyType().isVoidType())
				checkMayOccur(patType, subjectType.getKeyType(), ctx, shallow);
			if(shallow)
				return ((IMap) subjectValue).iterator();
			return new DescendantReader(subjectValue);
			
		// NonTerminal	
		} else if(subjectType.isExternalType()){
			if(subjectType instanceof NonTerminalType){
				
				IConstructor tree = (IConstructor) subjectValue;
				checkMayOccur(patType, subjectType, ctx, shallow);
				NonTerminalType nt = (NonTerminalType) subjectType;
				
				if(nt.isConcreteListType()){
					IConstructor listSymbol = nt.getSymbol();
					int delta = SymbolAdapter.isSepList(listSymbol)? 4 : 2;
					// TODO !shallow case?
					return new CFListIterator((IList)tree.get(1), delta);
				}
			}
			return new SingleIValueIterator(subjectValue);
			
		// Node and ADT
		} else if(subjectType.isNodeType() || subjectType.isAbstractDataType()){

			checkMayOccur(patType, subjectType, ctx, shallow);
			if(shallow)
				return new NodeChildIterator((INode) subjectValue);
			
			return new DescendantReader(subjectValue);
		} else if(subjectType.isTupleType()){
			if(shallow){
				int nElems = subjectType.getArity();
				for(int i = 0; i < nElems; i++){
					if(!subjectType.getFieldType(i).isSubtypeOf(patType)) {
						throw new UnexpectedTypeError(patType, subjectType.getFieldType(i), ctx.getCurrentAST());
					}
				}
				return new TupleElementIterator((ITuple)subjectValue);
			}
			return new DescendantReader(subjectValue);
			
		} else if(subjectType.isBoolType() ||
				subjectType.isIntegerType() ||
				subjectType.isRealType() ||
				subjectType.isStringType() ||
				subjectType.isSourceLocationType())
				{
			if(shallow && !subjectType.isSubtypeOf(patType)) {
				throw new UnexpectedTypeError(patType, subjectType, ctx.getCurrentAST());
			}
			return new SingleIValueIterator(subjectValue);
		} else {
			throw new UnsupportedOperationError("makeIterator", subjectType, ctx.getCurrentAST());
		}
	}
	
	private static void checkMayOccur(Type patType, Type rType, IEvaluatorContext ctx, boolean shallow){
		if(shallow && !ctx.getEvaluator().mayOccurIn(rType, patType))
			throw new UnexpectedTypeError(patType, rType, ctx.getCurrentAST());
	}
	
}
