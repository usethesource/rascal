/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure.combine.containers;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;

/**
 * Rectangular box that can act as container
 * 
 * @author paulk
 *
 */
public class Box extends Container {

	public Box(Figure inner, PropertyManager properties) {
		super(inner, properties);
	}

	@Override
	void drawContainer(GraphicsContext gc){
		gc.rect(getLeft(), getTop() , size.getWidth(), size.getHeight());
	}
	
	@Override
	String containerName(){
		return "box";
	}
}
