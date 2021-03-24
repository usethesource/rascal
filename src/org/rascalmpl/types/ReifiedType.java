/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.types;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.rascalmpl.types.TypeReifier.TypeStoreWithSyntax;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeFactory.RandomTypesConfig;
import io.usethesource.vallang.type.TypeFactory.TypeReifier;
import io.usethesource.vallang.type.TypeStore;

/**
 * A reified type is the type of a value that represents a type. It is parametrized by the type
 * it represents.
 * 
 * For example, the '#int' expression produces the 'type(int(),())' value which is of type 'type[int]'
 */
public class ReifiedType extends RascalType {
	private final Type arg;

	public ReifiedType(Type arg) {
		this.arg = arg;
	}
	
	public static class Reifier implements TypeReifier {

        @Override
        public Type getSymbolConstructorType() {
            return symbols().typeSymbolConstructor("reified", symbols().symbolADT(), "symbol");
        }

        @Override
        public Type fromSymbol(IConstructor symbol, TypeStore store,
                Function<IConstructor, Set<IConstructor>> grammar) {
            return RTF.reifiedType(symbols().fromSymbol((IConstructor) symbol.get("symbol"), store, grammar));
        }
	    
        @Override
        public IConstructor toSymbol(Type type, IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done) {
            return vf.constructor(getSymbolConstructorType(), type.getTypeParameters().getFieldType(0).asSymbol(vf, store, grammar, done));
        }
        
        @Override
        public void asProductions(Type type, IValueFactory vf, TypeStore store, ISetWriter grammar,
                Set<IConstructor> done) {
            ((ReifiedType) type).arg.asProductions(vf, store, grammar, done);
        }

        @Override
        public boolean isRecursive() {
            return true;
        }
        
        @Override
        public Type randomInstance(Supplier<Type> next, TypeStore store, RandomTypesConfig rnd) {
            // TODO: for now the interpreter tests can not handle this yet, so we return something else
//            return RascalTypeFactory.getInstance().reifiedType(next.get());
            return TypeFactory.getInstance().boolType();
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
	public boolean isReified() {
		return true;
	}
	
	@Override
	public boolean isAbstractData() {
	    // the reified constructor is a special kind of constructor.
	    return true;
	}
	
	@Override
	public String getName() {
		return "type";
	}

	@Override
	public boolean hasField(String fieldName) {
		return fieldName.equals("symbol") || fieldName.equals("definitions"); 
	}
	
	@Override
	public boolean hasKeywordField(String fieldName, TypeStore store) {
	    return false;
	}
	
	@Override
	public boolean isParameterized() {
		return true;
	}
	
	@Override
	public boolean isOpen() {
	  return arg.isOpen();
	}
	
	@Override
	public <T, E extends Throwable> T accept(IRascalTypeVisitor<T, E> visitor) throws E {
	  return visitor.visitReified(this);
	}
	
	@Override
	protected boolean isSupertypeOf(RascalType type) {
	  return type.isSubtypeOfReified(this);
	}
	
	@Override
	protected Type lub(RascalType type) {
	  return type.lubWithReified(this);
	}
	
	@Override
	protected Type glb(RascalType type) {
		return type.glbWithReified(this);
	}
	
	@Override
	public boolean intersects(Type other) {
		if (other instanceof RascalType) {
			return ((RascalType) other).intersectsWithReified(this);
		}
		else {
			return false;
		}
	}
	
	@Override
	protected boolean intersects(RascalType type) {
	    return type.intersectsWithReified(this);
	}
	
	@Override
	protected boolean intersectsWithReified(RascalType type) {
	    return arg.intersects(((ReifiedType) type).arg);
	}
	
	@Override
	public Type getTypeParameters() {
		return TypeFactory.getInstance().tupleType(arg);
	}
	
	@Override
	protected boolean isSubtypeOfNode(Type type) {
	  return true;
	}
	
	@Override
	protected boolean isSubtypeOfReified(RascalType type) {
	  return arg.isSubtypeOf(((ReifiedType) type).arg);
	}
	
	@Override
	protected Type lubWithNode(Type type) {
	  return type;
	}
	
	@Override
	protected Type lubWithReified(RascalType type) {
	  return RascalTypeFactory.getInstance().reifiedType(arg.lub(((ReifiedType) type).arg));
	}
	
	@Override
	protected Type glbWithNode(Type type) {
	  return this;
	}
	
	@Override
	protected Type glbWithReified(RascalType type) {
	  return RascalTypeFactory.getInstance().reifiedType(arg.glb(((ReifiedType) type).arg));
	}
	
	@Override
	public boolean match(Type matched, Map<Type, Type> bindings)
			throws FactTypeUseException {
		if (matched instanceof ReifiedType) {
			return super.match(matched, bindings)
					&& arg.match(((ReifiedType) matched).arg, bindings);
		}
		else if (matched.isBottom()) {
			return arg.match(matched, bindings);
		}
		else {
			return false;
		}
	}
	
	@Override
	public Type instantiate(Map<Type, Type> bindings) {
		return RascalTypeFactory.getInstance().reifiedType(arg.instantiate(bindings));
	}
	
	@Override
	public String toString() {
		return "type[" + arg.toString() + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj.getClass() == getClass()) {
			return arg.equals(((ReifiedType) obj).arg);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 2331 + arg.hashCode();
	}
	
	@Override
	public IValue randomValue(Random random, IValueFactory vf, TypeStore store, Map<Type, Type> typeParameters,
	    int maxDepth, int maxBreadth) {
	    
	    if (store instanceof TypeStoreWithSyntax) {
	        TypeStoreWithSyntax ts = (TypeStoreWithSyntax) store;
	        return new org.rascalmpl.types.TypeReifier(vf).typeToValue(arg, store, ts.getGrammar());
	    }
	    else {
	        return new org.rascalmpl.types.TypeReifier(vf).typeToValue(arg, store, vf.map());
	    }
	}
}
