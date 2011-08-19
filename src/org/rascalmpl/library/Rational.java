/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class Rational {
	private final IValueFactory values;
	
	public Rational(IValueFactory values){
		super();
		
		this.values = values;
	}

	public IValue toReal(IRational n)
	//@doc{toReal -- convert a rational value to a real value.}
	{
	  return n.toReal();
	}

	public IValue toInteger(IRational n)
	//@doc{toReal -- convert a rational value to a integer value.}
	{
	  return n.toInteger();
	}

	public IValue numerator(IRational n)
	{
		return n.numerator();
	}

	public IValue denominator(IRational n)
	{
	  return n.denominator();
	}

	public IValue remainder(IRational n)
	{
	  return n.remainder();
	}

	public IValue toString(IInteger n)
	//@doc{toString -- convert an integer value to a string.}
	{
	  return values.string(n.toString());
	}
}
