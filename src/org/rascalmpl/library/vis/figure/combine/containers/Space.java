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

import java.util.List;
import java.util.Vector;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Rectangle;

/**
 * Spacing that can act as container.
 * 
 * @author paulk
 *
 */
public class Space extends Container {

	public static Space empty = new Space(null, new PropertyManager());
	
	public Space(Figure inside, PropertyManager properties) {
		super(inside, properties);
	}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		System.out.printf("Drawing %s\n", this);
	}

	
	@Override
	String containerName(){
		return "space";
	}

	
}
