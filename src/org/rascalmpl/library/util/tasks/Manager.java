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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.tasks.IIValueTask;
import org.rascalmpl.tasks.ITaskRegistry;
import org.rascalmpl.tasks.ITransaction;
import org.rascalmpl.tasks.PDBValueTaskRegistry;
import org.rascalmpl.tasks.Transaction;
import org.rascalmpl.values.ValueFactoryFactory;

public class Manager {
	private Transaction base = null;
	private final ITaskRegistry<Type, IValue, IValue> registry;
	private final IValueFactory vf;
	private final TypeReifier typeReifier;
	private final Map<IValueWrapper,ProducerWrapper> producers = new HashMap<IValueWrapper,ProducerWrapper>();
	
	public Manager() {
		this(ValueFactoryFactory.getValueFactory());
	}
	
	public Manager(IValueFactory vf) {
		this.vf = vf;
		this.typeReifier = new TypeReifier(vf);
		this.registry = PDBValueTaskRegistry.getRegistry();
	}
	
	public void lockProducerRegistry() {
		registry.lock();
	}
	
	public void unlockProducerRegistry() {
		registry.unlock();
	}
	
	public IValue startTransaction(IEvaluatorContext ctx) {
		if(base == null)
			base = new Transaction(ctx.getStdErr());
		return new Transaction(base, ctx.getStdErr());
	}
	
	public IValue startTransaction(IValue tr, IEvaluatorContext ctx) {
		return new Transaction(transaction(tr), ctx.getStdErr());
	}
	
	public void endTransaction(IValue tr) {
		transaction(tr).commit();
	}

	public void abandonTransaction(IValue tr) {
		transaction(tr).abandon();
	}
	
	public IValue getFact(IValue tr, IValue key, IValue name, IEvaluatorContext ctx) {
		if(!(key instanceof IConstructor))
			throw RuntimeExceptionFactory.illegalArgument(key, ctx.getCurrentAST(), ctx.getStackTrace(), "key is not a reified type");

		IValue fact = transaction(tr).getFact(ctx, typeReifier.valueToType((IConstructor) key), name);
		if(fact == null)
			fact = null; // <- put breakpoint here!
		return check(fact, (IConstructor) key, name, ctx);
	}

	private IValue check(IValue fact, IValue key, IValue name, IEvaluatorContext ctx) {
		if(!(key instanceof IConstructor))
			throw RuntimeExceptionFactory.illegalArgument(key, ctx.getCurrentAST(), ctx.getStackTrace(), "key is not a reified type");

		if(fact == null)
			throw RuntimeExceptionFactory.noSuchKey(vf.string(typeReifier.valueToType((IConstructor) key).toString() + ":" + name.toString()), 
					ctx.getCurrentAST(), ctx.getStackTrace());
		else if(!fact.getType().isSubtypeOf(typeReifier.valueToType((IConstructor) key)))
			throw new UnexpectedType(typeReifier.valueToType((IConstructor) key), fact.getType(), ctx.getCurrentAST());
		else
			return fact;
	}

	public IValue queryFact(IValue tr, IValue key, IValue name, IEvaluatorContext ctx) {
		if(!(key instanceof IConstructor))
			throw RuntimeExceptionFactory.illegalArgument(key, ctx.getCurrentAST(), ctx.getStackTrace(), "key is not a reified type");

		return check(transaction(tr).queryFact(typeReifier.valueToType((IConstructor) key), name), key, name, ctx);
	}

	public void removeFact(IValue tr, IValue key, IValue name, IEvaluatorContext ctx) {
		if(!(key instanceof IConstructor))
			throw RuntimeExceptionFactory.illegalArgument(key, ctx.getCurrentAST(), ctx.getStackTrace(), "key is not a reified type");

		transaction(tr).removeFact(typeReifier.valueToType((IConstructor) key), name);
	}

	public void setFact(IValue tr, IValue key, IValue name, IValue value, IEvaluatorContext ctx) {
		if(!(key instanceof IConstructor))
			throw RuntimeExceptionFactory.illegalArgument(key, ctx.getCurrentAST(), ctx.getStackTrace(), "key is not a reified type");

		Type keyType = typeReifier.valueToType((IConstructor) key);
		if(!value.getType().isSubtypeOf(keyType))
			throw new UnexpectedType(keyType, value.getType(), ctx.getCurrentAST());
		else
			transaction(tr).setFact(keyType, name, value);
	}
	
	public void registerProducer(IValue producer, ISet keys, IEvaluatorContext ctx) {
		ProducerWrapper wrapper = new ProducerWrapper(ctx, (ICallableValue)producer, keys);
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

	protected IConstructor reify(IEvaluatorContext ctx, Type type) {
		return (IConstructor) typeReifier.typeToValue(type, ctx).getValue();
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
		
		private ICallableValue fun;
		private IEvaluatorContext ctx;
		private Collection<Type> keys = new ArrayList<Type>();
		
		ProducerWrapper(IEvaluatorContext ctx, ICallableValue fun, ISet keys) {
			this.ctx = ctx;
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
			IValue reifiedKey = reify(ctx, key);
			Result<IValue> result = fun.call(monitor, new Type[] {t.getType(), reifiedKey.getType(), name.getType()},
					new IValue[] {t, reifiedKey, name}, null);
			if(result.getValue() instanceof IBool)
				return ((IBool)result.getValue()).getValue();
			else
				throw new UnexpectedType(TypeFactory.getInstance().boolType(), result.getType(), ctx.getCurrentAST());
				
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
		} else if (!value.isEqual(other.value))
			return false;
		return true;
	}

}
