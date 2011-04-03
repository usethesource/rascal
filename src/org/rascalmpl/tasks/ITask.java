package org.rascalmpl.tasks;

import java.util.Collection;

import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.tasks.ITransaction;

public interface ITask<KeyType,NameType,ValueType> {
	/**
	 * Produce the fact requested by the <key,name> combination, and define it in tr.
	 * 
	 * The allowed key arguments are given by getKeys() below.
	 * 
	 * May also produce additional facts.
	 * 
	 * @return true if the task was authorative for this key/name combination, false if
	 *   it failed to produce the fact and was not authorative (i.e., we should try again with
	 *   a different task).
	 */
	public abstract boolean produce(IRascalMonitor monitor, ITransaction<KeyType,NameType,ValueType> tr, KeyType key, NameType name);

	/**
	 * The facts this producer should be considered a primary supplier of.
	 */
	public abstract Collection<KeyType> getKeys();
}
