/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
*******************************************************************************/
package org.rascalmpl.library.util.tasks;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.tasks.IIValueTask;
import org.rascalmpl.tasks.ITaskRegistry;
import org.rascalmpl.tasks.ITransaction;
import org.rascalmpl.tasks.PDBValueTaskRegistry;
import org.rascalmpl.tasks.Transaction;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class Manager {
	private Transaction base = null;
	private final ITaskRegistry<Type, IValue, IValue> registry;
	private final IValueFactory vf;
	private final TypeReifier typeReifier;
	private final Map<IValueWrapper,ProducerWrapper> producers = new HashMap<IValueWrapper,ProducerWrapper>();
    private final PrintWriter err;
    private final IRascalMonitor monitor;
	
	public Manager(IValueFactory vf, PrintWriter out, PrintWriter err, IRascalMonitor monitor) {
		this.vf = vf;
		this.err = err;
		this.monitor = monitor;
		this.typeReifier = new TypeReifier(vf);
		this.registry = PDBValueTaskRegistry.getRegistry();
	}
	
	public void lockProducerRegistry() {
		registry.lock();
	}
	
	public void unlockProducerRegistry() {
		registry.unlock();
	}
	
	public IValue startTransaction() {
		if(base == null)
			base = new Transaction(err);
		return new Transaction(base, err);
	}
	
	public IValue startTransaction(IValue tr) {
		return new Transaction(transaction(tr), err);
	}
	
	public void endTransaction(IValue tr) {
		transaction(tr).commit();
	}

	public void abandonTransaction(IValue tr) {
		transaction(tr).abandon();
	}
	
	public IValue getFact(IValue tr, IValue key, IValue name) {
		if(!(key instanceof IConstructor))
			throw RuntimeExceptionFactory.illegalArgument(key, "key is not a reified type");

		IValue fact = transaction(tr).getFact(monitor, typeReifier.valueToType((IConstructor) key), name);
		if(fact == null)
			fact = null; // <- put breakpoint here!
		return check(fact, (IConstructor) key, name);
	}

	private IValue check(IValue fact, IValue key, IValue name) {
		if(!(key instanceof IConstructor))
			throw RuntimeExceptionFactory.illegalArgument(key, "key is not a reified type");

		if(fact == null) {
			throw RuntimeExceptionFactory.noSuchKey(vf.string(typeReifier.valueToType((IConstructor) key).toString() + ":" + name.toString()));
		}
		else if(!fact.getType().isSubtypeOf(typeReifier.valueToType((IConstructor) key))) {
		    throw RuntimeExceptionFactory.illegalArgument(fact, "fact is not a subtype of " + typeReifier.valueToType((IConstructor) key));
		}
		else
			return fact;
	}

	public IValue queryFact(IValue tr, IValue key, IValue name) {
		if(!(key instanceof IConstructor))
			throw RuntimeExceptionFactory.illegalArgument(key, "key is not a reified type");

		return check(transaction(tr).queryFact(typeReifier.valueToType((IConstructor) key), name), key, name);
	}

	public void removeFact(IValue tr, IValue key, IValue name) {
		if(!(key instanceof IConstructor))
			throw RuntimeExceptionFactory.illegalArgument(key, "key is not a reified type");

		transaction(tr).removeFact(typeReifier.valueToType((IConstructor) key), name);
	}

	public void setFact(IValue tr, IValue key, IValue name, IValue value) {
		if(!(key instanceof IConstructor))
			throw RuntimeExceptionFactory.illegalArgument(key, "key is not a reified type");

		Type keyType = typeReifier.valueToType((IConstructor) key);
		if(!value.getType().isSubtypeOf(keyType))
		    throw RuntimeExceptionFactory.illegalArgument(value, "value is not a subtype of " + keyType);
		else
			transaction(tr).setFact(keyType, name, value);
	}
	
	public void registerProducer(IFunction producer, ISet keys) {
		ProducerWrapper wrapper = new ProducerWrapper(producer, keys);
		producers.put(new IValueWrapper(producer), wrapper);
		registry.registerProducer(wrapper);
	}
	

	public void unregisterProducer(IValue producer) {
		IValueWrapper prod = new IValueWrapper(producer);
		if(producers.containsKey(prod)) {
			ProducerWrapper wrapper = producers.get(prod);
			registry.unregisterProducer(wrapper);
			producers.remove(prod);
		}
	}
	
	public ITuple getDependencyGraph(IValue tr) {
		return transaction(tr).getGraph();
	}

	protected IConstructor reify(Type type) {
		return typeReifier.typeToValue(type, new TypeStore(), vf.mapWriter().done());
	}
	/**
	 *  Cast an IValue to ITransaction 
	 */
	protected static Transaction transaction(IValue tr) {
		if(tr instanceof Transaction)
			return (Transaction)tr;
		else
			throw new ImplementationError("Not a transaction: " + tr);
	}
	
	class ProducerWrapper implements IIValueTask {
		
		private IFunction fun;
		private Collection<Type> keys = new ArrayList<Type>();
		
		ProducerWrapper(IFunction fun, ISet keys) {
			this.fun = fun;
			for(IValue v : keys) {
				if(v instanceof ITuple) {
					IValue keyType = ((ITuple)v).get(0);
					IValue nameType = ((ITuple)v).get(1);
					this.keys.add(TypeFactory.getInstance().tupleType(
							typeReifier.valueToType((IConstructor)keyType),
							typeReifier.valueToType((IConstructor)nameType)));
				}
				else {
					this.keys.add(TypeFactory.getInstance().tupleType(
							typeReifier.valueToType((IConstructor)v),
							TypeFactory.getInstance().valueType()));
				}
			}
		}
		
		public boolean produce(IRascalMonitor monitor, ITransaction<Type, IValue, IValue> tr,
				Type key, IValue name) {
			Transaction t = (Transaction)tr;
			IValue reifiedKey = reify(key);
			IValue result = fun.call(t, reifiedKey, name);
			if(result instanceof IBool) {
				return ((IBool)result).getValue();
			}
			else {
			    throw RuntimeExceptionFactory.illegalArgument(result, "result is not a bool");
			}
				
		}
		
		public Collection<Type> getKeys() {
			return keys;
		}
		
	}


}
class IValueWrapper {
	private final IValue value;
	
	IValueWrapper(IValue value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IValueWrapper other = (IValueWrapper) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
