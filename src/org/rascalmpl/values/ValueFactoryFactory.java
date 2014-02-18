/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
*******************************************************************************/
package org.rascalmpl.values;

import org.eclipse.imp.pdb.facts.IValueFactory;
//import org.eclipse.imp.pdb.facts.impl.persistent.ValueFactory1;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;

public class ValueFactoryFactory{
	private final static IValueFactory valueFactory = ValueFactory.getInstance();
	
	public static IValueFactory getValueFactory(){
		return valueFactory;
	}
}
