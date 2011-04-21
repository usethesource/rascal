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

package org.rascalmpl.library.vis;

public interface IFigureApplet {
	public Object getComp(); // get Composite. Needed by swt variant
	public void init();
	public void setup();
	public void draw();
	public int getFigureWidth();
	public int getFigureHeight();
	public void incDepth();
	public void decDepth();
	public boolean isVisible(int d);
	public void registerId(String id, Figure fig);
	public Figure getRegisteredId(String id);
	public void registerFocus(Figure f);
	public boolean isRegisteredAsFocus(Figure f);
	public void unRegisterFocus(Figure f);
	public void registerMouseOver(Figure f);
	public boolean isRegisteredAsMouseOver(Figure f);
	public void unRegisterMouseOver(Figure f);
	public void keyPressed();
	public void mouseReleased();
	public void mouseMoved();
	public void mouseDragged();
	public void mousePressed();
	public void setComputedValueChanged();
	public void line(float arg0, float arg1, float arg2, float arg3);
	public void rect(float arg0, float arg1, float arg2, float arg3);
	public void ellipse(float arg0, float arg1, float arg2, float arg3);
	public void rectMode(int arg0);
	public void ellipseMode(int arg0);
	public void fill(int arg0);
	public void stroke(int arg0);
	public void strokeWeight(float arg0);
	public void textSize(float arg0);
	public void textColor(int arg0);
	public void textAlign(int arg0, int arg1);
	public void textAlign(int arg0);
	public void textFont(Object arg0);
	public float textWidth(String txt);
	public float textAscent();
	public float textDescent();
	public void text(String arg0, float arg1, float arg2);
	public void pushMatrix();
	public void popMatrix();
	public void rotate(float arg0);
	public void translate(float arg0, float arg1);
	public void scale(float arg0, float arg1);
	public void bezierVertex(float arg0, float arg1, float arg2, float arg3,
			float arg4, float arg5);
	public void vertex(float arg0, float arg1);
	public void curveVertex(float arg0, float arg1);
	public void noFill();
	public void arc(float arg0, float arg1, float arg2, float arg3, float arg4,
			float arg5);
	public void beginShape();
	public void beginShape(int arg0);
	public void endShape();
	public void endShape(int arg0 );
	public void print();
	// From PApplet 
	public Object createFont(String fontName, float fontSize);
	public void smooth();
	
	// From awt  
	public void setCursor(Object cursor);
	public void add(Object comp);
	public void remove(Object comp);
	public Object getFont();
	public void setBackground(Object color);
	public void setForeground(Object color);	
	public void invalidate();
	public void validate();
	public void stroke(float arg0, float arg1, float arg2);
	public String getName();
	// Needed by SpringGraph
}
