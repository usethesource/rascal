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
package org.rascalmpl.library.vis.figure.combine.containers;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.WithInnerFig;
import org.rascalmpl.library.vis.properties.PropertyManager;


/**
 * A container represents a visual element that can contain another (nested) visual element called the "inside" element.
 * Typical examples are Boxes and Ellipses that may contain another element.
 * 
 * 
 * @author paulk
 * 
 */

public abstract class Container extends WithInnerFig {

	@SuppressWarnings("unused")
	final private static boolean debug = false;

	public Container(Figure inner, PropertyManager properties) {
		super(inner,properties);
	}
	

	

	/**
	 * @return the actual container name, e.g. box, ellipse, ...
	 */
	
	abstract String containerName();
	
	
	@Override
	public String  toString(){
		return String.format("Container %s %s", globalLocation,size);
	}
	

}
