package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import java.util.HashSet;

import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;

//import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

/**
 * Create a descendant descriptor given
 * 1: symbolset, ISet of symbols
 * 2: prodset, ISet of productions
 * 3: concreteMatch, indicates a concrete or abstract match
 * 
 * [ IString id, ISet symbolset, ISet prodset, IBool concreteMatch] => descendant_descriptor
 */

public class DescendantDescriptor {
	private /*final*/ HashSet<Object> mSymbolSet;
	private /*final*/ boolean concreteMatch;
	private /*final*/ boolean containsNodeOrValueType;
	
	public DescendantDescriptor(Type[] symbolset, IConstructor[] prodset, IBool concreteMatch){
		mSymbolSet = new HashSet<Object>(symbolset.length + prodset.length);
		this.concreteMatch = concreteMatch.getValue();
		boolean nodeOrValue = true;
		
		for(Type tp : symbolset){
			mSymbolSet.add(tp);								// Add as TYPE to the set
//			if(tp == TF.AType_anode || tp == TF.AType_avalue){
//				nodeOrValue = true;
//			}
		}
		
		for(IConstructor cons: prodset){
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
			return IRascalValueFactory.getInstance().bool(true);
		}
		Type type = subject instanceof IConstructor 
				    ? ((IConstructor) subject).getConstructorType() 
				    : subject.getType();
		return mSymbolSet.contains(type) ? IRascalValueFactory.getInstance().bool(true) : IRascalValueFactory.getInstance().bool(false);
	}
	
	public IBool shouldDescentInConcreteValue(final ITree subject) {
		//assert concreteMatch : "shouldDescentInConcreteValue: concrete traversal required";
		if (subject.isAppl()) {
			if (containsNodeOrValueType) {
				return IRascalValueFactory.getInstance().bool(true);
			}
			IConstructor prod = (IConstructor) subject.getProduction();
			return mSymbolSet.contains(prod) ? IRascalValueFactory.getInstance().bool(true) : IRascalValueFactory.getInstance().bool(false);
		}
		if (subject.isAmb()) {
			return IRascalValueFactory.getInstance().bool(true);
		}
		return IRascalValueFactory.getInstance().bool(false);
	}
	
//	public IBool shouldDescentInType(Type type) {
//		assert !concreteMatch : "shouldDescentInType: abstract traversal required";
//		if (containsNodeOrValueType) {
//			return IRascalValueFactory.getInstance().bool(true);
//		}
//		return mSymbolSet.contains(type) ? IRascalValueFactory.getInstance().bool(true) : IRascalValueFactory.getInstance().bool(false);
//	}
	
}
