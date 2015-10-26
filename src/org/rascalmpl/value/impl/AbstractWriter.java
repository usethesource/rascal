/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWriter;
import org.rascalmpl.value.exceptions.FactTypeUseException;

public abstract class AbstractWriter implements IWriter {
	public void insertAll(Iterable<? extends IValue> collection) throws FactTypeUseException {
		for (IValue v : collection) {
			insert(v);
		}
	}
}
