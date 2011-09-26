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
package org.rascalmpl.library.vis.figure.tree;

import java.util.ArrayList;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.containers.Box;
import org.rascalmpl.library.vis.figure.compose.Compose;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import static org.rascalmpl.library.vis.properties.Properties.AREA;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.Rectangle;


/**
 * Tree map layout. Given a tree consisting of a list of nodes and edges, place them in a space conserving layout.
 * 
 * Based on Mark Bruls; Kees Huizing and Jarke J. vanWijk. "Squarified Treemaps"
 * 
 * @author paulk
 *
 */

public class TreeMap extends Compose{


	double area;
	Figure[] areas;
	int curChild;

	public TreeMap(Figure[] figures, PropertyManager properties) {
		super(figures, properties);
		this.areas = figures;
	}

	

	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen, boolean visible, NameResolver resolver){
		area = 0;
		for(Figure fig : children){
			area += fig.prop.getReal(AREA);
		}
	}
	
	private double worstAspectRatio(ArrayList<Figure> figs,double cumulatedArea){
		double height = (cumulatedArea / area) * size.getY();
		double result = 1.0;
		for(Figure fig : figs){
			double width = (fig.prop.getReal(AREA) / cumulatedArea) * size.getX();
			double widthDivHeight = width / height;
			result = Math.max(result, Math.max(widthDivHeight, 1.0/widthDivHeight));
		}
		return result;
	}
	

	private double layoutRow(double yOffset,double cumulatedArea,ArrayList<Figure> figs){
		double height = (cumulatedArea / area) * size.getY();
		double x = 0;
		for(Figure fig : figs){
			fig.location.setY(yOffset);
			fig.location.setX(x);
			fig.size.setY(height);
			double width = (fig.prop.getReal(AREA) / cumulatedArea)* size.getX();
			fig.size.setX( width);
			if(!fig.size.contains(fig.minSize)){
				children[curChild] = new Box(null, areas[curChild].prop);
			} else {
				children[curChild] = areas[curChild];
			}
			curChild++;
			System.out.printf("Setting %s  area %s %s %s\n",fig,fig.prop.getReal(AREA),area,cumulatedArea);
			x+=width;
		}
		return yOffset + height;
	}
	

	@Override
	public void resizeElement(Rectangle view) {
		curChild = 0;
		ArrayList<Figure> currentRow = new ArrayList<Figure>();
		double prevAR = Double.MAX_VALUE;
		double yOffset = 0;
		double cumulatedArea = 0;
		for(Figure cur : children ){
			
			cumulatedArea += cur.prop.getReal(AREA);
			System.out.printf("Cum area %f\n",cumulatedArea);
			currentRow.add(cur);
			double curAR = worstAspectRatio(currentRow,cumulatedArea);
			if(curAR > prevAR){
				System.out.printf("Getting wordse area %f\n",cumulatedArea);
				currentRow.remove(currentRow.size()-1);
				cumulatedArea -= cur.prop.getReal(AREA);
				yOffset = layoutRow(yOffset,cumulatedArea,currentRow);
				currentRow.clear();
				currentRow.add(cur);
				cumulatedArea = cur.prop.getReal(AREA);
				prevAR = Double.MAX_VALUE;
			} else {
				prevAR = curAR;
			}
			
		}
		layoutRow(yOffset,cumulatedArea,currentRow);
	}

	@Override
	public void computeMinSize() {
		minSize.set(10, 10);
		
	}
	
}
