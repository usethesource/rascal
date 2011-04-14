/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class Scale extends Figure {
	float xscale;
	float yscale;
	Figure figure;

	public Scale(IFigureApplet fpa, PropertyManager inheritedProps, IValue xs,
			IValue ys, IConstructor c, IEvaluatorContext ctx) {
		super(fpa, inheritedProps);
		xscale = xs.getType().isIntegerType() ? ((IInteger) xs).intValue()
				                              : ((IReal) xs).floatValue();
		
		yscale = ys.getType().isIntegerType() ? ((IInteger) ys).intValue()
                							  : ((IReal) ys).floatValue();
		
		figure = FigureFactory.make(fpa, c, properties, ctx);
	}

	@Override
	public
	void bbox(float desiredWidth, float desiredHeight) {
		figure.bbox(AUTO_SIZE, AUTO_SIZE);
		width = xscale * figure.width;
		height = yscale * figure.height;
	}

	@Override
	public
	void draw(float left, float top) {
		fpa.pushMatrix();
		fpa.translate(left, top);
		fpa.scale(xscale, yscale);
		figure.draw(0,0);
		fpa.popMatrix();
	}

}
