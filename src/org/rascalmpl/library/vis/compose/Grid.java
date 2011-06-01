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
	

	public Grid(IFigureApplet fpa, Figure[] figures, PropertyManager properties) {
		super(fpa,figures, properties );
		leftFig = new double[figures.length];
		topFig = new double[figures.length];
	}
	
	private void updateMax(Vector<Double> sizes, int index, double val){
		if(sizes.size() > index){
			sizes.set(index, Math.max(sizes.get(index), val));
		}
		sizes.add(val);
	}
	
	@Override
	public void bbox(){
		minSize.setWidth(getWidthProperty());
		minSize.setHeight(0);
		
		for(Figure fig : figures){
			fig.bbox();
		}
		
		int nrCollumns = computeNrCollumns();
		int nrRows = (int)Math.ceil( (double)figures.length / (double)nrCollumns ); 
		
		double[] collumnWidths = new double[nrCollumns];
		double[] rowHeights = new double[nrRows];
		
		int row = 0;
		int collumn = 0;
		for(int i = 0 ; i < figures.length ; i++){
			collumnWidths[collumn] = Math.max(collumnWidths[collumn] , figures[i].minSize.getWidth());
			rowHeights[row] = Math.max(rowHeights[row] , figures[i].minSize.getHeight());
			collumn++;
			if(collumn >= nrCollumns){
				collumn = 0;
				row++;
			}
		}
		minSize.setHeight(0);
		for(int i = 0 ; i < nrRows ; i++){
			minSize.setHeight(minSize.getHeight() + rowHeights[i]);
		}
		
		double x, y;
		row = 0;
		collumn = 0;
		x = y = 0;
		for(int i = 0 ; i < figures.length ; i++){
			Figure fig = figures[i];
			xPos[i] = x + ((collumnWidths[collumn]- fig.minSize.getWidth())) * fig.getHAlignProperty();
			yPos[i] = y + ((rowHeights[row] - fig.minSize.getHeight())) * fig.getVAlignProperty();
			x+= collumnWidths[collumn];
			collumn++;
			if(collumn >= nrCollumns){
				collumn = 0;
				x = 0;
				y+=rowHeights[row];
				row++;
			}
		}
		setNonResizable();
		super.bbox();
		
	}

	int computeNrCollumns() {
		int nrCollumnsGuess = 1;
		double totalCollumnWidth;
		boolean fillCompleted = false;
		do{
			double []collumnWidths = new double[nrCollumnsGuess];
			for(int rowOffset = 0 ; rowOffset < figures.length ; rowOffset+=nrCollumnsGuess){
				for(int collumnOffset = 0 ; collumnOffset < nrCollumnsGuess && rowOffset + collumnOffset < figures.length ; collumnOffset++){
					collumnWidths[collumnOffset] = Math.max(collumnWidths[collumnOffset] , figures[rowOffset+collumnOffset].minSize.getWidth());
				}
			}
			totalCollumnWidth = 0.0;
			for(int i = 0 ; i < nrCollumnsGuess ; i++){
				totalCollumnWidth+=collumnWidths[i];
			}

			nrCollumnsGuess++;
			fillCompleted = collumnWidths[collumnWidths.length - 1] == 0;
		} while(totalCollumnWidth < minSize.getWidth() && !fillCompleted);

		return  Math.max(1, nrCollumnsGuess-1);
	}
}
