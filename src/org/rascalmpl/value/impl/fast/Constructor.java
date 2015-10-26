/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*******************************************************************************/
package org.rascalmpl.value.impl.fast;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.impl.AbstractDefaultAnnotatable;
import org.rascalmpl.value.impl.AbstractDefaultWithKeywordParameters;
import org.rascalmpl.value.impl.AbstractValue;
import org.rascalmpl.value.impl.AnnotatedConstructorFacade;
import org.rascalmpl.value.impl.ConstructorWithKeywordParametersFacade;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.visitors.IValueVisitor;

import io.usethesource.capsule.AbstractSpecialisedImmutableMap;
import io.usethesource.capsule.ArrayIterator;
import io.usethesource.capsule.ImmutableMap;

/**
 * Implementation of IConstructor.
 * <br /><br />
 * Constructors that are annotated will use the AnnotatedConstructor class instead.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class Constructor extends AbstractValue implements IConstructor {
	protected final Type constructorType;
	protected final IValue[] children;
	private int hashCode = 0;

	/*package*/ static IConstructor newConstructor(Type constructorType, IValue[] children) {
		return new Constructor(constructorType, children); 
	}
	
	/*package*/ static IConstructor newConstructor(Type constructorType, IValue[] children, Map<String,IValue> kwParams) {
	  IConstructor r = new Constructor(constructorType, children);
	  
	  if (kwParams != null && !kwParams.isEmpty()) {
	    return r.asWithKeywordParameters().setParameters(kwParams);
	  }
	  
	  return r;
	}
	
	private Constructor(Type constructorType, IValue[] children){
		super();
		
		this.constructorType = constructorType;
		this.children = children;
	}
	
	@Override
	public Type getUninstantiatedConstructorType() {
	  return constructorType;
	}
	
	@Override
	public Type getType(){
		return getConstructorType().getAbstractDataType();
	}
	
	@Override
	public Type getConstructorType(){
	  if (constructorType.getAbstractDataType().isParameterized()) {
      // this assures we always have the most concrete type for constructors.
      Type[] actualTypes = new Type[children.length];
      for (int i = 0; i < children.length; i++) {
        actualTypes[i] = children[i].getType();
      }
    
      Map<Type,Type> bindings = new HashMap<Type,Type>();
      constructorType.getFieldTypes().match(TypeFactory.getInstance().tupleType(actualTypes), bindings);
      
      for (Type field : constructorType.getAbstractDataType().getTypeParameters()) {
        if (!bindings.containsKey(field)) {
          bindings.put(field, TypeFactory.getInstance().voidType());
        }
      }
      
      return constructorType.instantiate(bindings);
    }
	  
		return constructorType;
	}
	
	@Override
	public Type getChildrenTypes(){
		return constructorType.getFieldTypes();
	}

	@Override
	public String getName(){
		return constructorType.getName();
	}
	
	@Override
	public int arity(){
		return children.length;
	}

	@Override
	public IValue get(int i){
		return children[i];
	}
	
	@Override
	public IValue get(String label){
		return get(constructorType.getFieldIndex(label));
	}

	@Override
	public Iterable<IValue> getChildren(){
		return this;
	}

	@Override
	public Iterator<IValue> iterator(){
		return ArrayIterator.of(children);
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E{
		return v.visitConstructor(this);
	}
	
	@Override
	public IConstructor set(int i, IValue newChild){
		IValue[] newChildren = children.clone();
		newChildren[i] = newChild;
		
		return newConstructor(constructorType, newChildren);
	}
	
	@Override
	public IConstructor set(String label, IValue newChild){
		IValue[] newChildren = children.clone();
		newChildren[constructorType.getFieldIndex(label)] = newChild;
		
		return newConstructor(constructorType, newChildren);
	}
	
	@Override
	public boolean declaresAnnotation(TypeStore store, String label) {
		return (store.getAnnotationType(constructorType.getAbstractDataType(), label) != null);
	}
	
	@Override
	public int hashCode(){
		if (hashCode == 0) {
			hashCode = constructorType.hashCode();
			
			for(int i = children.length - 1; i >= 0; i--){
				hashCode = (hashCode << 23) + (hashCode >> 5);
				hashCode ^= children[i].hashCode();
			}
		}
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			Constructor otherTree = (Constructor) o;
			
			if(constructorType != otherTree.constructorType) return false;
			
			IValue[] otherChildren = otherTree.children;
			int nrOfChildren = children.length;
			if(otherChildren.length == nrOfChildren){
				for(int i = nrOfChildren - 1; i >= 0; i--){
					if(!otherChildren[i].equals(children[i])) return false;
				}
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isEqual(IValue value){
		if(value == this) return true;
		if(value == null) return false;
		
		if(value instanceof IConstructor){
			IConstructor otherTree = (IConstructor) value;
			
			if(!constructorType.comparable(otherTree.getConstructorType())) {
			  return false;
			}
			
			final Iterator<IValue> it1 = this.iterator();
			final Iterator<IValue> it2 = otherTree.iterator();

			while (it1.hasNext() && it2.hasNext()) {
				// call to IValue.isEqual(IValue)
				if (it1.next().isEqual(it2.next()) == false) {
					return false;
				}
			}  

			// TODO: this can be optimized when annotations are removed
			if (mayHaveKeywordParameters() && otherTree.mayHaveKeywordParameters()) {
			  return asWithKeywordParameters().equalParameters(otherTree.asWithKeywordParameters());
			}
			
			// TODO: this can be optimized when annotations are removed
			if (mayHaveKeywordParameters() && asWithKeywordParameters().hasParameters()) {
			  return false;
			}
			
			// TODO: this can be optimized when annotations are removed
			if (otherTree.mayHaveKeywordParameters() && otherTree.asWithKeywordParameters().hasParameters()) {
			  return false;
			}
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean has(String label) {
		return getConstructorType().hasField(label);
	}

	@Override
	public IConstructor replace(int first, int second, int end, IList repl)
			throws FactTypeUseException, IndexOutOfBoundsException {
		
		throw new UnsupportedOperationException("Replace not supported on constructor.");
	}
	
	/**
	 * TODO: Create and move to {@link AbstractConstructor}.
	 */
	@Override
	public boolean isAnnotatable() {
		return true;
	}
	
	/**
	 * TODO: Create and move to {@link AbstractConstructor}.
	 */
	@Override
	public IAnnotatable<IConstructor> asAnnotatable() {
		return new AbstractDefaultAnnotatable<IConstructor>(this) {
			@Override
			protected IConstructor wrap(IConstructor content,
					ImmutableMap<String, IValue> annotations) {
				return new AnnotatedConstructorFacade(content, annotations);
			}
		};
	}
	
	@Override
	public boolean mayHaveKeywordParameters() {
	  return true;
	}
	
	@Override
	public IWithKeywordParameters<IConstructor> asWithKeywordParameters() {
	  return new AbstractDefaultWithKeywordParameters<IConstructor>(this, AbstractSpecialisedImmutableMap.<String,IValue>mapOf()) {
	    @Override
	    protected IConstructor wrap(IConstructor content, ImmutableMap<String, IValue> parameters) {
	      return new ConstructorWithKeywordParametersFacade(content, parameters);
	    }
	    
	    @Override
	    public boolean hasParameters() {
	    	return false;
	    }

	    @Override
	    public java.util.Set<String> getParameterNames() {
	    	return Collections.emptySet();
	    }

	    @Override
	    public Map<String, IValue> getParameters() {
	    	return Collections.unmodifiableMap(parameters);
	    }
	  }; 
	}

}
