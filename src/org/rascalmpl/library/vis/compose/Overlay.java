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
import org.rascalmpl.library.vis.properties.IPropertyManager;


/**
 * 
 * Overlay elements by stacking them:
 * - when alignAnchors==true aligned around their anchor point
 * - otherwise aligned according to current alignment settings.
 * 
 * @author paulk
 *
 */
public class Overlay extends Compose {
	
	private static boolean debug = false;
	private boolean alignAnchors = false;
	float topAnchor = 0;
	float bottomAnchor = 0;
	float leftAnchor = 0;
	float rightAnchor = 0;

	public Overlay(IFigureApplet fpa, IPropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
	}
	
	@Override
	public void bbox(){
		alignAnchors = getAlignAnchorsProperty();
		if(alignAnchors)
			bboxAlignAnchors();
		else
			bboxStandard();
	}
	
	public void bboxAlignAnchors(){
		
		topAnchor = bottomAnchor = leftAnchor = rightAnchor = 0;
		
		for(Figure ve : figures){
			ve.bbox();
			topAnchor = max(topAnchor, ve.topAnchor());
			bottomAnchor = max(bottomAnchor, ve.bottomAnchor());
			leftAnchor = max(leftAnchor, ve.leftAnchor());
			rightAnchor = max(rightAnchor, ve.rightAnchor());
		}
		width = leftAnchor + rightAnchor;
		height = topAnchor + bottomAnchor;
		if(debug)System.err.printf("overlay.bbox: width=%f, height=%f\n", width, height);
	}
	
	public void bboxStandard(){
		width = height = 0;
		
		for(Figure fig : figures){
			fig.bbox();
			width = max(width, fig.width);
			height = max(height, fig.height);
		}
		if(debug)System.err.printf("overlay.bbox: width=%f, height=%f\n", width, height);
	}
	
	
	@Override
	public void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		
		applyProperties();
		if(debug)System.err.printf("overlay.draw: left=%f, top=%f\n", left, top);
		if(alignAnchors){
			for(Figure fig : figures){	
				fig.draw(left + leftAnchor - fig.leftAnchor(), top + topAnchor - fig.topAnchor());
			}
		} else {
			float halign = getHalignProperty();
			float valign = getValignProperty();
			
			for(Figure fig : figures){	
				float hpad = halign * (width - fig.width);
				float vpad = valign * (height - fig.height);
				fig.draw(left + hpad, top + vpad);
			}
		}
	}
}
