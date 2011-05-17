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
package org.rascalmpl.library.vis.compose;


import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.containers.HScreen.ProjectionPlacement;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.descriptions.HandlerProp;
import org.rascalmpl.library.vis.util.Coordinate;

/*
 * Given are a first figure (bottomFigure) that contains a second figure (refFigure) with identity id.
 * Place a third figure (topFigure) on top of refFigure
 */
public class Place extends Figure {
	
	private Figure bottomFigure;
	private Figure refFigure;
	private Figure topFigure;


	public Place(IFigureApplet fpa, PropertyManager properties, IConstructor ctop, IString id, IConstructor cbot, IEvaluatorContext ctx) {
		super(fpa, properties);
		this.bottomFigure = FigureFactory.make(fpa, cbot, properties, null, ctx);
		this.topFigure = FigureFactory.make(fpa, ctop, properties, null, ctx);
		this.refFigure = fpa.getRegisteredId(id.getValue());
		if(this.refFigure == null)
			throw RuntimeExceptionFactory.figureException("Cannot place on not (yet) existing figure", id, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}

	@Override
	public void bbox(double desiredWidth, double desiredHeight) {
		bottomFigure.bbox(AUTO_SIZE, AUTO_SIZE);
		topFigure.bbox(AUTO_SIZE, AUTO_SIZE);
		
		double halign = getHAlignProperty();
		double valign = getVAlignProperty();
		width = Math.max(bottomFigure.width, halign * refFigure.width + topFigure.width/2);
		height = Math.max(bottomFigure.height, valign * refFigure.height + topFigure.height/2);
	}

	@Override
	public void draw(double left, double top) {
		setLeft(left);
		setTop(top);
		double halign = getHAlignProperty();
		double valign = getVAlignProperty();
		bottomFigure.draw(left, top);
		topFigure.draw(refFigure.getLeft() + halign * refFigure.width - topFigure.width/2,
				       refFigure.getTop()  + valign * refFigure.height - topFigure.height/2);
	}
	
	@Override
	public void destroy(){
		bottomFigure.destroy();
        topFigure.destroy();
	}

	@Override
	public void gatherProjections(double left, double top,
			Vector<ProjectionPlacement> projections, boolean first,
			String screenId, boolean horizontal) {
		// TODO: Implement this...
		
	}
	

	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		// TODO: Implement this...
		return false;
	}
	
	public void executeMouseOverOffHandlers(HandlerProp prop) {
		// TODO: Implement this...
	}
}
