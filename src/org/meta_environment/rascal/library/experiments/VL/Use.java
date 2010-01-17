package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class Use extends VELEM {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VELEM inside;
	private static boolean debug = false;

	public Use(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside,IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		if(inside != null){
			this.inside = VELEMFactory.make(vlp, inside, this.properties, ctx);
		}
		if(debug)System.err.println("use.init: width=" + width + ", height=" + height);
	}

	@Override 
	void bbox(int left, int top){
		this.left = left;
		this.top = top;
		inside.bbox(left, top);
		width = inside.width;
		height = inside.height;
		if(debug)System.err.println("use.bbox: width=" + width + ", height=" + height);
	}

	@Override
	void draw() {
		applyProperties();
		inside.draw(left,top);
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		return inside.mouseOver(mousex, mousey);
	}
}
