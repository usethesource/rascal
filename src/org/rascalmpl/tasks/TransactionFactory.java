package org.rascalmpl.tasks;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.tasks.ITransaction;
import org.rascalmpl.tasks.Transaction;

public class TransactionFactory {
	public static ITransaction<Type,IValue,IValue> transaction() {
		return new Transaction(null);
	}
/*
	public static ITransaction<Type,IValue,IValue> transaction(ITransaction<Type,IValue,IValue> tr) {
		return new Transaction(tr);
	}
*/
}
