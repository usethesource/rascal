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
package org.rascalmpl.library.vis.figure.combine;

// TODO: fix me! also take care global coordinates
/*
public class Rotate extends WithInnerFig {
	private double angle;
	private double leftAnchor;
	private double rightAnchor;
	private double topAnchor;
	private double bottomAnchor;
	private static boolean debug = false;
	private double sina;
	private double cosa;
	
	Rotate(IFigureApplet fpa, double angle, Figure inner, PropertyManager properties) {
		super(fpa, inner,properties);
		angle = FigureApplet.radians(angle);
	}

	@Override
	public
	void bbox() {
		innerFig.bbox();
		for(boolean flip : BOTH_DIMENSIONS){
			setResizableX(flip, innerFig.getResizableX(flip));
		}
		minSize.setWidth(innerFig.minSize.getWidth()*Math.cos(angle) + innerFig.minSize.getHeight()* Math.sin(angle));
		minSize.setWidth(innerFig.minSize.getHeight()*Math.cos(angle) + innerFig.minSize.getWidth()* Math.sin(angle));
		/*
		sina = FigureApplet.abs(FigureApplet.sin(angle));
		cosa =  FigureApplet.abs(FigureApplet.cos(angle));
		
		double hanch = innerFig.getHAlignProperty();
		double vanch = innerFig.getVAlignProperty();
		
		double w = innerFig.minSize.getWidth();
		double h = innerFig.minSize.getHeight();
		
		minSize.setWidth(h * sina + w * cosa);
		minSize.setHeight(h * cosa + w * sina);
		
		leftAnchor = hanch * minSize.getWidth();
		rightAnchor = (1-hanch) * minSize.getWidth();
		
		topAnchor = vanch * minSize.getHeight();
		bottomAnchor = (1-vanch) * minSize.getHeight();
		
		if(debug)System.err.printf("rotate.bbox: width=%f (%f, %f), height=%f (%f, %f)\n", 
				   minSize.getWidth(), leftAnchor, rightAnchor, minSize.getHeight(), topAnchor, bottomAnchor);
		setNonResizable();
		super.bbox();
		*/
	/*
	}
	
	public void layout(){
		
	}

	@Override
	public
	void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		
		fpa.pushMatrix();
		// move origin to the anchor of the figure to be rotated
		fpa.translate(left + minSize.getWidth()/2, top + minSize.getHeight()/2);
		// rotate it
		fpa.rotate(angle);
		// move origin to the left top corner of figure.
		innerFig.draw(-innerFig.minSize.getWidth()/2, -innerFig.minSize.getHeight()/2);
		fpa.popMatrix();
	}
	
	@Override
	public double leftAlign(){
		return leftAnchor;
	}
	
	@Override
	public double rightAlign(){
		return rightAnchor;
	}
	
	@Override
	public double topAlign(){
		return topAnchor;
	}
	
	@Override
	public double bottomAlign(){
		return bottomAnchor;
	}
	
	@Override
	public void drawFocus(){
		fpa.pushMatrix();
		fpa.translate(getLeft() + minSize.getWidth()/2, getTop() + minSize.getHeight()/2);
		fpa.rotate(angle);
		fpa.stroke(255, 0,0);
		fpa.noFill();
		fpa.rect(-innerFig.minSize.getWidth()/2, -innerFig.minSize.getHeight()/2, innerFig.minSize.getWidth(), innerFig.minSize.getHeight());
	}

}
*/
