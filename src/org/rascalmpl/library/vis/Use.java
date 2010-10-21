package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

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

	public Use(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IConstructor inside,IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, ctx);
		if(inside != null){
			this.inside = FigureFactory.make(fpa, inside, this.properties, ctx);
		}
		if(debug)System.err.println("use.init: width=" + width + ", height=" + height);
	}

	@Override 
	void bbox(){
		
		inside.bbox();
		width = inside.width;
		height = inside.height;
		if(debug)System.err.println("use.bbox: width=" + width + ", height=" + height);
	}

	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		applyProperties();
		
		inside.draw(left + properties.hanchor*(width - inside.width),
					top  + properties.vanchor*(height - inside.height));
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
	public boolean mouseOver(int mousex, int mousey){
		return inside.mouseOver(mousex, mousey);
	}
}
