/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI  
 *******************************************************************************/
package org.rascalmpl.value.util;

import java.util.Comparator;
import java.util.Objects;

import org.rascalmpl.value.IValue;

public class EqualityUtils {

	/**
	 * Temporary function in order to support different equality checks.
	 */
	@SuppressWarnings("rawtypes")
	public static Comparator getDefaultEqualityComparator() {
		return new Comparator() {
			@Override
			public int compare(Object a, Object b) {
				return Objects.equals(a, b) ? 0 : -1;
			}
		};
	}
	
	/**
	 * Temporary function in order to support equivalence. Note, this
	 * implementation is only works for {@link IValue} arguments. If arguments
	 * are of a different type, an unchecked exception will be thrown.
	 */
	@SuppressWarnings("rawtypes")
	public static Comparator getEquivalenceComparator() {
		return new Comparator() {
			@Override
			public int compare(Object a, Object b) {
				IValue v = (IValue) a;
				IValue w = (IValue) b;

				if ((v == w) || (v != null && v.isEqual(w)))
					return 0;
				else
					return -1;
			}
		};
	}

}
