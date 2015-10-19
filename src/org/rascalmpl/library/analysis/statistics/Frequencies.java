/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.analysis.statistics;

import org.apache.commons.math.stat.Frequency;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.INumber;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class Frequencies {
	private final IValueFactory values;
	
	public Frequencies(IValueFactory values){
		super();
		this.values = values;
	}
	
	Frequency make(IList dataValues){
		Frequency freq = new Frequency();
		for(IValue v : dataValues){
			if(v instanceof INumber)
				freq.addValue(new ComparableValue((INumber)v));
			else if(v instanceof IString)
				freq.addValue(new ComparableValue((IString)v));
			else
				throw RuntimeExceptionFactory.illegalArgument(v,null, null);
		}
		return freq;
	}
	
	public IValue cumFreq(IList dataValues, INumber n){
		return values.integer(make(dataValues).getCumFreq(new ComparableValue(n)));
	}
	
	public IValue cumFreq(IList dataValues, IString s){
		return values.integer(make(dataValues).getCumFreq(new ComparableValue(s)));
	}
	
	public IValue cumPct(IList dataValues, INumber n){
		return values.real(make(dataValues).getCumPct(new ComparableValue(n)));
	}
	
	public IValue cumPct(IList dataValues, IString s){
		return values.real(make(dataValues).getCumPct(new ComparableValue(s)));
	}
	
	public IValue pct(IList dataValues, INumber n){
		return values.real(make(dataValues).getPct(new ComparableValue(n)));
	}
	
	public IValue pct(IList dataValues, IString s){
		return values.real(make(dataValues).getPct(new ComparableValue(s)));
	}
}

class ComparableValue implements Comparable<ComparableValue> {
    private final IValue v;
    
	ComparableValue(INumber n){
		this.v = n;
	}
	
	ComparableValue(IString s){
		this.v = s;
	}
	
	boolean isNumber() { return v instanceof INumber; }
	
	boolean isString() { return v instanceof IString; }
	
	@Override
	public int compareTo(ComparableValue other) {
		if(isNumber()){
		   if(other.isNumber())	
			   return ((INumber)v).compare((INumber)other.v);
		   else
			   return -1;
		} else if (other.isString()){
			return ((IString)v).compare((IString)other.v);
		} else
			return 1;
	}
}