/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure.combine;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class FigureWithNonLocalFigure extends LayoutProxy {
	
	// Figure with associated non local figure which is drawed from elsewhere (also overlap ordering should be specified elsewhere)
	
	public Figure nonLocalFigure;
	
	public FigureWithNonLocalFigure(Figure inner, Figure nonLocalFigure, PropertyManager properties) {
		super(inner, properties);
		this.nonLocalFigure = nonLocalFigure;
	}
	
	
	
}
