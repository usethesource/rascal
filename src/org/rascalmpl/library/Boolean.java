/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library;

import java.util.Random;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class Boolean {
	private final IValueFactory values;
	private final Random random;
	
	public Boolean(IValueFactory values){
		super();
		
		this.values = values;
		random = new Random();
	}
	
	public IValue arbBool()
	//@doc{arbBool -- get an arbitrary boolean value.}
	{
	  return values.bool(random.nextInt(2) == 1);
	}
}
