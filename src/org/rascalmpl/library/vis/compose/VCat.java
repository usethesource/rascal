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
package org.rascalmpl.library.vis.compose;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

/**
 * 
 * Vertical composition of elements:
 * - when alignAnchors==true, using their horizontal anchor for alignment.
 * - otherwise using current alignment settings
 * 
 * VCat is a HCat but with the axises swapped
 * 
 * @author paulk
 *
 */
public class VCat extends HCat {

	public VCat(IFigureApplet fpa, Figure[] figures, PropertyManager properties) {
		super(fpa, figures, properties);
		flip = true;
	}
	
	protected boolean correctOrientation(boolean horizontal) {
		return !horizontal;
	}
}
