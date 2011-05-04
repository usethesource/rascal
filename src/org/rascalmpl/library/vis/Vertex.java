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
import org.rascalmpl.library.vis.properties.Measure;
import org.rascalmpl.library.vis.properties.PropertyManager;

/**
 * Vertex: a point that is part of a shape.
 * TODO: subclass from container?
 * 
 * @author paulk
 *
 */
public class Vertex extends Figure {
	Figure marker;
	Measure deltax;
	Measure deltay;
	float leftAnchor;
	float rightAnchor;
	float topAnchor;
	float bottomAnchor;
	private static boolean debug = false;

	private float getIntOrReal(IValue v){
		if(v.getType().isIntegerType())
			return ((IInteger) v).intValue();
		if(v.getType().isRealType())
			return ((IReal) v).floatValue();
		return 0;
	}
	
	private Measure getMeasure(IValue val){
		IConstructor c = (IConstructor) val;
		return new Measure(getIntOrReal(c.get(0)),((IString)c.get(1)).getValue());
	}
	
	public Vertex(IFigureApplet fpa, PropertyManager properties, IValue dx, IValue dy) {
		super(fpa, properties);
		deltax = getMeasure(dx);
		deltay = getMeasure(dy);
	}
	
	public Vertex(IFigureApplet fpa, PropertyManager properties, IValue dx, IValue dy, IConstructor marker, IEvaluatorContext ctx) {
		super(fpa, properties);
		deltax = getMeasure(dx);
		deltay = getMeasure(dy);
		if(marker != null)
			this.marker = FigureFactory.make(fpa, marker, properties, null, ctx);
		if(debug)System.err.printf("Vertex at %f, %f\n", deltax, deltay);
	}
	
	public Measure getDeltaXMeasure(){
		return deltax;
	}
	
	public Measure getDeltaYMeasure(){
		return deltay;
	}

	public float getDeltaX(){
		return getScaled(deltax,true);
	}
	
	public float getDeltaY(){
		return getScaled(deltay,false);
	}
	
	@Override
	public
	void bbox(float desiredWidth, float desiredHeight){
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
		if(marker != null){
			marker.bbox(AUTO_SIZE, AUTO_SIZE);
			leftAnchor = marker.leftAlign();
			rightAnchor = marker.rightAlign();
			topAnchor = marker.topAlign();
			bottomAnchor = marker.bottomAlign();
		} else {
			leftAnchor = rightAnchor = topAnchor = bottomAnchor = 0;
		}
	}
	
	@Override
	public
	void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		applyProperties();
		if(debug){
			System.err.println("Vertex: marker = " + marker);
			System.err.printf("Vertex: marker at %f, %f\n", left, top);
		}
		if(marker != null){
			marker.bbox(AUTO_SIZE, AUTO_SIZE);
			marker.draw(left, top);
		}
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
	
	public Extremes getExtremesForAxis(String axisId, float offset, boolean horizontal){
		if(marker!=null){
			return marker.getExtremesForAxis(axisId, offset, horizontal);
		} else {
			return new Extremes();
		}
	}
	
	public float getOffsetForAxis(String axisId, float offset, boolean horizontal){
		if(marker!=null){
			return marker.getOffsetForAxis(axisId, offset, horizontal);
		} else {
			return Float.MAX_VALUE;
		}
	}
	

	public void propagateScaling(float scaleX,float scaleY,HashMap<String,Float> axisScales){
		super.propagateScaling(scaleX, scaleY,axisScales);
		if(marker != null){
			marker.propagateScaling(scaleX, scaleY,axisScales);
		}
	}

}
