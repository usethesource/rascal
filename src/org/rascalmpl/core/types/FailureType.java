/*******************************************************************************
 * Copyright (c) 2009-2016 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.core.types;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory.RandomTypesConfig;
import io.usethesource.vallang.type.TypeFactory.TypeReifier;
import io.usethesource.vallang.type.TypeStore;
import org.rascalmpl.core.values.ValueFactoryFactory;

/**
 * A failure type is external to the type hierarchy. It is not even a sub-type of `value`.
 * It is used to continue after a partially succesfull type check and provide user feedback.
 */
public class FailureType extends RascalType {
	private final ISet messages;

	public FailureType(ISet messages) {
		this.messages = messages;
	}
	
	public ISet getMessages() {
	    return messages;
	}
	
	public static class Reifier implements TypeReifier {

        @Override
        public Type getSymbolConstructorType() {
            return symbols().typeSymbolConstructor("failure", tf().setType(tf().abstractDataType(new TypeStore(), "Message")), "messages");
        }

        @Override
        public Type fromSymbol(IConstructor symbol, TypeStore store,
                Function<IConstructor, Set<IConstructor>> grammar) {
            return RTF.failureType((ISet) symbol.get("messages"));
        }
	    
        @Override
        public IConstructor toSymbol(Type type, IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done) {
            return vf.constructor(getSymbolConstructorType(), ((FailureType) type).getMessages());
        }
        
        @Override
        public void asProductions(Type type, IValueFactory vf, TypeStore store, ISetWriter grammar,
                Set<IConstructor> done) {
            return; 
        }

        @Override
        public boolean isRecursive() {
            return false;
        }
        
        @Override
        public Type randomInstance(Supplier<Type> next, TypeStore store, RandomTypesConfig rnd) {
            return RascalTypeFactory.getInstance().failureType(ValueFactoryFactory.getValueFactory().set());
        }
	}
	
	@Override
	public TypeReifier getTypeReifier() {
	    return new Reifier();
	}
	
	@Override
	public Type asAbstractDataType() {
		return getTypeReifier().symbols().symbolADT();
	}
	
	@Override
	public boolean isFailure() {
		return true;
	}
	
	@Override
	public String getName() {
		return "failure";
	}

	@Override
	public <T, E extends Throwable> T accept(IRascalTypeVisitor<T, E> visitor) throws E {
	  return visitor.visitFailureType(this);
	}
	
	@Override
	protected boolean isSupertypeOf(RascalType type) {
	  return type.isSubtypeOfFailure(this);
	}

	@Override
	protected Type lubWithValue(Type type) {
	    return this;
	}
	
	@Override
	protected Type lubWithVoid(Type type) {
	    return this;
	}
	
	@Override
	protected Type glbWithVoid(Type type) {
	    return this;
	}
	
	@Override
	protected Type glbWithValue(Type type) {
	    return this;
	}
	
	@Override
	protected Type lub(RascalType type) {
	  return type.lubWithFailure(this);
	}
	
	@Override
	protected Type glb(RascalType type) {
		return type.glbWithFailure(this);
	}
	
	@Override
	protected boolean isSubtypeOfValue(Type type) {
	    // failures never fit anywhere, not even for values!
	    return false;
	}
	
	@Override
	public boolean isSubtypeOfFailure(RascalType type) {
	    // failures never fit anywhere, not even with themselves.
	    return false;
	}

	@Override
	protected Type lubWithFailure(RascalType type) {
	    // let's just collect error messages here
	    return RascalTypeFactory.getInstance().failureType(messages.union(((FailureType) type).getMessages()));
	}
	
	@Override
	protected Type glbWithFailure(RascalType type) {
	    // let's just collect error messages here
	    return RascalTypeFactory.getInstance().failureType(messages.union(((FailureType) type).getMessages()));
	}
	
	
	@Override
	public String toString() {
	    return "failure[" + getMessages() + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if (obj.getClass() == getClass()) {
			return messages.equals(((FailureType) obj).messages);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 93 + messages.hashCode();
	}

	@Override
	public IValue randomValue(Random random, IValueFactory vf, TypeStore store, Map<Type, Type> typeParameters,
			int maxDepth, int maxBreadth) {
		throw new RuntimeException("randomValue not implemented for FailureType");
	}
}
