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



import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.compose.Compose;
import org.rascalmpl.library.vis.properties.PropertyManager;

import processing.core.PConstants;


/**
 * Arbitrary shape built from Vertices.
 * 
 * Relevant properties:
 * connected:	connect vertices with lines
 * closed:		make a closed shape
 * curved:		connect vertices with a spline
 * 
 * @author paulk
 *
 */
public class Shape extends Compose {
	static boolean debug = false;
	float minX, minY;
	float[] anchorPointsX, anchorPointsY;
	Shape(IFigureApplet fpa, PropertyManager properties, IList elems,  IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties, elems, childProps, ctx);
		anchorPointsX = new float[figures.length];
		anchorPointsY = new float[figures.length];
	}
	
	@Override
	public
	void bbox(float desiredWidth, float desiredHeight){
		minX = minY = Float.MAX_VALUE;
		for(int i = 0 ; i < figures.length ; i++){
			Vertex ver = (Vertex)figures[i];
			ver.bbox(AUTO_SIZE, AUTO_SIZE);
			anchorPointsX[i] = ver.getDeltaX();
			xPos[i] = anchorPointsX[i] - ver.leftAlign();
			anchorPointsY[i] = -ver.getDeltaY();
			yPos[i] = anchorPointsY[i]-  ver.topAlign();
			minX = min(minX,xPos[i]);
			minY = min(minY,yPos[i]);
		}
		for(int i = 0 ; i < figures.length ; i++){
			xPos[i]-= minX;
			anchorPointsX[i]-=minX;
			width = max(width,anchorPointsX[i] + figures[i].rightAlign());
			yPos[i]-= minY;
			anchorPointsY[i]-=minY;
			height = max(height,anchorPointsY[i] + figures[i].bottomAlign());
		}
		if(debug)System.err.printf("bbox.shape: width = %f , height = %f \n", 
				width, height);
	}
	
	@Override
	public
	void draw(float left, float top){
		
		this.setLeft(left);
		this.setTop(top);
		
		applyProperties();

		boolean closed = getClosedProperty();
		boolean curved = getCurvedProperty();
		boolean connected = closed ||  getConnectedProperty() || curved;
		
		if(connected){
			fpa.noFill();
			fpa.beginShape();
		}
		
		/*
		 * We present to the user a coordinate system
		 * with origin at the bottom left corner!
		 * Therefore we subtract deltay from bottom
		 */
		int next = 0;
		if(connected && closed){
			// Add a vertex at origin
			fpa.vertex(left + minX, top + minY);
			fpa.vertex(anchorPointsX[next], anchorPointsY[next]);
		}
		if(connected && curved)
			fpa.curveVertex(anchorPointsX[next], anchorPointsY[next]);
		
		for(int i = 0 ; i < figures.length ; i++){
			applyProperties();
			if(connected){
				if(!closed)
						fpa.noFill();
				if(curved)
					fpa.curveVertex(anchorPointsX[i], anchorPointsY[i]);
				else
					fpa.vertex(anchorPointsX[i], anchorPointsY[i]);
			}
		}
		if(connected){
			if(curved){
				fpa.curveVertex(anchorPointsX[figures.length - 1], anchorPointsY[figures.length - 1]);
			}
			if(closed){
				fpa.vertex(anchorPointsX[figures.length - 1], anchorPointsY[figures.length - 1]);
				fpa.endShape(PConstants.CLOSE);
			} else 
				fpa.endShape();
		}
		
		for(int i = 0 ; i < figures.length ; i++){
			figures[i].draw(left + xPos[i], top + yPos[i]);
		}
	}
	
	public Extremes getExtremesForAxis(String axisId, float offset, boolean horizontal){
		Extremes result = super.getExtremesForAxis(axisId, offset, horizontal);
		if(result.gotData()){
			return result;
		} else {
		
			Extremes[] extremesList = new Extremes[figures.length];
			for(int i = 0 ; i < figures.length ; i++ ){
				Vertex fig = (Vertex) figures[i];
				float offsetHere = offset;
				if(horizontal){
					if(fig.getDeltaXMeasure().axisName.equals(axisId)){
						offsetHere+=fig.getDeltaXMeasure().value;
					}
				} else {
					if(fig.getDeltaYMeasure().axisName.equals(axisId)){
						offsetHere+=fig.getDeltaYMeasure().value;
					}
				}
				extremesList[i] = figures[i].getExtremesForAxis(axisId, offsetHere, horizontal);
				if(!extremesList[i].gotData()){
					extremesList[i] = new Extremes(offsetHere);
				}
			}
			return Extremes.merge(extremesList);
		}
	}
	

	public float getOffsetForAxis(String axisId, float offset, boolean horizontal){
		float result = super.getOffsetForAxis(axisId, offset, horizontal);
		if(result != Float.MAX_VALUE){
			return result;
		} else {
			for(int i = 0 ; i < figures.length ; i++ ){
				Vertex fig = (Vertex) figures[i];
				float offsetHere ;
				if(horizontal){
					if(fig.getDeltaXMeasure().axisName.equals(axisId)){
						offsetHere= offset + anchorPointsX[i];
					} else {
						offsetHere = figures[i].getOffsetForAxis(axisId, offset + xPos[i], horizontal);
					}
				} else {
					if(fig.getDeltaYMeasure().axisName.equals(axisId)){
						offsetHere=offset + anchorPointsY[i];
					} else {
						offsetHere = figures[i].getOffsetForAxis(axisId, offset + yPos[i], horizontal);
					}
				}
				System.out.printf("got %f %f\n", offset, result);
				result = min(result,offsetHere );
			}
			return result;
		}
	}
}
