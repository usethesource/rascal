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
	
	private static boolean debug = true;
	

	public Grid(IFigureApplet fpa, PropertyManager properties, IList elems, IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties, elems, childProps, ctx);
		leftFig = new double[elems.length()];
		topFig = new double[elems.length()];
	}
	
	private void updateMax(Vector<Double> sizes, int index, double val){
		if(sizes.size() > index){
			sizes.set(index, Math.max(sizes.get(index), val));
		}
		sizes.add(val);
	}
	
	@Override
	public void bbox(double desiredWidth, double desiredHeight){
		
		width = getWidthProperty();
		height = 0;
		Vector<Double> collumnWidths = new Vector<Double>();
		Vector<Double> rowHeights = new Vector<Double>();
		int rowNr = 0;
		int collumnNr = 0;
		double x = 0;
		
		for(int i = 0 ; i < figures.length ; i++){
			figures[i].bbox(AUTO_SIZE, AUTO_SIZE);
			if(x + figures[i].width > width){
				collumnNr = 0;
				rowNr++;
				x=0.0f;
			}
			updateMax(collumnWidths,collumnNr, figures[i].width);
			updateMax(rowHeights,rowNr, figures[i].height);
			x+=figures[i].width;
		}
		x = 0;
		double y = 0;
		rowNr = 0;
		collumnNr = 0;
		for(int i = 0 ; i < figures.length ; i++){
			Figure fig = figures[i];
			if(x + figures[i].width > width){
				y+=rowHeights.get(rowNr);
				collumnNr = 0;
				rowNr++;
				x=0.0f;
			}
			xPos[i] = x + ((collumnWidths.get(collumnNr)- fig.width)) * fig.getHAlignProperty();
			yPos[i] = y + ((rowHeights.get(rowNr)- fig.height)) * fig.getVAlignProperty();
			x+=collumnWidths.get(collumnNr);
		}
		
	}
}
