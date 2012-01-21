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

import static org.rascalmpl.library.vis.properties.Properties.FONT_BASELINE;
import static org.rascalmpl.library.vis.properties.Properties.INNER_ALIGN;
import static org.rascalmpl.library.vis.properties.Properties.TEXT_ANGLE;

import java.util.List;

import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.PropertyValue;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.MaxFontAscent;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.BoundingBox;
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
	private MaxFontAscent mf;


	public Text(PropertyManager properties,PropertyValue<String> txt) {
		super( properties);
		this.txt = txt;
		children = childless;
		minSizeUnrotated = new BoundingBox();
		mf = null;
	}

	@Override
	void registerMore(IFigureConstructionEnv env, NameResolver resolver) {
		String fb = prop.getStr(FONT_BASELINE);
		if(!fb.equals("")) {
			mf = resolver.resolveMaxFontAscent(fb);
			if(mf == null){
				mf = new MaxFontAscent();
				resolver.register(fb,mf);
			}
			mf.updateToClock(env.getCallBackEnv().getComputeClock());
			mf.set(getTextAscent(),getTextDescent());
		}
	}
	
	@Override
	public void computeMinSize() {
		double textHeight = getLineHeight();
		
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
		double height = lines.length * textHeight;
		minSize.set(width,height);
			double angle = FigureMath.radians(prop.getReal(TEXT_ANGLE));
			minSizeUnrotated.set(minSize);
			double r1 = Math.abs(Math.cos(angle));
			double r2 = Math.abs(Math.sin(angle));
			minSize.setX(minSizeUnrotated.getX() * r1 + minSizeUnrotated.getY() * r2);
			minSize.setY(minSizeUnrotated.getY() * r1 + minSizeUnrotated.getX() * r2);
		resizable.set(false,false);
	}

	public double getLineHeight() {
		double textHeight;
		if(mf == null){
			textHeight = getTextHeight();
		} else {
			textHeight = mf.getFontAscent() + mf.getFontDescent();
			System.out.printf("Textheight %f\n",textHeight);
		}
		return textHeight;
	}

	@Override
	public void resizeElement(Rectangle view) {}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		double ascentOffset = 0;
		if (mf!=null) {
			ascentOffset = mf.getFontAscent() - getTextAscent();
			System.out.println("ascentOffset " + ascentOffset);
		}
		double lineHeight = getLineHeight();
		//System.out.printf("Drawing %s\n",this);
		double y = -minSizeUnrotated.getY()/2.0;
		double tx =  globalLocation.getX() + minSize.getX()/2.0;
		double ty =  globalLocation.getY() + minSize.getY()/2.0;;
		double lux = -minSizeUnrotated.getX()/2.0;
		double luy = -minSizeUnrotated.getY()/2.0;
		gc.translate( tx,  ty);
		gc.rotate(prop.getReal(TEXT_ANGLE));
		
		for(int i = 0 ; i < lines.length ; i++){
			
			gc.text(lines[i], lux + indents[i],y + ascentOffset);
			y+= getTextHeight() ;
			
		}
		gc.rotate(-prop.getReal(TEXT_ANGLE));
		gc.translate( - tx, - ty);
		
	}
	
	
	
	@Override
	public
	String toString(){
		return String.format("text %s %s %s", txt.getValue(), localLocation, minSize);
	}

}
