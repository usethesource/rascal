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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
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
	double deltax;
	double deltay;
	double leftAnchor;
	double rightAnchor;
	double topAnchor;
	double bottomAnchor;
	private static boolean debug = false;

	private double getIntOrReal(IValue v){
		if(v.getType().isIntegerType())
			return ((IInteger) v).intValue();
		if(v.getType().isRealType())
			return ((IReal) v).floatValue();
		return 0;
		
	}
	public Vertex(IFigureApplet fpa, PropertyManager properties, IValue dx, IValue dy) {
		super(fpa, properties);
		deltax = getIntOrReal(dx);
		deltay = getIntOrReal(dy);
	}
	
	public Vertex(IFigureApplet fpa, PropertyManager properties, IValue dx, IValue dy, IConstructor marker, IEvaluatorContext ctx) {
		super(fpa, properties);
		deltax = getIntOrReal(dx);
		deltay = getIntOrReal(dy);
		if(marker != null)
			this.marker = FigureFactory.make(fpa, marker, properties, null, ctx);
		if(debug)System.err.printf("Vertex at %f, %f\n", deltax, deltay);
	}

	@Override
	public
	void bbox(){

		if(marker != null){
			//TODO is this ok?
			marker.bbox();
			if(debug) System.err.printf("Vertex: marker anchors hor (%f, %f), vert (%f, %f)\n",
					   marker.leftAlign(), marker.rightAlign(), marker.topAlign(), marker.bottomAlign());
			if(marker.leftAlign() >= deltax){
				leftAnchor = marker.leftAlign() - deltax;
				minSize.setWidth(marker.minSize.getWidth());
				rightAnchor = minSize.getWidth() - leftAnchor;
			} else {
				leftAnchor = 0;
				minSize.setWidth(deltax + marker.rightAlign());
				rightAnchor = minSize.getWidth();
			}
			
			if(marker.bottomAlign() >= deltay){
				bottomAnchor = marker.bottomAlign();
				topAnchor = marker.topAlign() + deltay;
				minSize.setHeight(bottomAnchor + topAnchor);
			} else {
				bottomAnchor = 0;
				minSize.setHeight(deltay + marker.topAlign());
				topAnchor = minSize.getHeight();
			}
			
		} else {
			minSize.setWidth(deltax);
			minSize.setHeight(deltay);
			leftAnchor = bottomAnchor = 0;
			rightAnchor = minSize.getWidth();
			topAnchor = minSize.getHeight();
		}
		if(debug)System.err.printf("bbox.vertex: deltax=%f, deltay=%f, width = %f (%f, %f), height= %f (%f, %f))\n", 
							deltax, deltay, minSize.getWidth(), leftAnchor, rightAnchor, minSize.getHeight(), topAnchor, bottomAnchor);
		setNonResizable();
		super.bbox();
	}
	
	@Override
	public
	void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		applyProperties(false);
		if(debug){
			System.err.println("Vertex: marker = " + marker);
			System.err.printf("Vertex: marker at %f, %f\n", left, top);
		}
		if(marker != null){
			marker.bbox();
			marker.draw(left-marker.minSize.getWidth()/2, top-marker.minSize.getHeight()/2);
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
	@Override
	public void layout() {
		size.set(minSize);
		if(marker!=null)marker.layout();
		
	}

}
