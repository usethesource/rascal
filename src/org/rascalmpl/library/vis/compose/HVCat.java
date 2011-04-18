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
 * HVCat elements on consecutive rows. Width is determined by the width property, height is
 * determined by the number and size of the elements. This is similar to aligning words in
 * a text but is opposed to composition in a grid, where the elements are placed on fixed
 * grid positions.
 * 
 * @author paulk
 *
 */
public class HVCat extends Compose {
	
	float leftElem[];
	float topRowElem[];
	float rowHeight[];
	float rowWidth[];
	int inRow[];
	
	static boolean debug = false;

	public HVCat(IFigureApplet fpa, PropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
		leftElem = new float[elems.length()];
		topRowElem = new float[elems.length()];
		rowHeight = new float[elems.length()];
		rowWidth = new float[elems.length()];
		inRow = new int[elems.length()];
	}
	
	@Override
	public void bbox(float desiredWidth, float desiredHeight){
		width = getWidthProperty();
		height = 0;
		float w = 0;
		float hrow = 0;
		float toprow = 0;
		int nrow = 0;
		float hgap = getHGapProperty();
		float vgap = getVGapProperty();
		for(int i = 0; i < figures.length; i++){
			Figure fig = figures[i];
			fig.bbox(AUTO_SIZE, AUTO_SIZE);
			if(w + hgap + fig.width > width){
				if(w == 0){
					width = fig.width;
				} else {
					rowHeight[nrow] = hrow;
					rowWidth[nrow] = w;
					nrow++;
					height += hrow + vgap;
					toprow = height;
					w = hrow = 0;
				}
			}
			leftElem[i] = w;
			topRowElem[i] = toprow;
			inRow[i] = nrow;
			w += fig.width + hgap;
			hrow = max(hrow, fig.height);
	
		}
		rowHeight[nrow] = hrow;
		rowWidth[nrow] = w;
		height += hrow;
		if(nrow == 0)
			width = w - hgap;
		if(debug)System.err.printf("HVCat.bbox: width=%f, height=%f\n", width, height);
	}
	
	@Override
	public
	void draw(float left, float top){
		this.setLeft(left);
		this.setTop(top);

		applyProperties();

		for(int i = 0; i < figures.length; i++){
			Figure fig = figures[i];
			float hrow = rowHeight[inRow[i]];
			float rfiller = width - rowWidth[inRow[i]];

			fig.draw(left + leftElem[i] + fig.getHanchorProperty() * rfiller,
					top + topRowElem[i] + fig.getVanchorProperty() * (hrow - fig.height));                  
		}
		
	}
}
