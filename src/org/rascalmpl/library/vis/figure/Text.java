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
package org.rascalmpl.library.vis.figure;

import static org.rascalmpl.library.vis.properties.Properties.*;
import static org.rascalmpl.library.vis.util.vector.Dimension.*;
import java.util.List;

import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.PropertyValue;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.vector.BoundingBox;
import org.rascalmpl.library.vis.util.vector.Coordinate;
import org.rascalmpl.library.vis.util.vector.Rectangle;

/**
 * Text element.
 * 
 * @author paulk
 *
 */
public class Text extends Figure {
	private static final int TAB_WIDTH = 4;
	private static boolean debug = false;
	private String [] lines;
	private double[] indents;
	private PropertyValue<String> txt;
	private BoundingBox minSizeUnrotated;


	public Text(PropertyManager properties,PropertyValue<String> txt) {
		super( properties);
		this.txt = txt;
		children = childless;
		minSizeUnrotated = new BoundingBox();
	}

	@Override
	public void computeMinSize() {
		lines = txt.getValue().split("\n");
		indents = new double[lines.length];
		double width = 0;
		for(int i = 0 ; i < lines.length ; i++){
			//lines[i] = Util.tabs2spaces(TAB_WIDTH,lines[i]);
			indents[i] = getTextWidth(lines[i]);
			width = Math.max(width,indents[i]);
		}
		double innerAlign = prop.getReal(INNER_ALIGN);
		for(int i = 0 ; i < indents.length ; i++){
			indents[i] = (width - indents[i]) * innerAlign;
		}
		double height = lines.length * getTextHeight();
		minSize.set(width,height);
			double angle = FigureMath.radians(prop.getReal(TEXT_ANGLE));
			minSizeUnrotated.set(minSize);
			double r1 = Math.abs(Math.cos(angle));
			double r2 = Math.abs(Math.sin(angle));
			minSize.setX(minSizeUnrotated.getX() * r1 + minSizeUnrotated.getY() * r2);
			minSize.setY(minSizeUnrotated.getY() * r1 + minSizeUnrotated.getX() * r2);
		resizable.set(false,false);
	}

	@Override
	public void resizeElement(Rectangle view) {}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		//System.out.printf("Drawing %s\n",this);
		double y = -minSizeUnrotated.getY()/2.0;
		double tx =  location.getX() + minSize.getX()/2.0;
		double ty =  location.getY() + minSize.getY()/2.0;;
		double lux = -minSizeUnrotated.getX()/2.0;
		double luy = -minSizeUnrotated.getY()/2.0;
		gc.translate( tx,  ty);
		gc.rotate(prop.getReal(TEXT_ANGLE));
		
		for(int i = 0 ; i < lines.length ; i++){
			
			gc.text(lines[i], lux + indents[i],y);
			y+= getTextHeight();
			
		}
		gc.rotate(-prop.getReal(TEXT_ANGLE));
		gc.translate( - tx, - ty);
		
	}
	
	@Override
	public
	String toString(){
		return String.format("text %s %s %s", txt.getValue(), location, minSize);
	}
}
