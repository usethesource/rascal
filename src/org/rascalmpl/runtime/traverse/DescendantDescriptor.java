/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
