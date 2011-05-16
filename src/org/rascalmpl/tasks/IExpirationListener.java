package org.rascalmpl.tasks;

public interface IExpirationListener<V> {
	void expire(Object key);
}
