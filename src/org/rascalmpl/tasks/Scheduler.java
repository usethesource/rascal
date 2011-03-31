package org.rascalmpl.tasks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.tasks.DepFactPolicy;
import org.rascalmpl.tasks.ITask;
import org.rascalmpl.tasks.ITransaction;
import org.rascalmpl.tasks.RefFactPolicy;
import org.rascalmpl.tasks.Scheduler;

public class Scheduler<K,N,V> implements ITask<K,N,V> {
	private static final Map<ClassTriple,Scheduler<?,?,?>> schedulers =
		new HashMap<ClassTriple,Scheduler<?,?,?>>();
	private Map<K,ITask<K,N,V>> producers = new HashMap<K,ITask<K,N,V>>();
	private Map<K, DepFactPolicy> depPolicies = new HashMap<K, DepFactPolicy>();
	private Map<K, RefFactPolicy> refPolicies = new HashMap<K, RefFactPolicy>();
	
	@SuppressWarnings("unchecked")
	public static <K,N,V> Scheduler<K,N,V> getScheduler(Class<K> keyType, Class<N> name, Class<V> value) {
		ClassTriple key = new ClassTriple(keyType, name, value);
		Scheduler<?, ?, ?> scheduler = schedulers.get(key);
		if(scheduler == null) {
			scheduler = new Scheduler<K,N,V>();
			schedulers.put(key, scheduler);
		}
		return (Scheduler<K, N, V>) scheduler;
	}

	public ITask<K,N,V> getProducer(K key, N name) {
		ITask<K, N, V> producer = producers.get(key);
		return producer;
	}
	
	@Override
	public void produce(IRascalMonitor monitor, ITransaction<K, N, V> tr, K key, N name) {
		ITask<K, N, V> producer = getProducer(key, name);
		if(producer == null)
			throw new ImplementationError("No registered fact producer for " + key.toString());
		producer.produce(monitor, tr, key, name);
	}
	
	public void registerProducer(ITask<K,N,V> producer) {
		for(K key : producer.getKeys())
			producers.put(key, producer);
	}

	public void unregisterProducer(ITask<K,N,V> producer) {
		for(K key : producer.getKeys()) {
			if(producers.get(key) == producer)
				producers.remove(key);
		}
	}
	
	public void setRefPolicy(K key, RefFactPolicy policy) {
		refPolicies.put(key, policy);
	}

	public void setDepPolicy(K key, DepFactPolicy policy) {
		depPolicies.put(key, policy);
	}
	
	public DepFactPolicy getDepPolicy(K key) {
		DepFactPolicy policy = depPolicies.get(key);
		if(policy == null)
			policy = DepFactPolicy.DEFAULT;
		return policy;
	}

	public RefFactPolicy getRefPolicy(K key) {
		RefFactPolicy policy = refPolicies.get(key);
		if(policy == null)
			policy = RefFactPolicy.DEFAULT;
		return policy;
	}
	
	@Override
	public Collection<K> getKeys() {
		return producers.keySet();
	}
	
	public void clear() {
		producers.clear();
		schedulers.clear();
		depPolicies.clear();
		refPolicies.clear();
	}
}


class ClassTriple {


	final private Class<?> first;
	final private Class<?> second;
	final private Class<?> third;
	public ClassTriple(Class<?> first, Class<?> second, Class<?> third) {
		super();
		this.first = first;
		this.second = second;
		this.third = third;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result + ((third == null) ? 0 : third.hashCode());
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
		ClassTriple other = (ClassTriple) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		if (third == null) {
			if (other.third != null)
				return false;
		} else if (!third.equals(other.third))
			return false;
		return true;
	}
	
	

}
