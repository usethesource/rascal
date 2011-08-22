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

import java.util.Vector;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.PropertyValue;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.util.vector.TransformMatrix;


/*
public class Rotate extends WithInnerFig{
	// TODO: fix me
	PropertyValue<Double> angle;

	public Rotate(PropertyValue<Double> angle,Figure inner, PropertyManager properties) {
		super(inner, properties);
		this.angle = angle;
	}
	
	@Override
	public void computeFiguresAndProperties(Vector<FigureWithVisiblity> visibleChildren, ICallbackEnv env) {
		angle.compute(env);
		super.computeFiguresAndProperties(visibleChildren, env);
	}

	@Override
	public void computeMinSize(Vector<FigureWithVisiblity> visibleChildren) {
		for(boolean flip : BOTH_DIMENSIONS){
			minSize.setWidth(flip, innerFig.minSize.getWidth(flip) * getGrowFactor(flip));
		}
		if(getBooleanProperty(Properties.ALLOW_ROTATE_FULL)){
			double w = minSize.getWidth();
			double h = minSize.getHeight();
			double diagonal = Math.sqrt(w*w + h*h);
			minSize.set(diagonal,diagonal);
		} else {
			TransformMatrix t = new TransformMatrix();
			t.rotate(angle.getValue());
			t.transformBoundingBox(minSize);
		}
	}
	
	public void setTransform(TransformMatrix transform, double back) {
		transform.translate(size.getWidth()/2.0, size.getHeight()/2.0);
		transform.rotate(angle.getValue());
	}
	
	public void unsetTransform(TransformMatrix transform) {
		transform.translate(size.getWidth()/2.0, size.getHeight()/2.0);
		transform.rotate(-angle.getValue());
		transform.translate(-size.getWidth()/2.0, -size.getHeight()/2.0);
	}
	
	@Override
	public void resize(Vector<FigureWithVisiblity> visibleChildren,
			ResizeMode resizeMode, TransformMatrix transform) {
		if(getBooleanProperty(Properties.ALLOW_ROTATE_FULL)){
			
		} else {
			double c = Math.abs(Math.cos(angle.getValue()));
			double s = Math.abs(Math.sin(angle.getValue()));
			innerFig.size.setWidth(size.getWidth() * c * c / getGrowFactor(false));
			innerFig.size.setHeight(size.getHeight() * s * s / getGrowFactor(false));
		}
	}
	
}
*/
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
