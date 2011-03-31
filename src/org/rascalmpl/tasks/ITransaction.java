package org.rascalmpl.tasks;

import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.tasks.IFact;


public interface ITransaction<K,N,V> {

	public abstract IFact<V> setFact(K key, N name, V value);

	//public abstract V getFact(K key, N name);

	public abstract V getFact(IRascalMonitor monitor, K key, N name);

	public abstract V queryFact(K key, N name);
	
	public abstract IFact<V> findFact(K key, N name);

	public abstract void removeFact(K key, N name);

	public abstract void abandon();
	
	public abstract void commit();
}
