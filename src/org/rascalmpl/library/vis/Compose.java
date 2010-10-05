package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;

/**
 * Abstract class for the composition of a list of visual elements.
 * 
 * @author paulk
 *
 */
public abstract class Compose extends Figure {

	protected Figure[] figures;
	private static boolean debug = false;

	Compose(FigurePApplet fpa,PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, ctx);	
		int n = elems.length();
		figures = new Figure[n];
		for(int i = 0; i < n; i++){
			IValue v = elems.get(i);
			IConstructor c = (IConstructor) v;
			if(debug)System.err.println("Compose, elem = " + c.getName());
			figures[i] = FigureFactory.make(fpa, c, properties, ctx);
		}
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		for(Figure fig : figures)
			if(fig.mouseOver(mousex, mousey))
				return true;
		return super.mouseOver(mousex, mousey);
	}
	
//	@Override
//	public void drawFocus(){
//		for(Figure fig : figures)
//			fig.drawFocus();
//	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey){
		for(Figure fig : figures)
			if(fig.mousePressed(mousex, mousey))
				return true;
		return super.mousePressed(mousex, mousey);
	}
	
	@Override
	public boolean mouseDragged(int mousex, int mousey){
		for(Figure fig : figures)
			if(fig.mouseDragged(mousex, mousey))
				return true;
		return super.mouseDragged(mousex, mousey);
	}
}
