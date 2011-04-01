package org.rascalmpl.tasks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public class PDBValueTaskRegistry implements ITaskRegistry<Type, IValue, IValue> {
	private static  PDBValueTaskRegistry instance = null;
	private Map<Type,Map<Type,ITask<Type,IValue,IValue>>> producers = new HashMap<Type,Map<Type,ITask<Type,IValue,IValue>>>();
	private Map<Type, DepFactPolicy> depPolicies = new HashMap<Type, DepFactPolicy>();
	private Map<Type, RefFactPolicy> refPolicies = new HashMap<Type, RefFactPolicy>();
	
	
	public static PDBValueTaskRegistry getRegistry() {
		if(instance == null)
			instance = new PDBValueTaskRegistry();
		return instance;
	}
	
	private PDBValueTaskRegistry() {
	}
	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#getProducer(Type, IValue)
	 */
	@Override
	public ITask<Type,IValue,IValue> getProducer(Type key, IValue name) {
		Map<Type, ITask<Type, IValue, IValue>> producerMap = producers.get(key);
		if(producerMap != null) {
			Type nameType = name.getType();
			if(producerMap.containsKey(nameType))
				return producerMap.get(nameType);
			for(Type t : producerMap.keySet()) {
				if(nameType.isSubtypeOf(t))
					return producerMap.get(t);
			}
		}
		throw new ImplementationError("No suitable producer found for " + key + "(" + name + ")");
	}
	
	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#produce(org.rascalmpl.interpreter.IRascalMonitor, org.rascalmpl.tasks.ITransaction, Type, IValue)
	 */
	@Override
	public void produce(IRascalMonitor monitor, ITransaction<Type, IValue, IValue> tr, Type key, IValue name) {
		ITask<Type, IValue, IValue> producer = getProducer(key, name);
		if(producer == null)
			throw new ImplementationError("No registered fact producer for " + key.toString());
		producer.produce(monitor, tr, key, name);
	}
	
	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#registerProducer(org.rascalmpl.tasks.ITask)
	 */
	@Override
	public void registerProducer(ITask<Type,IValue,IValue> producer) {
		for(Type key : producer.getKeys()) {
			if(key.isTupleType()) {
				Type key1 = key.getFieldType(0);
				Type key2 = key.getFieldType(1);
				Map<Type, ITask<Type, IValue, IValue>> map = producers.get(key1);
				if(map == null)
					map = new HashMap<Type, ITask<Type, IValue, IValue>>();
				map.put(key2, producer);
				producers.put(key1, map);
			}
			else
				throw new ImplementationError("Producer key should be a <keyType,argtype> tuple");
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#unregisterProducer(org.rascalmpl.tasks.ITask)
	 */
	@Override
	public void unregisterProducer(ITask<Type,IValue,IValue> producer) {
		for(Type key : producer.getKeys()) {
			if(key.isTupleType()) {
				Type key1 = key.getFieldType(0);
				Map<Type, ITask<Type, IValue, IValue>> map = producers.get(key1);
				if(map != null) {
					for(Type k : map.keySet()) {
						if(map.get(k).equals(producer))
							map.remove(k);
					}
					if(map.isEmpty())
						producers.remove(key1);
					else
						producers.put(key1, map);
				}
			}
			else
				throw new ImplementationError("Producer key should be a <keyType,argtype> tuple");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#setRefPolicy(Type, org.rascalmpl.tasks.RefFactPolicy)
	 */
	@Override
	public void setRefPolicy(Type key, RefFactPolicy policy) {
		refPolicies.put(key, policy);
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#setDepPolicy(Type, org.rascalmpl.tasks.DepFactPolicy)
	 */
	@Override
	public void setDepPolicy(Type key, DepFactPolicy policy) {
		depPolicies.put(key, policy);
	}
	
	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#getDepPolicy(Type)
	 */
	@Override
	public DepFactPolicy getDepPolicy(Type key) {
		DepFactPolicy policy = depPolicies.get(key);
		if(policy == null)
			policy = DepFactPolicy.DEFAULT;
		return policy;
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#getRefPolicy(Type)
	 */
	@Override
	public RefFactPolicy getRefPolicy(Type key) {
		RefFactPolicy policy = refPolicies.get(key);
		if(policy == null)
			policy = RefFactPolicy.DEFAULT;
		return policy;
	}
	
	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#getKeys()
	 */
	@Override
	public Collection<Type> getKeys() {
		return producers.keySet();
	}
	
	/* (non-Javadoc)
	 * @see org.rascalmpl.tasks.ITaskRegistry#clear()
	 */
	@Override
	public void clear() {
		producers.clear();
		depPolicies.clear();
		refPolicies.clear();
	}
}

