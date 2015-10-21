/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jurgen Vinju - initial API and implementation
 *******************************************************************************/
package org.rascalmpl.value.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.ExternalType;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.visitors.IValueVisitor;

import io.usethesource.capsule.AbstractSpecialisedImmutableMap;
import io.usethesource.capsule.ImmutableMap;

/**
 * IExternalValue, together with {@link ExternalType} offer a limited form of extensibility
 * to the PDB's value and type system. The IExternalValue interface is used to tag extensions,
 * such as 'function values' that are not part of the PDB fact exchange and manipulation
 * interfaces but do need to integrate with them.
 * <br>
 * Note that implementations of IExternalValues are obliged to have a type that subclasses
 * ExternalType and that they all implement encodeAsConstructor. 
 * If you do not do this, (de)serialization will not work.
 * <br>
 * Note that NORMAL USE OF THE PDB DOES NOT REQUIRE IMPLEMENTING THIS INTERFACE
 */
public abstract class AbstractExternalValue implements IExternalValue {

	@Override
	public boolean isEqual(IValue other) {
		return equals(other);
	}

	public static IConstructor encodeAsConstructor(IExternalValue instance) {
		return new IConstructor() {
			@Override
			public Type getConstructorType() {
				return TypeFactory.getInstance().constructor(new TypeStore(), getType(), getName(), new Type[0]);
			}
			
			@Override
			public Type getType() {
				return ((ExternalType) getType()).asAbstractDataType();
			}

			@Override
			public String getName() {
				return instance.getClass().getSimpleName().toLowerCase();
			}

			@Override
			public Type getUninstantiatedConstructorType() {
				return getConstructorType();
			}

			@Override
			public IValue get(String label) {
				return null;
			}

			@Override
			public IConstructor set(String label, IValue newChild) throws FactTypeUseException {
				return this;
			}

			@Override
			public boolean has(String label) {
				return false;
			}

			@Override
			public IConstructor set(int index, IValue newChild) {
				return this;
			}

			@Override
			public Type getChildrenTypes() {
				return TypeFactory.getInstance().voidType();
			}

			@Override
			public boolean declaresAnnotation(TypeStore store, String label) {
				return false;
			}

			@Override
			public <T, E extends Throwable> T accept(IValueVisitor<T, E> v)
					throws E {
				return v.visitConstructor(this);
			}

			@Override
			public boolean isEqual(IValue other) {
				return equals(other);
			}

			@Override
			public boolean isAnnotatable() {
				return true;
			}

			@Override
			public boolean mayHaveKeywordParameters() {
				return true;
			}

			@Override
			public INode replace(int first, int second, int end, IList repl)
					throws FactTypeUseException, IndexOutOfBoundsException {
				return this;
			}
			
			@Override
			public int arity() {
				return 0;
			}
			
			@Override
			public IValue get(int i) throws IndexOutOfBoundsException {
				return null;
			}
			
			@Override
			public Iterable<IValue> getChildren() {
				return Collections.emptyList();
			}
			
			
			@Override
			public Iterator<IValue> iterator() {
				return Collections.emptyIterator();
			}
			
			
			@Override
			public IAnnotatable<? extends IConstructor> asAnnotatable() {
				return new AbstractDefaultAnnotatable<IConstructor>(this) {
					@Override
					protected IConstructor wrap(IConstructor content,
							ImmutableMap<String, IValue> annotations) {
						return new AnnotatedConstructorFacade(content, annotations);
					}
				};
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
		};		
	}
	
	@Override
	public IConstructor encodeAsConstructor() {
		return encodeAsConstructor(this);
	}
	
	@Override
	public boolean isAnnotatable() {
		return false;
	}
	
	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean mayHaveKeywordParameters() {
		return false;
	}
	
	@Override
	public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitExternal(this);
	}
}
