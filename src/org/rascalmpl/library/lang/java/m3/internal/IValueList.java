/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jouke Stoel - Jouke.Stoel@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.lang.java.m3.internal;

import java.util.ArrayList;
import java.util.List;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class IValueList {
	private final IValueFactory values;
	
	private List<IValue> valueList;
	
	public IValueList(final IValueFactory values) {
		this.values = values;
		
		valueList = new ArrayList<IValue>();
	}

	public void add(IValue value) {
		valueList.add(value);
	}
	
	private IValue[] toArray() {
		return valueList.toArray(new IValue[0]);
	}
	
	public IValue asList() {
		return values.list(toArray());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((valueList == null) ? 0 : valueList.hashCode());
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
		IValueList other = (IValueList) obj;
		if (valueList == null) {
			if (other.valueList != null)
				return false;
		} else if (!valueList.equals(other.valueList))
			return false;
		return true;
	}
}
