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
package org.rascalmpl.tasks;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public class PDBValueTaskRegistry extends TaskRegistry<Type, IValue, IValue> implements ITaskRegistry<Type, IValue, IValue> {
	private static volatile PDBValueTaskRegistry instance = null;
	private Map<Type,Map<Type,ITask<Type,IValue,IValue>>> keyedProducers = new HashMap<Type,Map<Type,ITask<Type,IValue,IValue>>>();

	public static PDBValueTaskRegistry getRegistry() {
		if(instance == null) {
			synchronized(PDBValueTaskRegistry.class) {
				if(instance == null)
					instance = new PDBValueTaskRegistry();
			}
		}
		return instance;
	}

	private PDBValueTaskRegistry() {
		super();
	}
	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#getProducer(Type, IValue)
	 */
	@Override
	public ITask<Type,IValue,IValue> getProducer(Type key, IValue name) {
		lock.lock();
		try {
			Map<Type, ITask<Type, IValue, IValue>> producerMap = keyedProducers.get(key);
			if(producerMap != null) {
				Type nameType = name.getType();
				if(producerMap.containsKey(nameType))
					return producerMap.get(nameType);
				for(Map.Entry<Type, ITask<Type, IValue, IValue>> t : producerMap.entrySet()) {
					if(nameType.isSubtypeOf(t.getKey()))
						return t.getValue();
				}
			}
			throw new ImplementationError("No suitable producer found for " + key + "(" + name + ")");
		}
		finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#produce(org.rascalmpl.interpreter.IRascalMonitor, org.rascalmpl.tasks.ITransaction, Type, IValue)
	 */
	@Override
	public boolean produce(IRascalMonitor monitor, ITransaction<Type, IValue, IValue> tr, Type key, IValue name) {
		ITask<Type, IValue, IValue> producer = null;
		lock.lock();
		try {
			producer = getProducer(key, name);
		}
		finally {
			lock.unlock();
		}
		if(producer == null)
			throw new ImplementationError("No registered fact producer for " + key.toString());
		return producer.produce(monitor, tr, key, name);
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#registerProducer(org.rascalmpl.tasks.ITask)
	 */
	@Override
	public void registerProducer(ITask<Type,IValue,IValue> producer) {
		lock.lock();
		try {
			for(Type key : producer.getKeys()) {
				if(key.isTuple()) {
					Type key1 = key.getFieldType(0);
					Type key2 = key.getFieldType(1);
					Map<Type, ITask<Type, IValue, IValue>> map = keyedProducers.get(key1);
					if(map == null)
						map = new HashMap<Type, ITask<Type, IValue, IValue>>();
					map.put(key2, producer);
					keyedProducers.put(key1, map);
				}
				else {
					producers.put(key, producer);
				}
			}
		}
		finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#unregisterProducer(org.rascalmpl.tasks.ITask)
	 */
	@Override
	public void unregisterProducer(ITask<Type,IValue,IValue> producer) {
		lock.lock();
		try {
			for(Type key : producer.getKeys()) {
				if(key.isTuple()) {
					Type key1 = key.getFieldType(0);
					Map<Type, ITask<Type, IValue, IValue>> map = keyedProducers.get(key1);
					if(map != null) {
						for(Map.Entry<Type, ITask<Type,IValue,IValue>> entry : map.entrySet()) {
							if(entry.getValue().equals(producer))
								map.remove(entry.getKey());
						}
						if(map.isEmpty())
							keyedProducers.remove(key1);
						else
							keyedProducers.put(key1, map);
					}
				}
				else {
					if(producers.get(key) == producer)
						producers.remove(key);
				}
			}
		}
		finally {
			lock.unlock();
		}
	}


	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#getKeys()
	 */
	@Override
	public Collection<Type> getKeys() {
		lock.lock();
		try {
			Set<Type> set = new HashSet<Type>();
			set.addAll(keyedProducers.keySet());
			set.addAll(super.getKeys());
			return set;
		}
		finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#clear()
	 */
	@Override
	public void clear() {
		lock.lock();
		try {
			keyedProducers.clear();
			super.clear();
		}
		finally {
			lock.unlock();
		}
	}

}

