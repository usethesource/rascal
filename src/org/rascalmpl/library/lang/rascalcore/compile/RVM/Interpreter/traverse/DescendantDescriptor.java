package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse;

import java.util.HashSet;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import org.rascalmpl.values.uptr.ITree;

/**
 * Create a descendant descriptor given
 * 1: symbolset, ISet of symbols
 * 2: prodset, ISet of productions
 * 3: concreteMatch, indicates a concrete or abstract match
 * 
 * [ IString id, ISet symbolset, ISet prodset, IBool concreteMatch] => descendant_descriptor
 */

public class DescendantDescriptor {
	private final HashSet<Object> mSymbolSet;
	private final boolean concreteMatch;
	private final boolean containsNodeOrValueType;
	
	public DescendantDescriptor(ISet symbolset, ISet prodset, IMap definitions, IBool concreteMatch, RascalExecutionContext rex){
		mSymbolSet = new HashSet<Object>(symbolset.size() + prodset.size());
		this.concreteMatch = concreteMatch.getValue();
		boolean nodeOrValue = true;
		
		for(IValue v : symbolset){
			Type tp = rex.symbolToType((IConstructor) v, definitions);
			mSymbolSet.add(tp);								// Add as TYPE to the set
			if(tp == RascalPrimitive.nodeType || tp == RascalPrimitive.valueType){
				nodeOrValue = true;
			}
		}
		
		for(IValue v : prodset){
			IConstructor cons = (IConstructor) v;
			mSymbolSet.add(cons);							// Add the production itself to the set
		}

		containsNodeOrValueType = nodeOrValue;
	}
	
	public boolean isConcreteMatch(){
		return concreteMatch;
	}
	
	public boolean isAllwaysTrue(){
		return containsNodeOrValueType;
	}
	
	public IBool shouldDescentInAbstractValue(final IValue subject) {
		//assert !concreteMatch : "shouldDescentInAbstractValue: abstract traversal required";
		//System.out.println("shouldDescentInAbstractValue: " + ++counter + ", " + subject.toString());
		if (containsNodeOrValueType) {
			return RascalPrimitive.Rascal_TRUE;
		}
		Type type = subject instanceof IConstructor 
				    ? ((IConstructor) subject).getConstructorType() 
				    : subject.getType();
		return mSymbolSet.contains(type) ? RascalPrimitive.Rascal_TRUE : RascalPrimitive.Rascal_FALSE;
	}
	
	public IBool shouldDescentInConcreteValue(final ITree subject) {
		//assert concreteMatch : "shouldDescentInConcreteValue: concrete traversal required";
		if (subject.isAppl()) {
			if (containsNodeOrValueType) {
				return RascalPrimitive.Rascal_TRUE;
			}
			IConstructor prod = (IConstructor) subject.getProduction();
			return mSymbolSet.contains(prod) ? RascalPrimitive.Rascal_TRUE : RascalPrimitive.Rascal_FALSE;
		}
		if (subject.isAmb()) {
			return RascalPrimitive.Rascal_TRUE;
		}
		return RascalPrimitive.Rascal_FALSE;
	}
	
//	public IBool shouldDescentInType(Type type) {
//		assert !concreteMatch : "shouldDescentInType: abstract traversal required";
//		if (containsNodeOrValueType) {
//			return RascalPrimitive.Rascal_TRUE;
//		}
//		return mSymbolSet.contains(type) ? RascalPrimitive.Rascal_TRUE : RascalPrimitive.Rascal_FALSE;
//	}
	
}
