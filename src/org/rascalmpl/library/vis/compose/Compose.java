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


import java.util.Vector;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.ISWTZOrdering;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.Rectangle;

/**
 * Abstract class for the composition of a list of visual elements.
 * 
 * @author paulk
 * 
 */
public abstract class Compose extends Figure {

	final protected Figure[] figures;
	protected Coordinate[] pos;
	final private static boolean debug = false;

	protected Compose(Figure[] figures,PropertyManager properties) {
		super(properties);
		this.figures = figures;
		pos = new Coordinate[figures.length];
		for(int i = 0 ; i < figures.length ; i++){
			pos[i] = new Coordinate();
		}
	}

	@Override
	public boolean keyPressed(int key, int keyCode) {
		for (int i = figures.length - 1; i >= 0; i--)
			if (figures[i].keyPressed(key, keyCode))
				return true;
		return super.keyPressed(key, keyCode);
	}
	
		
	
	@Override
	public
	void draw(GraphicsContext gc){
		applyProperties(gc);
		for(int i = 0; i < figures.length; i++){
			figures[i].draw(gc);
		}
	}
	
	@Override
	public
	void drawPart(Rectangle r,GraphicsContext gc){
		applyProperties(gc);
		for(int i = 0; i < figures.length; i++){
			if(r.contains(figures[i].globalLocation, figures[i].size)){
				figures[i].draw(gc);
			} else if(figures[i].overlapsWith(r)){
				figures[i].drawPart(r,gc);
			}
		}
	}
	
	
	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		if(!mouseInside(c.getX(), c.getY())) return false;
		for(int i = figures.length-1 ; i >= 0 ; i--){
			if(figures[i].getFiguresUnderMouse(c, result)){
				break;
			}
		}
		result.add(this);
		return true;
	}
	
	public void init(){
		super.init();
		for(Figure fig : figures){
			fig.init();
		}
	}
	
	public void computeFiguresAndProperties(ICallbackEnv env){
		super.computeFiguresAndProperties(env);
		for(Figure fig : figures){
			fig.computeFiguresAndProperties(env);
		}
	}
	
	public void registerNames(NameResolver resolver){
		super.registerNames(resolver);
		for(Figure fig : figures){
			fig.registerNames(resolver);
		}
	}
	

	public void registerValues(NameResolver resolver){
		super.registerValues(resolver);
		for(Figure fig : figures){
			fig.registerValues(resolver);
		}
	}
	

	public void getLikes(NameResolver resolver){
		super.getLikes(resolver);
		for(Figure fig : figures){
			fig.getLikes(resolver);
		}
	}
	
	public void layout(){
		for(Figure fig : figures){
			fig.layout();
		}
	}
	
	public void finalize(){
		super.finalize();
		for(Figure fig : figures){
			fig.finalize();
		}
	}
	
	public void destroy(){
		super.destroy();
		for(Figure fig : figures){
			fig.destroy();
		}
	}
	
	public void bbox(){
		for(Figure fig : figures){
			fig.bbox();
		}
	}
	
	public void setSWTZOrder(ISWTZOrdering zorder){
		for(Figure fig : figures){
			fig.setSWTZOrder(zorder);
		}
	}
}
