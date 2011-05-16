/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.compose;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.containers.HScreen;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Coordinate;

/**
 * Abstract class for the composition of a list of visual elements.
 * 
 * @author paulk
 * 
 */
public abstract class Compose extends Figure {

	final protected Figure[] figures;
	protected double[] xPos;
	protected double[] yPos;
	final private static boolean debug = false;

	protected Compose(IFigureApplet fpa, PropertyManager properties,
			IList elems, IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties);
		int n = elems.length();
		figures = new Figure[n];
		xPos = new double[figures.length];
		yPos = new double[figures.length];
		for (int i = 0; i < n; i++) {
			IValue v = elems.get(i);
			IConstructor c = (IConstructor) v;
			if (debug)
				System.err.println("Compose, elem = " + c.getName());
			figures[i] = FigureFactory.make(fpa, c, properties, childProps, ctx);
		}
	}

	@Override
	public boolean keyPressed(int key, int keyCode) {
		for (int i = figures.length - 1; i >= 0; i--)
			if (figures[i].keyPressed(key, keyCode))
				return true;
		return super.keyPressed(key, keyCode);
	}
	
	@Override public void destroy(){
		for (int i = figures.length - 1; i >= 0; i--)
			figures[i].destroy();
	}
		
	
	@Override
	public
	void draw(double left, double top){
		setLeft(left);
		setTop(top);
		applyProperties();
		for(int i = 0; i < figures.length; i++){
			figures[i].draw(left + xPos[i], top + yPos[i]);
		}
	}
	
	public void propagateScaling(double scaleX,double scaleY, HashMap<String,Double> axisScales){
		super.propagateScaling(scaleX, scaleY, axisScales);
		for(Figure fig : figures){
			fig.propagateScaling(scaleX, scaleY, axisScales);
		}
	}
	
	public void gatherProjections(double left, double top, Vector<HScreen.ProjectionPlacement> projections, boolean first, String screenId, boolean horizontal){
		for(int i = 0 ; i < figures.length ; i++){
			figures[i].gatherProjections(left + xPos[i], top + yPos[i], projections, first, screenId, horizontal);
		}
	}
	

	public Extremes getExtremesForAxis(String axisId, double offset, boolean horizontal){
		Extremes result = super.getExtremesForAxis(axisId, offset, horizontal);
		if(result.gotData()){
			return result;
		} else {
			Extremes[] extremesList = new Extremes[figures.length];
			for(int i = 0 ; i < figures.length ; i++){
				extremesList[i] = figures[i].getExtremesForAxis(axisId, offset, horizontal);
			}
			return Extremes.merge(extremesList);
		}
	}
	

	public double getOffsetForAxis(String axisId, double offset, boolean horizontal){
		double result = super.getOffsetForAxis(axisId, offset, horizontal);
		if(result != Double.MAX_VALUE){
			return result;
		} else {
			for(int i = 0 ; i < figures.length ; i++){
				double off = 0.0f;
				if(horizontal){
					off = xPos[i];
				} else {
					off = yPos[i];
				}
				//System.out.printf("offset %f off %f %s\n",offset, off,this);
				result = min(result,figures[i].getOffsetForAxis(axisId, off+offset, horizontal));
			}
			return result;
		}
	}
	
	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		if(!mouseInside(c.getX(), c.getY())) return false;
		for(int i = figures.length-1 ; i >= 0 ; i--){
			if(figures[i].getFiguresUnderMouse(c, result)){
				break;
			}
		}
		result.add(this);
		return true;
	}
}
