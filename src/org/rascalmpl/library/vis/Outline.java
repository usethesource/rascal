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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.properties.PropertyManager;

/**
 * Outline element: a rectangle with colored horizontal lines
 * 
 * @author paulk
 *
 */
public class Outline extends Figure {

	private boolean debug = false;
	private final IList lineInfo;
	private final int maxLine;

	public Outline(IFigureApplet fpa, PropertyManager properties, IList lineInfo, IInteger maxLine) {
		super(fpa, properties);
		this.lineInfo = lineInfo;
		this.maxLine = maxLine.intValue();
	}

	@Override
	public
	void bbox(double desiredWidth, double desiredHeight){
		double lw = getLineWidthProperty();
		width = getWidthProperty();
		height = getHeightProperty();
		width += 2*lw;
		height += 2*lw;
		if(debug) System.err.println("Outline.bbox => " + width + ", " + height);
		if(debug)System.err.printf("Outline.bbox: topAnchor=%f, bottomAnchor=%f\n", topAlign(), bottomAlign());
		
	}
	
	@Override
	public
	void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		
	    double lw = getLineWidthProperty();
		applyProperties();
		if(debug) System.err.println("Outline.draw => " + width + ", " + height);
		if(height > 0 && width > 0){
			fpa.rect(left, top, width, height);
			for(IValue v : lineInfo){
				IConstructor lineDecor = (IConstructor) v;
				int lino = ((IInteger) lineDecor.get(0)).intValue();
				String name = lineDecor.getName();
				
				int color;
				
				if(name.equals("info"))
					color = FigureColorUtils.getErrorColor(0);
				else if(name.equals("warning"))
					color = FigureColorUtils.getErrorColor(1);
				else if(name.equals("error"))
				color = FigureColorUtils.getErrorColor(2);
				else {
					int highlightKind = 0;
					
					if(lineDecor.arity() > 2){
						highlightKind = ((IInteger)lineDecor.get(2)).intValue();
						if(highlightKind < 0)
							highlightKind = 0;
						if(highlightKind >= FigureColorUtils.highlightColors.length)
							highlightKind = FigureColorUtils.highlightColors.length - 1;
					}
					color = FigureColorUtils.getHighlightColor(highlightKind);
				}

				fpa.stroke(color);
				double vpos = top + (lino * height) /maxLine ;
				fpa.line(left + lw, vpos, left + width - lw, vpos);
			}
		}
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY, double centerX, double centerY, boolean mouseInParent){
		if(debug)System.err.println("Outline.MouseOver: " + this);
		if(mouseInside(mouseX, mouseY, centerX, centerY)){
		   fpa.registerMouseOver(this);
		   return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY, boolean mouseInParent){
		if(debug)System.err.println("Outline.MouseOver: " + this);
		if(mouseInside(mouseX, mouseY)){
		   fpa.registerMouseOver(this);
		   return true;
		}
		return false;
	}
}
