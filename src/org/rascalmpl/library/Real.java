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

import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class Real {
	private final IValueFactory values;
	private final Random random;
	private final double pi, e;
	
	public Real(IValueFactory values){
		super();
		
		this.values = values;
		random = new Random();
		pi = Math.PI;
		e = Math.E;
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
	  return values.real(pi);
	}
	
	public IValue E()
	//@doc{e -- returns the constant E}
	{
	  return values.real(e);
	}
	
	public IValue pow(IReal x, IReal y){
	  return values.real(Math.pow(x.doubleValue(), y.doubleValue()));
	}
	
	public IValue exp(IReal x){
		  return values.real(Math.exp(x.doubleValue()));
		}
	
	public IValue sin(IReal x){
		  return values.real(Math.sin(x.doubleValue()));
		}
	
	public IValue cos(IReal x){
		  return values.real(Math.cos(x.doubleValue()));
		}
	
	public IValue tan(IReal x){
		  return values.real(Math.tan(x.doubleValue()));
		}
	
	public IValue sqrt(IReal x){
		  return values.real(Math.sqrt(x.doubleValue()));
		}
	
	public IValue log(IReal x) {
		return values.real(Math.log(x.doubleValue()));
	}
	public IValue log10(IReal x) {
		return values.real(Math.log10(x.doubleValue()));
	}
}
