package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import java.util.HashSet;

import org.rascalmpl.values.parsetrees.ITree;

//import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

/**
 * Create a descendant descriptor given
 * 1: symbolset, array of symbols to be visited
 * 2: prodset, array of productions to be visited
 * 3: concreteMatch, indicates a concrete or abstract match
 */

public class DescendantDescriptor implements IDescendantDescriptor {
	private final HashSet<Object> mSymbolSet;
	private final boolean concreteMatch;
	final boolean debug = false;
	
	public DescendantDescriptor(Type[] symbolset, IConstructor[] prodset, IBool concreteMatch){
		mSymbolSet = new HashSet<Object>(symbolset.length + prodset.length);
		this.concreteMatch = concreteMatch.getValue();
		
		for(Type tp : symbolset){
			mSymbolSet.add(tp);								// Add as TYPE to the set
		}
		
		for(IConstructor cons: prodset){
			mSymbolSet.add(cons);							// Add the production itself to the set
		}
	}
	
	@Override
	public boolean isConcreteMatch(){
		return concreteMatch;
	}
	
	@Override
	public boolean isAllwaysTrue(){
		return false;
	}
	
	@Override
	public IBool shouldDescentInAbstractValue(final IValue subject) {
		//assert !concreteMatch : "shouldDescentInAbstractValue: abstract traversal required";
	
		Type type = subject instanceof IConstructor 
				    ? ((IConstructor) subject).getConstructorType().getAbstractDataType()
				    : subject.getType();
		IBool res = mSymbolSet.contains(type) ? TRUE : FALSE;
		if(debug) System.err.println("shouldDescentInAbstractValue(" + res + "): " + subject);
		return res;
	}
	
	@Override
	public IBool shouldDescentInConcreteValue(final ITree subject) {
		//assert concreteMatch : "shouldDescentInConcreteValue: concrete traversal required";
		if (subject.isAppl()) {
			IConstructor prod = (IConstructor) subject.getProduction();
			IBool res =  mSymbolSet.contains(prod) ? TRUE : FALSE;
			if(debug) System.err.println("shouldDescentInConcreteValue(" + res + "): " + subject);
			return res;
		}
		if (subject.isAmb()) {
			if(debug) System.err.println("shouldDescentInConcreteValue(" + true + "): " + subject);
			return TRUE;
		}
		if(debug) System.err.println("shouldDescentInConcreteValue(" + false + "): " + subject);
		return FALSE;
	}
}
