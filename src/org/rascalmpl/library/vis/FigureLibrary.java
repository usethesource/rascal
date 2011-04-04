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
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.values.ValueFactoryFactory;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * 
 * FigureLibrary: Rascal library functions to access Processing's graphics operations.
 * 
 * @author paulk
 *
 */

public class FigureLibrary extends PApplet {

	private  static final long serialVersionUID = 1L;
	
	static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private static IList fontNames;
	
	public FigureLibrary(IValueFactory factory){
		vf = factory;
	}
	
	public void render(IConstructor fig, IEvaluatorContext ctx){
		PApplet pa = new FigurePApplet(fig, ctx);
		new StandaloneSWTBridge(pa);
	}
	
	public void render(IString name, IConstructor fig,  IEvaluatorContext ctx){
		PApplet pa = new FigurePApplet(name, fig, ctx);
		new StandaloneSWTBridge(pa);
	}
	
	public synchronized void renderSave(IConstructor fig, ISourceLocation file, IEvaluatorContext ctx){
		PApplet pa = new FigurePApplet(fig, file, ctx);
		pa.init();
		synchronized(pa){
			pa.setup();
			pa.draw();
			pa.flush();
			//pa.stop();
			//pa.exit();
			pa.destroy();
		}
	}
		
	public IList fontNames(){
		if(fontNames == null){
			TypeFactory types = TypeFactory.getInstance();
			IListWriter w = vf.listWriter(types.stringType());
			//PApplet pa = new PApplet();
			//pa.createFont("Helvetica", 12);
			for(String s : PFont.list()){
				w.append(vf.string(s));
			}
			//pa.destroy();
			fontNames = w.done();
		}
		return fontNames;
	}
	
}

