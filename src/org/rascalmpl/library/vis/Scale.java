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

import org.rascalmpl.library.vis.containers.WithInnerFig;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class Scale extends WithInnerFig {
	double xscale;
	double yscale;
	boolean propagated;

	public Scale(IFigureApplet fpa,double scaleX, double scaleY, Figure inner, PropertyManager properties) {
		super(fpa, inner, properties);
		propagated=false;
		
	}

	@Override
	public
	void bbox(double desiredWidth, double desiredHeight) {
		if(properties.getBooleanProperty(Properties.SCALE_ALL) && !propagated) propagateScaling(1.0f, 1.0f,null);
		innerFig.bbox(AUTO_SIZE, AUTO_SIZE);
	}

	@Override
	public
	void draw(double left, double top) {
		if(properties.getBooleanProperty(Properties.SCALE_ALL)){
			fpa.pushMatrix();
			fpa.translate(left, top);
			fpa.scale(xscale, yscale);
			innerFig.draw(0,0);
			fpa.popMatrix();
		} else {
			innerFig.draw(left, top);
		}
	}

}
