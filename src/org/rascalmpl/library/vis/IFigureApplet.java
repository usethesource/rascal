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
import org.rascalmpl.library.vis.graphics.FontStyle;

public interface IFigureApplet {
	
	public int getFigureWidth();
	public int getFigureHeight();
	public void registerFocus(Figure f);
	public boolean isRegisteredAsFocus(Figure f);
	public void unRegisterFocus(Figure f);
	public void keyPressed();
	public void mouseReleased();
	public void mouseMoved();
	public void mouseDragged();
	public void mousePressed();
	public void setComputedValueChanged();
	
	public void dispose();

	
	public void setCursor(Cursor cursor);
	public Cursor getCursor();
	public Object getFont();

	public GC getPrinterGC();
	public String getName();

	public void write(OutputStream out, int fileFormat /*SWT IMAGE_BMP, IMAGE_JPEG, IMAGE_ICO*/);
	public Color getRgbColor(int fillColorProperty);
	public Color getColor(int colorRed);
	

	
	
}
