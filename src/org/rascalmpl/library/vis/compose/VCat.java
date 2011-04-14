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
 * @author paulk
 *
 */
public class VCat extends Compose {
	
	float vgap;
	float leftAnchor = 0;
	float rightAnchor = 0;
	private boolean alignAnchors = false;
	private static boolean debug = false;

public VCat(IFigureApplet fpa, PropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
	}
	
	@Override
	public
	void bbox(float desiredMajorSize, float desiredMinorSize){
		alignAnchors = getAlignAnchorsProperty();
		if(alignAnchors)
			bboxAlignAnchors();
		else
			bboxStandard();
	}
	
	public
	void bboxAlignAnchors(){
		width = 0;
		height = 0;
		leftAnchor = 0;
		rightAnchor = 0;
		vgap = getVGapProperty();
		if(debug)System.err.printf("vertical.bbox: vgap=%f\n", vgap);
		for(Figure fig : figures){
			fig.bbox(Figure.AUTO_SIZE,Figure.AUTO_SIZE);
			leftAnchor = max(leftAnchor, fig.leftAnchor());
			rightAnchor = max(rightAnchor, fig.rightAnchor());
			height = height + fig.height;
		}
		
		width = leftAnchor + rightAnchor;
		int ngaps = (figures.length - 1);
		
		height += ngaps * vgap;
		if(debug)System.err.printf("vcat: width=%f, height=%f, leftAnchor=%f, rightAnchor=%f\n", width, height, leftAnchor, rightAnchor);
	}
	
	public void bboxStandard(){
		width = 0;
		height = 0;

		float halign = getHalignProperty();
		vgap = getVGapProperty();
		if(debug)System.err.printf("vertical.bbox: vgap=%f\n", vgap);
		for(Figure fig : figures){
			fig.bbox(Figure.AUTO_SIZE,Figure.AUTO_SIZE);
			width = max(width, fig.width);
			height += fig.height;
		}
		int ngaps = (figures.length - 1);
		height += ngaps * vgap;
		leftAnchor = halign * width;
		rightAnchor = (1 - halign) * width;
		
		if(debug)System.err.printf("vcat: width=%f, height=%f, leftAnchor=%f, rightAnchor=%f\n", width, height, leftAnchor, rightAnchor);
	}
	
	@Override
	public
	void draw(float left, float top){
		this.setLeft(left);
		this.setTop(top);

		applyProperties();
		if(alignAnchors){
			float bottom = top + height;

			// Draw from top to bottom
			for(int i = figures.length-1; i >= 0; i--){
				if(debug)System.err.printf("vertical.draw: i=%d, vgap=%f, bottom=%f\n", i, vgap, bottom);
				Figure fig = figures[i];
				float h = fig.height;
				fig.draw(left + leftAnchor - fig.leftAnchor(), bottom - h);
				bottom -= h + vgap;
			}
		} else {
			float currentTop = top;
			float halign = getHalignProperty();
			for(Figure fig : figures){
				float hpad = halign * (width - fig.width);
				fig.draw(left + hpad, currentTop);
				currentTop += fig.height + vgap;
			}
		}
	}
	
	@Override
	public float leftAnchor(){
		return leftAnchor;
	}
	
	@Override
	public float rightAnchor(){
		return rightAnchor;
	}
}
