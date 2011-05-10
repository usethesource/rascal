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

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;


/**
 * Place elements on fixed grid positions. The width is determined by the width property, height is
 * determined by number of elements. Each element is positioned 
 * - with its anchor on the grid point, when alignAnchors==true
 * - aligned relative to grid point using current alignment values, otherwise.
 * 
 * @author paulk
 *
 */
public class Grid extends Compose {
	
	double leftFig[];
	double topFig[];
	
	double extTop = 0;
	double extBot = 0;
	double extLeft = 0;
	double extRight = 0;
	
	private static boolean debug = false;
	

	public Grid(IFigureApplet fpa, PropertyManager properties, IList elems, IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties, elems, childProps, ctx);
		leftFig = new double[elems.length()];
		topFig = new double[elems.length()];
	}
	
	@Override
	public void bbox(double desiredWidth, double desiredHeight){
		
		width = getWidthProperty();
		height = 0;
		double w = 0;
		int nrow = 0;
		
		double hgap = getHGapProperty();
		double vgap = getVGapProperty();
		
		int lastRow = (hgap == 0) ? 0 : figures.length / (1 + (int) (width / hgap)) - 1;
		if(debug)System.err.printf("lastRow = %d\n", lastRow);
		
		extTop = 0;
		extBot = 0;
		extLeft = 0;
		extRight = 0;
		
		for(int i = 0; i < figures.length; i++){
			if(w > width){
				nrow++;
				height += vgap;
				w = 0;
			}
			
			Figure fig = figures[i];
			fig.bbox(AUTO_SIZE, AUTO_SIZE);
			
			if(w == 0)
				extLeft = max(extLeft, fig.leftAlign());
			if(w + hgap >= width)
				extRight = max(extRight, fig.rightAlign());
			if(nrow == 0)
				extTop = max(extTop, fig.topAlign());
			if(nrow == lastRow){
				extBot = max(extBot, fig.bottomAlign());
			}
			
			if(debug)System.err.printf("i=%d, row=%d, w=%f, extLeft=%f, extRight=%f, extTop=%f, extBot=%f\n", i, nrow, w, extLeft, extRight, extTop, extBot);
			
			leftFig[i] = w;
			topFig[i] = height;
			w += hgap;
		}
		width += extLeft + extRight;
		height += extTop + extBot;
		if(debug)System.err.printf("grid.bbox: %f, %f\n", width, height);
		
		determinePlacement();
		
	}

	private void determinePlacement() {
		for(int i = 0; i < figures.length; i++){
			if(debug)System.err.printf("i=%d: %f, %f, \n", i, leftFig[i], topFig[i]);
			
			xPos[i] = extLeft + leftFig[i] - figures[i].leftAlign();
			yPos[i] = extTop + topFig[i] - figures[i].topAlign();
		}
	}
	
	
	
}
