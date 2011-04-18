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
package org.rascalmpl.library.vis.compose;

import java.awt.event.MouseEvent;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

/**
 * Abstract class for the composition of a list of visual elements.
 * 
 * @author paulk
 * 
 */
public abstract class Compose extends Figure {

	final protected Figure[] figures;
	final private static boolean debug = false;

	protected Compose(IFigureApplet fpa, PropertyManager properties,
			IList elems, IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties);
		int n = elems.length();
		figures = new Figure[n];
		for (int i = 0; i < n; i++) {
			IValue v = elems.get(i);
			IConstructor c = (IConstructor) v;
			if (debug)
				System.err.println("Compose, elem = " + c.getName());
			figures[i] = FigureFactory.make(fpa, c, properties, childProps, ctx);
		}
	}

	@Override
	public boolean mouseInside(int mousex, int mousey) {
		if(super.mouseInside(mousex, mousey)){
			for (int i = figures.length - 1; i >= 0; i--)
				if (figures[i].mouseInside(mousex, mousey))
					return true;
		}
		return false;
	}

	@Override
	public boolean mouseInside(int mousex, int mousey, float centerX,
			float centerY) {
		if(super.mouseInside(mousex, mousey, centerX, centerY)){
			for (int i = figures.length - 1; i >= 0; i--)
				if (figures[i].mouseInside(mousex, mousey, figures[i].getCenterX(),
						figures[i].getCenterY()))
					return true;
		}
		return false;
	}
	
	// Visit figures front to back
	@Override
	public boolean mouseOver(int mouseX, int mouseY,  float centerX, float centerY, boolean mouseInParent) {
		// System.err.println("Compose.MouseOver2: " + this.getClass());
		if(super.mouseInside(mouseX, mouseY)){
			for (int i = figures.length - 1; i >= 0; i--){
				// System.err.println("Compose.MouseOver, child : " + i);
				if (figures[i].mouseOver(mouseX, mouseY, figures[i].getCenterX(),
						figures[i].getCenterY(), false)){
					return true;
				}
			}
		}
		return false; //super.mouseOver(mouseX, mouseY, mouseInParent);
	}

	// Visit figures front to back
	@Override
	public synchronized boolean mousePressed(int mouseX, int mouseY, MouseEvent e) {
		if(super.mouseInside(mouseX, mouseY)){
			for (int i = figures.length - 1; i >= 0; i--)
				if (figures[i].mousePressed(mouseX, mouseY, e))
					return true;
		}
		return false; //super.mousePressed(mouseX, mouseY, e);
	}

//	 @Override
//	 public boolean mouseDragged(int mousex, int mousey){
//	 for(Figure fig : figures)
//	 if(fig.mouseDragged(mousex, mousey))
//	 return true;
//	 return super.mouseDragged(mousex, mousey);
//	 }

	@Override
	public boolean keyPressed(int key, int keyCode) {
		for (int i = figures.length - 1; i >= 0; i--)
			if (figures[i].keyPressed(key, keyCode))
				return true;
		return super.keyPressed(key, keyCode);
	}
	
	@Override public void destroy(){
		for (int i = figures.length - 1; i >= 0; i--)
			figures[i].destroy();
	}
		
}
