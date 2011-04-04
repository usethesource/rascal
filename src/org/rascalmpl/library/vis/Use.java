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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis;

import java.awt.event.MouseEvent;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/**
 * Use another element. Mostly used to override properties.
 * 
 * @author paulk
 *
 */
public class Use extends Figure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Figure inside;
	private static boolean debug = false;

	public Use(FigurePApplet fpa, IPropertyManager properties, IConstructor inside, IEvaluatorContext ctx) {
		super(fpa, properties);
		if(inside != null){
			this.inside = FigureFactory.make(fpa, inside, this.properties, ctx);
		}
		if(debug)System.err.println("use.init: width=" + width + ", height=" + height);
	}

	@Override
	public 
	void bbox(){
		
		inside.bbox();
		width = inside.width;
		height = inside.height;
		if(debug)System.err.println("use.bbox: width=" + width + ", height=" + height);
	}

	@Override
	public
	void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		applyProperties();
		
		inside.draw(left + getHanchorProperty()*(width - inside.width),
					top  + getVanchorProperty()*(height - inside.height));
	}
/*	
	@Override
	protected float leftAnchor(){
		return inside.leftAnchor();
	}
	
	@Override
	protected float rightAnchor(){
		return inside.rightAnchor();
	}
	
	@Override
	protected float topAnchor(){
		return inside.topAnchor();
	}
	
	@Override
	protected float bottomAnchor(){
		return inside.bottomAnchor();
	}
*/	
	@Override
	public boolean mouseOver(int mousex, int mousey, float centerX, float centerY, boolean mouseInParent){
		return inside.mouseOver(mousex, mousey, centerX, centerY, false);
	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey, MouseEvent e) {
		return inside.mousePressed(mousex, mousey, e);
	}
	
	@Override
	public boolean mouseReleased() {	
		return inside.mouseReleased();
	}
	
	@Override
	public boolean mouseDragged(int mousex, int mousey){
		return inside.mouseDragged(mousex, mousey);
	}
}
