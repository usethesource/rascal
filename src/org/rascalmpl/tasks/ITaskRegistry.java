package org.rascalmpl.tasks;

import java.util.Collection;

import org.rascalmpl.interpreter.IRascalMonitor;

public interface ITaskRegistry<K, N, V> extends ITask<K, N, V> {

	public abstract ITask<K, N, V> getProducer(K key, N name);

	public abstract boolean produce(IRascalMonitor monitor,
			ITransaction<K, N, V> tr, K key, N name);

	public abstract void registerProducer(ITask<K, N, V> producer);

	public abstract void unregisterProducer(ITask<K, N, V> producer);

	public abstract void setRefPolicy(K key, RefFactPolicy policy);

	public abstract void setDepPolicy(K key, DepFactPolicy policy);

	public abstract DepFactPolicy getDepPolicy(K key);

	public abstract RefFactPolicy getRefPolicy(K key);

	public abstract Collection<K> getKeys();

	public abstract void clear();

}