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

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.containers.HScreen;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.descriptions.BoolProp;

public class Scale extends Figure {
	double xscale;
	double yscale;
	Figure figure;
	boolean propagated;

	public Scale(IFigureApplet fpa, PropertyManager inheritedProps, IValue xs,
			IValue ys, IConstructor c, IEvaluatorContext ctx) {
		super(fpa, inheritedProps);
		xscale = xs.getType().isIntegerType() ? ((IInteger) xs).intValue()
				                              : ((IReal) xs).floatValue();
		
		yscale = ys.getType().isIntegerType() ? ((IInteger) ys).intValue()
                							  : ((IReal) ys).floatValue();
		
		figure = FigureFactory.make(fpa, c, properties, null, ctx);
		propagated=false;
		
	}

	@Override
	public
	void bbox(double desiredWidth, double desiredHeight) {
		if(properties.getBooleanProperty(BoolProp.SCALE_ALL) && !propagated) propagateScaling(1.0f, 1.0f,null);
		figure.bbox(AUTO_SIZE, AUTO_SIZE);
	}

	@Override
	public
	void draw(double left, double top) {
		if(properties.getBooleanProperty(BoolProp.SCALE_ALL)){
			fpa.pushMatrix();
			fpa.translate(left, top);
			fpa.scale(xscale, yscale);
			figure.draw(0,0);
			fpa.popMatrix();
		} else {
			figure.draw(left, top);
		}
	}
	
	public void propagateScaling(double scaleX,double scaleY, HashMap<String,Double> axisScales){
		super.propagateScaling(scaleX, scaleY, axisScales);
		figure.propagateScaling(scaleX, scaleY, axisScales);
	}
	
	public void gatherProjections(double left, double top, Vector<HScreen.ProjectionPlacement> projections, boolean first, String screenId, boolean horizontal){
		if(figure!=null){
			figure.gatherProjections(left, top, projections, first, screenId, horizontal);
		}
	}
	

	public Extremes getExtremesForAxis(String axisId, double offset, boolean horizontal){
		throw new UnsupportedOperationException("No rotate on axises yet");
	}

}
