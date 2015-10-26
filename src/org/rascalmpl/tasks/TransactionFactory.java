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

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

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
