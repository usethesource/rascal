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

import org.rascalmpl.library.vis.properties.IPropertyValue;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.FigureApplet;

/**
 * Text element.
 * 
 * @author paulk
 *
 */
public class Text extends Figure {
	private static boolean debug = false;
	private double topAnchor = 0;
	private double bottomAnchor = 0;
	private double leftAnchor;
	private double rightAnchor;
	private double hfill = 0;
	private double vfill = 0;
	private IPropertyValue<String> txt;
	private int textAlignH = FigureApplet.CENTER;	

	public Text(IFigureApplet fpa, PropertyManager properties,IPropertyValue<String> txt) {
		super(fpa, properties);
		this.txt = txt;
		if(debug)System.err.printf("Text: %s\n", txt.getValue());
	}
	
	@Override
	public
	void bbox(double desiredWidth, double desiredHeight){
		double halign = getHAlignProperty();
		textAlignH = (halign < 0.5f) ? FigureApplet.LEFT : (halign > 0.5f) ? FigureApplet.RIGHT : FigureApplet.CENTER;
		applyFontProperties();
		topAnchor = fpa.textAscent() ;
		bottomAnchor = fpa.textDescent();
		
		String [] lines = txt.getValue().split("\n");
		int nlines = lines.length;
		width = 0;
		for(int i = 0; i < nlines; i++)
			width = Math.max(width, fpa.textWidth(lines[i]));
		
		if(nlines > 1){
			height = nlines * (topAnchor + bottomAnchor) + bottomAnchor;
			topAnchor = bottomAnchor = getVAlignProperty() * height;
		} else {
			height = topAnchor + bottomAnchor;
		}
		hfill = textAlignH == FigureApplet.LEFT ? 0 : textAlignH == FigureApplet.RIGHT ? width : width/2;
		/*
		if(debug){
			System.err.printf("text.bbox: font=%s, ascent=%f, descent=%f\n", fpa.getFont(), fpa.textAscent(), fpa.textDescent() );
			System.err.printf("text.bbox: txt=\"%s\", width=%f, height=%f angle =%f\n", txt, width, height, getTextAngleProperty());
		}
		*/
		if(getTextAngleProperty() != 0){
			double angle = FigureApplet.radians(getTextAngleProperty());
			double sina = FigureApplet.sin(angle);
			double cosa = FigureApplet.cos(angle);
			double h1 = Math.abs(width * sina);
			double w1 = Math.abs(width * cosa);
			double h2 = Math.abs(height *  cosa);
			double w2 = Math.abs(height *  sina);
			
			width = w1 + w2;
			height = h1 + h2;
			
			leftAnchor = w1/width;
			rightAnchor = w2/width;
			topAnchor = h1/height;
			bottomAnchor = h2/height;
			
			hfill = width/2;
			if(nlines > 1){
				vfill = textAlignH == FigureApplet.LEFT ? height : textAlignH == FigureApplet.RIGHT ? 0 : height/2;
			} else {
				vfill = height/2;
			}
			
			if(debug)System.err.printf("bbox text: height=%f, width=%f, h1=%f h2=%f w1=%f w2=%f\n", height, width, h1, h2, w1, w2);
		}
	}
	
	@Override
	public
	void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		
		applyProperties();
		applyFontProperties();
	
		if(debug)System.err.printf("text.draw: %s, font=%s, left=%f, top=%f, width=%f, height=%f\n", txt, fpa.getFont(), left, top, width, height);
		if(height > 0 && width > 0){
			double angle = getTextAngleProperty();

			fpa.textAlign(textAlignH,FigureApplet.CENTER);
			if(angle != 0){
				fpa.pushMatrix();
				fpa.translate(left + hfill, top + vfill);
				fpa.rotate((FigureApplet.radians(angle)));
				fpa.text(txt.getValue(), 0, 0);
				fpa.popMatrix();
			} else {
				fpa.text(txt.getValue(), left + hfill, top + height/2);
//				vlp.rectMode(FigureApplet.CORNERS);
//				vlp.text(txt, left, top, left+width, top+height);
			}
		}
	}
	
//	@Override
//	public double leftAnchor(){
//		double res= leftAnchor;
//		System.err.println(this + ".leftAnchor = " + res);
//		return res;
//	}
//	
//	@Override
//	public double rightAnchor(){
//		double res = rightAnchor;
//		System.err.println(this + ".rightAnchor = " + res);
//		return res;
//	}
	
	@Override
	public double topAlign(){
		return topAnchor;
	}
	
	@Override
	public double bottomAlign(){
		return bottomAnchor;
	}
	
	@Override
	public
	String toString(){
		return new StringBuffer("text").append("(").append("\"").append(txt.getValue()).append("\",").
		append(getLeft()).append(",").
		append(getTop()).append(",").
		append(width).append(",").
		append(height).append(")").toString();
	}
}
