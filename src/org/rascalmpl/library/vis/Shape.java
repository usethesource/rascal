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

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.compose.Compose;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.descriptions.DimensionalProp;



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
	double zeroX,zeroY;
	double[] anchorPointsX, anchorPointsY;
	Shape(IFigureApplet fpa, PropertyManager properties, IList elems,  IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties, elems, childProps, ctx);
		anchorPointsX = new double[figures.length];
		anchorPointsY = new double[figures.length];
	}
	
	@Override
	public
	void bbox(double desiredWidth, double desiredHeight){
		double minX = 0.0;		
		double maxX = 0.0;
		double minY = 0.0;
		double maxY = 0.0;

		for(int i = 0 ; i < figures.length ; i++){
			Vertex ver = (Vertex)figures[i];
			ver.bbox(AUTO_SIZE, AUTO_SIZE);
			anchorPointsX[i] = ver.getDeltaX();
			xPos[i] = anchorPointsX[i] - ver.leftAlign();
			anchorPointsY[i] = - ver.getDeltaY();
			yPos[i] = anchorPointsY[i]-  ver.topAlign();
			minY = min(minY, -ver.getDeltaY() - ver.topAlign());
			maxY = max(maxY,-ver.getDeltaY() + ver.bottomAlign());
			minX = min(minX, ver.getDeltaX() - ver.leftAlign());
			maxX = max(maxX,ver.getDeltaX() + ver.rightAlign());
		}
		zeroX = -minX;
		zeroY = -minY;
		height = maxY - minY;
		width = maxX - minX;
		for(int i = 0 ; i < figures.length ; i++){
			anchorPointsX[i]+=zeroX;
			xPos[i]+=zeroX;
			anchorPointsY[i]+=zeroY;
			yPos[i]+=zeroY;
		}
		if(debug)System.err.printf("bbox.shape: width = %f , height = %f \n", 
				width, height);
	}
	
	@Override
	public
	void draw(double left, double top){
		
		this.setLeft(left);
		this.setTop(top);
		
		applyProperties();

		boolean closed = getClosedProperty();
		boolean curved = getCurvedProperty();
		boolean connected = closed ||  getConnectedProperty() || curved;
		
		if(connected){
			//fpa.noFill();
			fpa.beginShape();
		}
		
		/*
		 * We present to the user a coordinate system
		 * with origin at the bottom left corner!
		 * Therefore we subtract deltay from bottom
		 */
		int next = 0;
		if(closed && connected){
			// Add a vertex at origin
			fpa.vertex(left +zeroX, top + zeroY);
			fpa.vertex(left + anchorPointsX[next], top + anchorPointsY[next]);
		}
		if(connected && curved)
			fpa.curveVertex(left + anchorPointsX[next], top + anchorPointsY[next]);
		
		if(connected){
			for(int i = 0 ; i < figures.length ; i++){
				applyProperties();
					if(!closed)
							fpa.noFill();
					if(curved)
						fpa.curveVertex(left + anchorPointsX[i], top +  anchorPointsY[i]);
					else
						fpa.vertex(left + anchorPointsX[i], top + anchorPointsY[i]);
			}
		}
		if(connected){
			if(curved){
				fpa.curveVertex(left + anchorPointsX[figures.length - 1], top + anchorPointsY[figures.length - 1]);
			}
			if(closed){
				fpa.vertex(left + anchorPointsX[figures.length - 1], top+ zeroY);
				fpa.endShape(FigureApplet.CLOSE);
			} else 
				fpa.endShape();
		}
		
		for(int i = 0 ; i < figures.length ; i++){
			figures[i].draw(left + xPos[i], top + yPos[i]);
		}
	}
	
	public Extremes getExtremesForAxis(String axisId, double offset, boolean horizontal){
		if(horizontal && getMeasureProperty(DimensionalProp.WIDTH).axisName.equals(axisId)){
			double val = getMeasureProperty(DimensionalProp.WIDTH).value;
			return new Extremes(offset - getHAlignProperty() * val, offset + (1-getHAlignProperty()) * val);
		} else if( !horizontal && getMeasureProperty(DimensionalProp.HEIGHT).axisName.equals(axisId)){
			double val = getMeasureProperty(DimensionalProp.HEIGHT).value;
			return new Extremes(offset - getVAlignProperty() * val, offset + (1-getVAlignProperty()) * val);
		} else {
		
			Extremes[] extremesList = new Extremes[figures.length + 1];
			for(int i = 0 ; i < figures.length ; i++ ){
				Vertex fig = (Vertex) figures[i];
				double offsetHere = offset;
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
				System.out.printf("Shape %s extreme %d: %f %f\n", this, i, extremesList[i].getMinimum(), extremesList[i].getMaximum());
				if(!extremesList[i].gotData()){
					extremesList[i] = new Extremes(offset,offsetHere);
				}
			}
			extremesList[figures.length] = new Extremes(offset);
			return Extremes.merge(extremesList);
		}
	}
	

	public void propagateScaling(double scaleX,double scaleY, HashMap<String,Double> axisScales){
		super.propagateScaling(scaleX, scaleY, axisScales);
		for(Figure fig : figures){
			fig.propagateScaling(scaleX, scaleY, axisScales);
		}
	}
	

	public double getOffsetForAxis(String axisId, double offset, boolean horizontal){
		double result = super.getOffsetForAxis(axisId, offset, horizontal);
		if(result != Double.MAX_VALUE){
			return result;
		} else {
			for(int i = 0 ; i < figures.length ; i++ ){
				Vertex fig = (Vertex) figures[i];
				double offsetHere ;
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
				result = min(result,offsetHere );
			}
			return result;
		}
	}
}
