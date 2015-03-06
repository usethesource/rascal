/*******************************************************************************
 * Copyright (c) 2009-2014 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.values;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.values.uptr.Factory;

public class ValueFactoryFactory{
	//private final static IValueFactory valueFactory =org.eclipse.imp.pdb.facts.impl.fast.ValueFactory.getInstance();
	private final static IValueFactory valueFactory = new Factory();
	
	public static IValueFactory getValueFactory(){
		return valueFactory;
	}
}
