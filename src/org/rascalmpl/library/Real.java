/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Davy Landman - Davy.Landman@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library;

import java.util.Random;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.result.RealResult;

public class Real {
	private final IValueFactory values;
	private final Random random;
	
	public Real(IValueFactory values){
		super();
		
		this.values = values;
		random = new Random();
	}

	public IValue arbReal()
	//@doc{arbReal -- returns an arbitrary real value in the interval [0.0,1.0).}
	{
	  return values.real(random.nextDouble());
	}

	public IValue toInt(IReal d)
	//@doc{toInteger -- convert a real to integer.}
	{
	  return d.toInteger();
	}

	public IValue toString(IReal d)
	//@doc{toString -- convert a real to a string.}
	{
	  return values.string(d.toString());
	}
	
	public IValue round(IReal d) {
		return d.round();
	}
	
	public IValue PI()
	//@doc{pi -- returns the constant PI}
	{
		return values.pi(RealResult.PRECISION);
	}
	
	public IValue E()
	//@doc{e -- returns the constant E}
	{
		return values.e(RealResult.PRECISION);
	}
	
	public IValue pow(IReal x, IInteger y){
		return x.pow(y);
	}
	
	public IValue exp(IReal x){
		return x.exp(RealResult.PRECISION);
	}
	
	public IValue sin(IReal x){
		return x.sin(RealResult.PRECISION);
	}
	
	public IValue cos(IReal x){
		return x.cos(RealResult.PRECISION);
	}
	
	public IValue tan(IReal x){
		return x.tan(RealResult.PRECISION);
	}
	
	public IValue sqrt(IReal x){
		return x.sqrt(RealResult.PRECISION);
	}

	public IValue nroot(IReal x, IInteger y){
		return x.nroot(y, RealResult.PRECISION);
	}

	public IValue ln(IReal x) {
		return x.ln(RealResult.PRECISION);
	}

	public IValue log(IReal x, IReal base) {
		return x.log(base, RealResult.PRECISION);
	}
}
