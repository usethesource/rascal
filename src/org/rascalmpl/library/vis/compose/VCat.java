/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.compose;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

/**
 * 
 * Vertical composition of elements:
 * - when alignAnchors==true, using their horizontal anchor for alignment.
 * - otherwise using current alignment settings
 * 
 * VCat is a HCat but with the axises swapped
 * 
 * @author paulk
 *
 */
public class VCat extends HCat {

	public VCat(IFigureApplet fpa, PropertyManager properties, IList elems,
			IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
	}
	
	void setProperties(){
		isWidthPropertySet = isHeightPropertySet();
		isHeightPropertySet = isWidthPropertySet();
		isHGapPropertySet = isVGapPropertySet();
		isHGapFactorPropertySet = isVGapFactorPropertySet();
		
		getWidthProperty = getHeightProperty();
		getHeightProperty = getWidthProperty();
		getHGapProperty = getVGapProperty();
		getHGapFactorProperty = getVGapFactorProperty();
	}
	
	public void bbox(float desiredWidth,float desiredHeight){
		super.bbox(desiredHeight,desiredWidth);
		float tmp = width;
		width = height;
		height = tmp;
		
	}
	
	float getFigureWidth(Figure fig){ return fig.height; }
	float getFigureHeight(Figure fig){return fig.width;}
	float getTopAnchor(Figure fig){return fig.leftAlign();}
	float getBottomAnchor(Figure fig){return fig.rightAlign();}
	void  drawFigure(Figure fig,float left,float top,float leftBase,float topBase){
		fig.draw(leftBase + top, topBase + left);
	}
	void  bboxOfFigure(Figure fig,float desiredWidth,float desiredHeight){ fig.bbox(desiredHeight,desiredWidth);}
	float getHeight(){return width;}
	
}
