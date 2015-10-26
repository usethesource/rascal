/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure;

import static org.rascalmpl.library.vis.properties.Properties.LINE_WIDTH;

import java.util.List;

import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.FigureColorUtils;
import org.rascalmpl.library.vis.util.vector.Rectangle;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IValue;

/**
 * Outline element: a rectangle with colored horizontal lines
 * 
 * @author paulk
 *
// */
public class Outline extends Figure {

	private final IList lineInfo;
	private final int maxLine;

	public Outline(PropertyManager properties, IList lineInfo, int maxLine) {
		super(properties);
		this.lineInfo = lineInfo;
		this.maxLine = maxLine;
		children = childless;
	}

	@Override
	public
	void computeMinSize(){
		resizable.set(false,false);
	}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		
	    double lw = prop.getReal(LINE_WIDTH);
			gc.rect(globalLocation.getX(), globalLocation.getY(), size.getX(), size.getY());
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

				gc.stroke(color);
				double vpos = globalLocation.getY() + (lino * size.getY()) /maxLine ;
				gc.line(globalLocation.getX() + + lw, vpos, globalLocation.getX() + size.getX() - lw, vpos);
			}
	}

	@Override
	public void resizeElement(Rectangle view) {}
	
}
