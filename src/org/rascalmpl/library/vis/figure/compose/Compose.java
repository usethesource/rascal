/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure.compose;


import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Util;

/**
 * Abstract class for the composition of a list of visual elements.
 * 
 * @author paulk
 * 
 */
public abstract class Compose extends Figure {

	@SuppressWarnings("unused")
	final private static boolean debug = false;

	protected Compose(Figure[] figures,PropertyManager properties) {
		super(properties);
		children = figures;
	}
	
	
	public String toString(){
		String[] s = new String[children.length];
		for(int i = 0 ; i < s.length ; i++){
			s[i] = children[i].toString();
		}
		return "{" + Util.intercalate(",", s) + "}";
	}
}
