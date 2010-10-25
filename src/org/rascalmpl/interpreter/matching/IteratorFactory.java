package org.rascalmpl.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperationError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class IteratorFactory {
	
	public static Iterator<IValue> make(IEvaluatorContext ctx, IMatchingResult matchPattern, 
			                              Result<IValue> subject, boolean shallow){
		
		Type subjectType = subject.getType();
		IValue subjectValue = subject.getValue();
		Type patType = matchPattern.getType(ctx.getCurrentEnvt());
		
		// TODO: this should be a visitor design as well..
		
		// List
		if(subjectType.isListType()){
			//TODO: we could do this more precisely				
			if(shallow){
				checkMayOccur(patType, subjectType.getElementType(), ctx);
				return ((IList) subjectValue).iterator();
			}
			return new DescendantReader(subjectValue, false);
			
		// Set
		} else 	if(subjectType.isSetType()){				
			if(shallow){
				checkMayOccur(patType, subjectType.getElementType(), ctx);
				return ((ISet) subjectValue).iterator();
			}
			return new DescendantReader(subjectValue, false);
		
		// Map
		} else if(subjectType.isMapType()){				
			if(shallow){
				checkMayOccur(patType, subjectType.getKeyType(), ctx);
				return ((IMap) subjectValue).iterator();
			}
			return new DescendantReader(subjectValue, false);
			
		// NonTerminal (both pattern and subject are non-terminals, so we can skip layout and stuff)
		} else if(subjectType.isExternalType()){
			if(subjectType instanceof NonTerminalType){
				IConstructor tree = (IConstructor) subjectValue;
				NonTerminalType nt = (NonTerminalType) subjectType;
				
				if(nt.isConcreteListType()){
					if(shallow){
						checkMayOccur(patType, subjectType, ctx);
						IConstructor listSymbol = nt.getSymbol();
						int delta = SymbolAdapter.isSepList(listSymbol)? 4 : 2;
						return new CFListIterator((IList)tree.get(1), delta);
					}
					
				}
				
				if(shallow)
					checkMayOccur(patType, subjectType, ctx);
				
				return new DescendantReader(tree, patType instanceof NonTerminalType);
				
			}
			return new SingleIValueIterator(subjectValue);
			
		// Node and ADT
		} else if(subjectType.isNodeType() || subjectType.isAbstractDataType()){			
			if(shallow){
				checkMayOccur(patType, subjectType, ctx);
				return new NodeChildIterator((INode) subjectValue);
			}
			return new DescendantReader(subjectValue, false);
			
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
			return new DescendantReader(subjectValue, false);
			
		} else if(subjectType.isBoolType() ||
				subjectType.isIntegerType() ||
				subjectType.isRealType() ||
				subjectType.isStringType() ||
				subjectType.isSourceLocationType() ||
				subjectType.isDateTimeType())
				{
			if(shallow && !subjectType.isSubtypeOf(patType)) {
				throw new UnexpectedTypeError(patType, subjectType, ctx.getCurrentAST());
			}
			return new SingleIValueIterator(subjectValue);
		} else {
			throw new UnsupportedOperationError("makeIterator", subjectType, ctx.getCurrentAST());
		}
	}
	
	private static void checkMayOccur(Type patType, Type rType, IEvaluatorContext ctx){
		if(!ctx.getEvaluator().mayOccurIn(rType, patType))
			throw new UnexpectedTypeError(rType, patType, ctx.getCurrentAST());
	}
	
}
