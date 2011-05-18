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
package org.rascalmpl.library.vis;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.containers.WithInnerFig;
import org.rascalmpl.library.vis.properties.Measure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Dimension;

/**
 * Vertex: a point that is part of a shape.
 * TODO: subclass from container?
 * 
 * @author paulk
 *
 */
public class Vertex extends WithInnerFig {
	Measure deltax;
	Measure deltay;
	double leftAnchor;
	double rightAnchor;
	double topAnchor;
	double bottomAnchor;
	private static boolean debug = false;

	
	
	
	public Vertex(IFigureApplet fpa, Measure dx, Measure dy, Figure inner,PropertyManager properties) {
		super(fpa, inner, properties);
		deltax = dx;
		deltay = dy;
	}
	
	public Measure getDeltaXMeasure(){
		return deltax;
	}
	
	public Measure getDeltaYMeasure(){
		return deltay;
	}

	public double getDeltaX(){
		return getScaled(deltax,Dimension.X);
	}
	
	public double getDeltaY(){
		return getScaled(deltay,Dimension.Y);
	}
	
	@Override
	public
	void bbox(double desiredWidth, double desiredHeight){
		/*
		if(marker != null){
			//TODO is this ok?
			marker.bbox(AUTO_SIZE, AUTO_SIZE);
			if(debug) System.err.printf("Vertex: marker anchors hor (%f, %f), vert (%f, %f)\n",
					   marker.leftAlign(), marker.rightAlign(), marker.topAlign(), marker.bottomAlign());
			if(marker.leftAlign() >= deltax.value){
				leftAnchor = marker.leftAlign() - deltax.value;
				width = marker.width;
				rightAnchor = width - leftAnchor;
			} else {
				leftAnchor = 0;
				width = deltax.value + marker.rightAlign();
				rightAnchor = width;
			}
			
			if(marker.bottomAlign() >= deltay.value){
				bottomAnchor = marker.bottomAlign();
				topAnchor = marker.topAlign() + deltay.value;
				height = bottomAnchor + topAnchor;
			} else {
				bottomAnchor = 0;
				height = deltay.value + marker.topAlign();
				topAnchor = height;
			}
			
		} else {
			width = deltax.value;
			height = deltay.value;
			leftAnchor = bottomAnchor = 0;
			rightAnchor = width;
			topAnchor = height;
		}
		if(debug)System.err.printf("bbox.vertex: deltax=%f, deltay=%f, width = %f (%f, %f), height= %f (%f, %f))\n", 
							deltax, deltay, width, leftAnchor, rightAnchor, height, topAnchor, bottomAnchor);
		*/
		if(innerFig != null){
			innerFig.bbox(AUTO_SIZE, AUTO_SIZE);
			leftAnchor = innerFig.leftAlign();
			rightAnchor = innerFig.rightAlign();
			topAnchor = innerFig.topAlign();
			bottomAnchor = innerFig.bottomAlign();
		} else {
			leftAnchor = rightAnchor = topAnchor = bottomAnchor = 0;
		}
	}
	
	@Override
	public
	void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		applyProperties();
		if(debug){
			System.err.println("Vertex: marker = " + innerFig);
			System.err.printf("Vertex: marker at %f, %f\n", left, top);
		}
		if(innerFig != null){
			innerFig.bbox(AUTO_SIZE, AUTO_SIZE);
			innerFig.draw(left, top);
		}
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

}
