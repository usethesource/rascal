/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure.combine.containers;

import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.FigureColorUtils;

public class WhiteSpace extends Container{

	public WhiteSpace(Figure inner, PropertyManager properties) {
		super(inner, properties);
	}

	@Override
	String containerName() {
		return "Whitespace";
	}

	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements) {
		gc.fill(FigureColorUtils.WHITE);
		gc.stroke(FigureColorUtils.WHITE);
		gc.rect(localLocation.getX(), localLocation.getY() , size.getX(), size.getY());
	}

}
