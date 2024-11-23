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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.tasks.IDependencyListener.Change;
import org.rascalmpl.tasks.facts.AbstractFact;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.ExternalType;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

public class Transaction  implements ITransaction<Type,IValue,IValue>, IExternalValue,
IExpirationListener<IValue> {
	public static final Type TransactionType = new ExternalType() {

	    protected boolean intersectsWithExternal(Type type) {
	        return false;
	    };
	    
		@Override
		protected Type lubWithExternal(Type type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected boolean isSubtypeOfExternal(Type type) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected Type glbWithExternal(Type type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IConstructor asSymbol(IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done) {
		  // TODO Auto-generated method stub
		  return null;
		}

		@Override
		public Type asAbstractDataType() {
		  // TODO Auto-generated method stub
		  return null;
		}
		
		public IValue randomValue(java.util.Random random, IValueFactory vf, TypeStore store, java.util.Map<Type,Type> typeParameters, int maxDepth, int maxBreadth) {
		    // TODO Auto-generated method stub
	          return null;
		};
		};
		
	private final Transaction parent;
	private final boolean commitEnabled;
	private final Map<Key, IFact<IValue>> map = new HashMap<Key, IFact<IValue>>();
	private final ITaskRegistry<Type,IValue,IValue> registry;
	private final Set<IFact<IValue>> deps = new HashSet<IFact<IValue>>();
	private final Set<Key> removed = new HashSet<Key>();
	private INameFormatter format;
	private final PrintWriter stderr;
	private Map<Type, Collection<IDependencyListener>> listeners = new HashMap<Type, Collection<IDependencyListener>>();
	public Transaction(PrintWriter stderr) {
		this(null, null, stderr, true);
	}

	public Transaction(INameFormatter format, PrintWriter stderr) {
		this(null, format, stderr, true);
	}

	public Transaction(Transaction parent, PrintWriter stderr) {
		this(parent, null, stderr, true);
	}

	public Transaction(Transaction parent, PrintWriter stderr, boolean commitEnabled) {
		this(parent, null, stderr, commitEnabled);
	}

	public Transaction(Transaction parent, INameFormatter format, PrintWriter stderr, boolean commitEnabled) {
		this.parent = parent;
		this.commitEnabled = commitEnabled;
		if(parent != null)
			this.registry = parent.registry;
		else
			this.registry = PDBValueTaskRegistry.getRegistry();
		if(format == null && parent != null)
			this.format = parent.format;
		else
			this.format = format;
		if(stderr == null)
			this.stderr = new PrintWriter(System.err);
		else
			this.stderr = stderr;
	}

	@Override
	public Type getType() {
		return TransactionType;
	}

	@Override
	public boolean match(IValue other) {
        return false;
    }

	@Override
	public IValue getFact(IRascalMonitor monitor, Type key, IValue name) {
		IFact<IValue> fact = null;
		try {
			// System.err.println("GetFact: " + formatKey(key, name));
			boolean status = false;
			synchronized (this) {
				Key k = new Key(key, name);
				fact = query(k);

				if (fact != null) {
					IValue value = fact.getValue();
					if (value != null) {
						deps.add(fact);
						return value;
					}
				}
				String JOB = "Producing fact " + formatKey(key, name);
				monitor.jobStart(JOB);
				Transaction tr = new Transaction(this, stderr, true);
				status = registry.produce(monitor, tr, key, name);
				monitor.jobEnd(JOB, true);
				fact = tr.map.get(k);
				if (fact != null) {
					tr.commit();
					deps.add(fact);
				}
				//			else
				//			tr.abandon();
			}
			if (fact != null)
				return fact.getValue();
			else {
				System.err.println("ERROR: failed to produce fact: " + formatKey(key, name)
						+ "." + (status ? "" : " (producer was not authorative)"));

			}
			return null;
		}
		catch(RuntimeException t) {
			t.printStackTrace();
			throw t;
		}
	}

	@Override
	public IValue queryFact(Type key, IValue name) {
		Key k = new Key(key, name);
		IFact<IValue> fact = query(k);

		if(fact != null)
			return fact.getValue();
		else
			return null;
	}

	protected IFact<IValue> query(Key k) {
		IFact<IValue> fact = map.get(k);
		if(fact == null && parent != null)
			return parent.query(k);
		else
			return fact;

	}

	@Override
	public synchronized void removeFact(Type key, IValue name) {
		Key k = new Key(key, name);
		IFact<IValue> fact = map.get(k);
		map.remove(k);
		if(fact != null) {
			notifyListeners(key, fact, Change.REMOVED);
			fact.remove();
			deps.remove(fact);
			removed.add(k);
		}
	}

	public synchronized void removeFact(IFact<IValue> fact) {
		removeFact(((Key)fact.getKey()).type, ((Key)fact.getKey()).name);
	}

	@Override
	public synchronized IFact<IValue> setFact(Type key, IValue name, IValue value) {
		return setFact(key, name, value, null, FactFactory.getInstance());
	}

	@Override
	public synchronized IFact<IValue> setFact(Type key, IValue name, IValue value,
			Collection<IFact<IValue>> dependencies) {
		return setFact(key, name, value, dependencies, FactFactory.getInstance());
	}

	@Override
	public synchronized IFact<IValue> setFact(Type key, IValue name, IFact<IValue> fact) {
		Key k = new Key(key, name);
		IFact<IValue> oldFact = map.get(k);

		if(oldFact == null) {
			map.put(k, fact);
			removed.remove(k);
		}
		else if(oldFact != fact) {
			oldFact.remove();
			map.put(k, fact);
			removed.remove(k);
		}

		notifyListeners(key, fact, Change.CHANGED);

		return fact;
	}

	@Override
	public synchronized IFact<IValue> setFact(Type key, IValue name, IValue value,
			Collection<IFact<IValue>> dependencies, IFactFactory factory) {
		Key k = new Key(key, name);
		IFact<IValue> fact = map.get(k);
		if(fact == null) {
			fact = factory.fact(IValue.class, k, formatKey(k), this, registry.getDepPolicy(key), registry.getRefPolicy(key));
		}
		boolean change = fact.setValue(value);
		if(dependencies != null)
			fact.setDepends(Collections.unmodifiableCollection(dependencies));
		else
			fact.setDepends(Collections.unmodifiableCollection(deps));
		map.put(k, fact);
		removed.remove(k);

		if(change)
			notifyListeners(key, fact, Change.CHANGED);
		/*AVOID CONSOLE PRINTING WITHIN SYNCHRONIZED?
		 * stderr.printf("Set fact %s = %s\n    <- ", formatKey(key, name), abbrev(value.toString(), 40));
		for(IFact<?> d : fact.getDepends())
			stderr.print(formatKey(d.getKey()) + " ");
		stderr.println();
		stderr.flush();
		 */

		return fact;
	}

	@Override
	public void abandon() {
		for(IFact<IValue> fact : map.values()) {
			notifyListeners(((Key)fact.getKey()).type, fact, Change.REMOVED);
			fact.remove(); // TODO: dispose?
		}
		map.clear();
		removed.clear();
		deps.clear();
	}

	@Override
	public void commit() {
		if(parent != null && commitEnabled) {
			if(parent.parent == null) {
				AbstractFact.pruneExpired();
			}
			for(Key k : removed) {
				parent.removeFact(k.type, k.name);
			}
			Set<IDependencyListener> toNotify = new HashSet<IDependencyListener>();
			Set<IDependencyListener> trSet= new HashSet<IDependencyListener>();

			for(Key k : map.keySet()) {
				IFact<IValue> fact = parent.map.get(k);
				IFact<IValue> trFact = map.get(k);
				trSet.add(trFact);
				if(fact != null) {
					Collection<IDependencyListener> ls = fact.getListeners();
					if(fact.updateFrom(trFact)) {
						parent.notifyListeners(k.type, fact, Change.CHANGED);
						toNotify.addAll(ls);
					}
					trSet.add(fact);
				}
				else {
					parent.map.put(k, map.get(k));
					parent.notifyListeners(k.type, trFact, Change.CHANGED);
				}
			}
			// TODO: update fact references
		}
	}

	@Override
	public void commit(Collection<IFact<IValue>> deps) {
		if(parent != null && commitEnabled) {
			for(Key k : removed) {
				parent.removeFact(k.type, k.name);
			}
			for(Key k : map.keySet()) {
				IFact<IValue> fact = parent.query(k);
				map.get(k).setDepends(deps);
				if(fact != null) {
					if(fact.updateFrom(map.get(k)))
						parent.notifyListeners(k.type, fact, Change.CHANGED);
				}
				else {
					parent.map.put(k, map.get(k));
					parent.notifyListeners(k.type, map.get(k), Change.CHANGED);
				}
			}
		}
	}

	private void notifyListeners(Type k, IFact<IValue> fact, Change change) {
		Collection<IDependencyListener> ls = listeners.get(k);
		if(ls != null)
			for(IDependencyListener l : ls) {
				l.changed(fact, change, null);
			}
	}

	@Override
	public synchronized void registerListener(IDependencyListener listener, Type key) {
		Collection<IDependencyListener> ls = listeners.get(key);
		if(ls == null)
			ls = new ArrayList<IDependencyListener>();
		if(!ls.contains(listener))
			ls.add(listener);
		listeners.put(key, ls);
	}

	@Override
	public synchronized void unregisterListener(IDependencyListener listener, Type key) {
		Collection<IDependencyListener> ls = listeners.get(key);
		if(ls != null) {
			ls.remove(listener);
			listeners.put(key, ls);
		}
	}

	private String formatKey(Object key) {
		if(key instanceof Key) {
			Key k = (Key)key;
			return formatKey(k.type, k.name);
		}
		else
			return key.toString();
	}

	private String formatKey(Type key, IValue name) {
		String n;
		if(format == null && parent != null)
			return parent.formatKey(key, name);
		else if(format != null)
			n = format.format(name);
		else
			n = name.toString();

		return key.getName() + "(" + n + ")";
	}

	@Override
	public IFact<IValue> findFact(Type key, IValue name) {
		return query(new Key(key, name));
	}

	public static Key makeKey(Type key, IValue name) {
		return new Key(key, name);
	}

	public ITuple getGraph() {
		GraphBuilder g = new GraphBuilder(); 
		for(Key k : map.keySet()) {
			g.addFact(map.get(k), k.type.getName(), map.get(k).getStatus());			
		}
		for(IFact<?> fact : map.values()) {
			for(IFact<?> d : fact.getDepends()) {
				if(d.getListeners().contains(fact))
					g.arrow(fact, d, "<->");
				else {
					g.arrow(fact, d, "->");
					System.err.printf("Warning: fact %s depends on %s but does not listen on it.", fact, d);
				}
			}
		}
		return g.getGraph();
	}

	public synchronized void expire(Object key) {
		Key k = (Key) key;
		map.remove(k);
		removed.add(k);
	}

	@Override
	public int getMatchFingerprint() {
		return hashCode();
	}
}

class Key {
	public final Type type;
	public final IValue name;

	Key(Type type, IValue name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Key other = (Key) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}

