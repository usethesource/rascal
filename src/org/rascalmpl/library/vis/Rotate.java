/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class Rotate extends Figure {
	private Figure figure;
	private float angle;
	private float leftAnchor;
	private float rightAnchor;
	private float topAnchor;
	private float bottomAnchor;
	private static boolean debug = false;
	private float sina;
	private float cosa;
	
	Rotate(IFigureApplet fpa, PropertyManager inherited, IValue rangle, IConstructor c, IEvaluatorContext ctx) {
		super(fpa, inherited);
		float a = rangle.getType().isIntegerType() ? ((IInteger) rangle).intValue()
				                                    : ((IReal) rangle).floatValue();
		angle = FigureApplet.radians(a);
		figure = FigureFactory.make(fpa, c, properties, null, ctx);
	}

	@Override
	public
	void bbox(float desiredWidth, float desiredHeight) {
		
		figure.bbox(AUTO_SIZE, AUTO_SIZE);
		
		sina = abs(FigureApplet.sin(angle));
		cosa = abs(FigureApplet.cos(angle));
		
		float hanch = figure.getHanchorProperty();
		float vanch = figure.getVanchorProperty();
		
		float w = figure.width;
		float h = figure.height;
		
		width  = h * sina + w * cosa;
		height = h * cosa + w * sina;
		
		leftAnchor = hanch * width;
		rightAnchor = (1-hanch) * width;
		
		topAnchor = vanch * height;
		bottomAnchor = (1-vanch) * height;
		
		if(debug)System.err.printf("rotate.bbox: width=%f (%f, %f), height=%f (%f, %f)\n", 
				   width, leftAnchor, rightAnchor, height, topAnchor, bottomAnchor);
	}

	@Override
	public
	void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		
		fpa.pushMatrix();
		// move origin to the anchor of the figure to be rotated
		fpa.translate(left + width/2, top + height/2);
		// rotate it
		fpa.rotate(angle);
		// move origin to the left top corner of figure.
		figure.draw(-figure.width/2, -figure.height/2);
		fpa.popMatrix();
	}
	
	@Override
	public float leftAlign(){
		return leftAnchor;
	}
	
	@Override
	public float rightAlign(){
		return rightAnchor;
	}
	
	@Override
	public float topAlign(){
		return topAnchor;
	}
	
	@Override
	public float bottomAlign(){
		return bottomAnchor;
	}
	
	@Override
	public void drawFocus(){
		if(isVisible()){
			fpa.pushMatrix();
			fpa.translate(getLeft() + width/2, getTop() + height/2);
			fpa.rotate(angle);
			fpa.stroke(255, 0,0);
			fpa.noFill();
			fpa.rect(-figure.width/2, -figure.height/2, figure.width, figure.height);
		}
	}
}
