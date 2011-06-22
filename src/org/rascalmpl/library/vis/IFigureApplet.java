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

import java.io.OutputStream;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;

public interface IFigureApplet {
	public Composite getComp(); 
	public Color getRgbColor(int rgbColor);
	public Color getColor(int codeColor);
	public void redraw();
	public int getFigureWidth();
	public int getFigureHeight();
	public void registerId(String id, Figure fig);
	public Figure getRegisteredId(String id);
	public void registerFocus(Figure f);
	public boolean isRegisteredAsFocus(Figure f);
	public void unRegisterFocus(Figure f);
	public void keyPressed();
	public void mouseReleased();
	public void mouseMoved();
	public void mouseDragged();
	public void mousePressed();
	public void setComputedValueChanged();
	public void line(double arg0, double arg1, double arg2, double arg3);
	public void rect(double arg0, double arg1, double arg2, double arg3);
	public void ellipse(double arg0, double arg1, double arg2, double arg3);
	public void rectMode(int arg0);
	public void ellipseMode(int arg0);
	public void fill(int arg0);
	public void stroke(int arg0);
	public void strokeWeight(double arg0);
	public void textSize(double arg0);
	public void textColor(int arg0);
	public void textAlign(int arg0, int arg1);
	public void textAlign(int arg0);
	public void textFont(Object arg0);
	public double textWidth(String txt);
	public double textAscent();
	public double textDescent();
	public void text(String arg0, double arg1, double arg2);
	public void pushMatrix();
	public void popMatrix();
	public void rotate(double arg0);
	public void translate(double arg0, double arg1);
	public void scale(double arg0, double arg1);
	public void bezierVertex(double arg0, double arg1, double arg2, double arg3,
			double arg4, double arg5);
	public void vertex(double arg0, double arg1);
	public void curveVertex(double arg0, double arg1);
	public void noFill();
	public void arc(double arg0, double arg1, double arg2, double arg3, double arg4,
			double arg5);
	public void beginShape();
	public void beginShape(int arg0);
	public void endShape();
	public void endShape(int arg0 );
	public void print(Printer printer);
	public void dispose();
	public Object createFont(String fontName, double fontSize);
	public void smooth();
	
	public void setCursor(Cursor cursor);
	public Cursor getCursor();
	public Object getFont();
	public void setBackground(Color color);
	public void setForeground(Color color);	
	public GC getPrinterGC();
	public void stroke(double arg0, double arg1, double arg2);
	public String getName();

	void checkIfIsCallBack(IValue fun,IEvaluatorContext ctx);
	
	public Result<IValue> executeRascalCallBack(IValue callback, Type[] argTypes, IValue[] argVals);
	
	public Result<IValue> executeRascalCallBackWithoutArguments(IValue callback);
	
	public Result<IValue> executeRascalCallBackSingleArgument(IValue callback,Type type, IValue arg);
	
	public void write(OutputStream out, int fileFormat /*SWT IMAGE_BMP, IMAGE_JPEG, IMAGE_ICO*/);
	
	public void setShadow(boolean shadow);
	
	public void setShadowColor(int color);
	
	public void setShadowLeft(double x);
	
	public void setShadowTop(double y);
	
	
}
