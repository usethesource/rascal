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
		
		for(Figure fig : figures){
			fig.bbox(AUTO_SIZE,AUTO_SIZE);
		}
		
		int nrCollumns = computeNrCollumns();
		int nrRows = (int)Math.ceil( (double)figures.length / (double)nrCollumns ); 
		
		double[] collumnWidths = new double[nrCollumns];
		double[] rowHeights = new double[nrRows];
		
		int row = 0;
		int collumn = 0;
		for(int i = 0 ; i < figures.length ; i++){
			collumnWidths[collumn] = Math.max(collumnWidths[collumn] , figures[i].width);
			rowHeights[row] = Math.max(rowHeights[row] , figures[i].height);
			collumn++;
			if(collumn >= nrCollumns){
				collumn = 0;
				row++;
			}
		}
		height = 0;
		for(int i = 0 ; i < nrRows ; i++){
			height+=rowHeights[i];
		}
		
		double x, y;
		row = 0;
		collumn = 0;
		x = y = 0;
		for(int i = 0 ; i < figures.length ; i++){
			Figure fig = figures[i];
			xPos[i] = x + ((collumnWidths[collumn]- fig.width)) * fig.getHAlignProperty();
			yPos[i] = y + ((rowHeights[row] - fig.height)) * fig.getVAlignProperty();
			x+= collumnWidths[collumn];
			collumn++;
			if(collumn >= nrCollumns){
				collumn = 0;
				x = 0;
				y+=rowHeights[row];
				row++;
			}
		}
		
	}

	int computeNrCollumns() {
		int nrCollumnsGuess = 1;
		double totalCollumnWidth;
		boolean fillCompleted = false;
		do{
			double []collumnWidths = new double[nrCollumnsGuess];
			for(int rowOffset = 0 ; rowOffset < figures.length ; rowOffset+=nrCollumnsGuess){
				for(int collumnOffset = 0 ; collumnOffset < nrCollumnsGuess && rowOffset + collumnOffset < figures.length ; collumnOffset++){
					collumnWidths[collumnOffset] = Math.max(collumnWidths[collumnOffset] , figures[rowOffset+collumnOffset].width);
				}
			}
			totalCollumnWidth = 0.0;
			for(int i = 0 ; i < nrCollumnsGuess ; i++){
				totalCollumnWidth+=collumnWidths[i];
			}

			nrCollumnsGuess++;
			fillCompleted = collumnWidths[collumnWidths.length - 1] == 0;
		} while(totalCollumnWidth < width && !fillCompleted);

		return  Math.max(1, nrCollumnsGuess-1);
	}
}
