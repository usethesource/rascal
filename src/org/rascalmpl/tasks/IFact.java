package org.rascalmpl.tasks;

import java.util.Collection;

import org.rascalmpl.tasks.IDependencyListener;
import org.rascalmpl.tasks.IFact;

public interface IFact<V> {
	

	/**
	 *  Returns true if a fact's valid is valid.
	 * 
	 *  Facts can become invalid if the invalidate() method is called, or if the the
	 *  value is weakly referenced and disappears from memory.
	 */
	public abstract boolean isValid();
	
	/**
	 *  Return the value of this fact.
	 *  
	 *  Returns null iff !isValid()
	 */
	public abstract V getValue();
	
	/**
	 *  Set the value to 'val'.
	 *  
	 *  If the fact supports dependency tracking, this will cause dependents to be notified
	 *  (if the value has changed).
	 *  
	 * @param val The new value
	 */
	public abstract void setValue(V val);

	/**
	 * Removing a fact means removing it from the dependency chain, and
	 * disposing of the data. This is done when the fact is no longer useful
	 * (for example, if the underlying entity has been removed).
	 */
	public abstract void remove();
	
	public abstract void registerListener(IDependencyListener listener);
	
	public abstract void unregisterListener(IDependencyListener listener);
	
	public abstract void setDepends(Collection<IFact<V>> deps);
	public abstract void updateFrom(IFact<V> fact);
	public abstract Object getKey();
}