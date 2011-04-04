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
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/**
 * Horizontal composition of elements:
 * - when alignAnchors==true, using their vertical anchor for alignment
 * - otherwise using current alignment settings
 * 
 * @author paulk
 *
 */
public class HCat extends Compose {
	
	float hgap;
	float topAnchor = 0;
	float bottomAnchor = 0;
	private boolean alignAnchors = false;
	private static boolean debug = false;

	public HCat(FigurePApplet fpa, IPropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
	}
	
	@Override
	public
	void bbox(){
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
		topAnchor = 0;
		bottomAnchor = 0;
		hgap = getHGapProperty();
		for(Figure fig : figures){
			fig.bbox();
			width += fig.width;
			topAnchor = max(topAnchor, fig.topAnchor());
			bottomAnchor = max(bottomAnchor, fig.bottomAnchor());
			if(debug)System.err.printf("hcat (loop): topAnchor=%f, bottomAnchor=%f\n", topAnchor, bottomAnchor);
		} 
		int ngaps = (figures.length - 1);
		width += ngaps * hgap;
		height = topAnchor + bottomAnchor;
		
		if(debug)System.err.printf("hcat: width=%f, height=%f, topAnchor=%f, bottomAnchor=%f\n", width, height, topAnchor, bottomAnchor);
	}	
	
	public
	void bboxStandard(){
		width = 0;
		height = 0;
		
		float valign = getValignProperty();
		
		hgap = getHGapProperty();
		for(Figure fig : figures){
			fig.bbox();
			width += fig.width;
			height = max(height, fig.height);
			if(debug)System.err.printf("hcat (loop): topAnchor=%f, bottomAnchor=%f\n", topAnchor, bottomAnchor);
		} 
		int ngaps = (figures.length - 1);
		width += ngaps * hgap;
		topAnchor = valign * height;
		bottomAnchor = (1 - valign) * height;
		
		if(debug)System.err.printf("hcat: width=%f, height=%f, topAnchor=%f, bottomAnchor=%f\n", width, height, topAnchor, bottomAnchor);
	}	
	
	@Override
	public
	void draw(float left, float top){
		this.setLeft(left);
		this.setTop(top);
	
		applyProperties();
		if(alignAnchors){
			// Draw from left to right
			for(Figure fig : figures){
				fig.draw(left, top + topAnchor - fig.topAnchor());
				left += fig.width + hgap;
			}
		} else {
			float valign = getValignProperty();
			for(Figure fig : figures){
				float vpad = valign * (height - fig.height);
				fig.draw(left, top + vpad);
				left += fig.width + hgap;
			}
		}
	}
	
	@Override
	public float topAnchor(){
		return topAnchor;
	}
	
	@Override
	public float bottomAnchor(){
		return bottomAnchor;
	}
	
}
