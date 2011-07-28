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

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;


/**
 * HVCat elements on consecutive rows. Width is determined by the width property, height is
 * determined by the number and size of the elements. This is similar to aligning words in
 * a text but is opposed to composition in a grid, where the elements are placed on fixed
 * grid positions.
 * 
 * @author paulk
 *
 */

// TODO: fix me for resizing!
public class HVCat extends Compose {
	
	double leftElem[];
	double topRowElem[];
	double rowHeight[];
	double rowWidth[];
	int inRow[];
	
	static boolean debug = false;

	public HVCat(Figure[] figures, PropertyManager properties) {
		super(figures, properties);
		leftElem = new double[figures.length];
		topRowElem = new double[figures.length];
		rowHeight = new double[figures.length];
		rowWidth = new double[figures.length];
		inRow = new int[figures.length];
	}
	
	@Override
	public void bbox(){
		
		minSize.setWidth(getWidthProperty());
		minSize.setHeight(0);
		double w = 0;
		double hrow = 0;
		double toprow = 0;
		int nrow = 0;
		double hgap = getHGapProperty();
		double vgap = getVGapProperty();
		for(int i = 0; i < figures.length; i++){
			Figure fig = figures[i];
			fig.bbox();
			if(w + hgap + fig.minSize.getWidth() > minSize.getWidth()){
				if(w == 0){
					minSize.setWidth(fig.minSize.getWidth());
				} else {
					rowHeight[nrow] = hrow;
					rowWidth[nrow] = w;
					nrow++;
					minSize.setHeight(minSize.getHeight() + hrow + vgap);
					toprow = minSize.getHeight();
					w = hrow = 0;
				}
			}
			leftElem[i] = w;
			topRowElem[i] = toprow;
			inRow[i] = nrow;
			w += fig.minSize.getWidth() + hgap;
			hrow = Math.max(hrow, fig.minSize.getHeight());
	
		}
		rowHeight[nrow] = hrow;
		rowWidth[nrow] = w;
		minSize.setHeight(minSize.getHeight() + hrow);
		if(nrow == 0)
			minSize.setWidth(w - hgap);
		if(debug)System.err.printf("HVCat.bbox: width=%f, height=%f\n", minSize.getWidth(), minSize.getHeight());
		
		determinePlacement();
		setNonResizable();
		super.bbox();
	}

	private void determinePlacement() {
		for(int i = 0; i < figures.length; i++){
			Figure fig = figures[i];
			double hrow = rowHeight[inRow[i]];
			double rfiller = minSize.getWidth() - rowWidth[inRow[i]];
			pos[i].setX( leftElem[i] + fig.getHAlignProperty()  * rfiller);
			pos[i].setY( topRowElem[i] +  fig.getVAlignProperty() * (hrow - fig.minSize.getHeight()));              
		}
	}
	
}
