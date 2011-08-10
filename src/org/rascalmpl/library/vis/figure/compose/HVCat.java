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
package org.rascalmpl.library.vis.figure.compose;

import java.util.Vector;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.FigureSWTApplet;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.Rectangle;
import org.rascalmpl.library.vis.util.Util;


/**
 * HVCat elements on consecutive rows. Width is determined by the width property, height is
 * determined by the number and size of the elements. This is similar to aligning words in
 * a text but is opposed to composition in a grid, where the elements are placed on fixed
 * grid positions.
 * 
 * @author paulk
 *
 */
// A HVCat is always wrapped in an WithDependantWidthHeight figure
public class HVCat extends Compose {
	static boolean debug = false;
	boolean flip;
	Vector<Row> rows;
	double[] curRowBorders;
	
	static class Row{
		double yStart;
		int startIndex, endIndex;
		Row(double yStart, int startIndex, int endIndex){
			this.yStart = yStart;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}
	}
	
	public HVCat(boolean flip,Figure[] figures, PropertyManager properties) {
		super(figures, properties);
		this.flip = flip;
		rows = new Vector<HVCat.Row>();
	}
	
	@Override
	public void bbox(){
		super.bbox();
		double minWidth = 0;
		for(Figure fig : figures){
			minWidth = Math.max(minWidth,fig.minSize.getWidth(flip) / fig.getHGrowProperty(flip));
		}
		minSize.setWidth(flip, minWidth + FigureSWTApplet.scrollbarSize.getWidth(flip));
		minSize.setHeight(flip, FigureSWTApplet.scrollableMinSize.getHeight(flip));
	}
	
	@Override
	public void layout(){
		double x = 0,y = 0;
		int i = 0;
		while(i < figures.length){
			double figureWidth = 0;
			int lowestFigIndex = i;
			boolean firstOnRow = true;
			do{
				x += figureWidth;
				if(firstOnRow){ firstOnRow = false;}
				else { x += getHGapProperty(flip); }
				pos[i].setX(flip,x);
				i++;
				if(i == figures.length) break;
				figures[i].takeDesiredWidth(flip, figures[i].getHShrinkProperty(flip) * size.getWidth(flip));
				figures[i].takeDesiredHeight(flip, figures[i].minSize.getHeight(flip));
				figures[i].layout();
				figureWidth = figures[i].size.getWidth(flip);
			} while(x + getHGapProperty(flip) + figureWidth <= size.getWidth(flip));
			double rowHeight = getMaxHeight(lowestFigIndex,i);
			layoutRow(y, x, rowHeight, lowestFigIndex, i);
			rows.add(new Row(y,lowestFigIndex,i));
			y+=rowHeight + getVGapProperty(flip);
			x = 0;
		}
		size.setHeight(flip, y - getVGapProperty(flip));
		curRowBorders = getCurRowBorders();
	}
	
	public double[] getCurRowBorders(){
		double[] result;
		if(curRowBorders != null && rows.size() == curRowBorders.length){
			result = curRowBorders;
		} else {
			result = new double[rows.size()];
		}
		for(int i = 0 ; i < rows.size() ; i++){
			result[i] = rows.get(i).yStart;
		}
		return result;
	}
	
	public double getMaxHeight(int lowestFigIndex, int highestFigIndex){
		double result = 0;
		for(int i = lowestFigIndex ; i < highestFigIndex; i++){
			result = Math.max(result,figures[i].size.getHeight(flip));
		}
		return result;
	}
	
	public void layoutRow(double yStart,double width,double height,int lowestFigIndex, int highestFigIndex){
		double innerAlign = 0;
		double xOffset = size.getWidth(flip) - width * innerAlign;
		for(int i = lowestFigIndex ; i < highestFigIndex; i++){
			pos[i].addX(flip, xOffset);
			pos[i].setY(flip, yStart + (height - figures[i].size.getHeight(flip)) * figures[i].getVAlignProperty(flip));
		}
	}
	
	// Optimized versions of drawPart and getFigures under mouse!
	@Override
	public
	void drawPart(Rectangle r,GraphicsContext gc){
		applyProperties(gc);
		int startRow = Math.max(0,Util.binaryIntervalSearch(curRowBorders, r.getY(flip) - globalLocation.getY(flip)));
		int endRow = Math.max(0,Util.binaryIntervalSearch(curRowBorders, r.getYDown(flip) - globalLocation.getY(flip)));
		for(int row = startRow; row <= endRow; row++){
			for(int i = rows.get(row).startIndex ; i < rows.get(row).endIndex ; i++){
				if(r.contains(figures[i].globalLocation, figures[i].size)){
					figures[i].draw(gc);
				} else if(figures[i].overlapsWith(r)){
					figures[i].drawPart(r,gc);
				}
			}
		}
	}
	
	
	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		if(!mouseInside(c.getX(), c.getY())) return false;
		int row = Math.max(0,Util.binaryIntervalSearch(curRowBorders, c.getY(flip) - globalLocation.getY(flip)));
		for(int i = rows.get(row).startIndex ; i < rows.get(row).endIndex ; i++){
			if(figures[i].getFiguresUnderMouse(c, result)){
				break;
			}
		}
		result.add(this);
		return true;
	}
	
	

}
