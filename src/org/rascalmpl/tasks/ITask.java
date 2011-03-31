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
	 * @param tr
	 * @param key
	 * @param name
	 */
	public abstract void produce(IRascalMonitor monitor, ITransaction<KeyType,NameType,ValueType> tr, KeyType key, NameType name);

	/**
	 * The facts this producer should be considered a primary supplier of.
	 */
	public abstract Collection<KeyType> getKeys();
}
