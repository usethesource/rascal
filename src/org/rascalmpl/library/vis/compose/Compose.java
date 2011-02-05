package org.rascalmpl.library.vis.compose;

import java.awt.event.MouseEvent;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/**
 * Abstract class for the composition of a list of visual elements.
 * 
 * @author paulk
 * 
 */
public abstract class Compose extends Figure {

	protected Figure[] figures;
	private static boolean debug = false;

	protected Compose(FigurePApplet fpa, IPropertyManager properties,
			IList elems, IEvaluatorContext ctx) {
		super(fpa, properties);
		int n = elems.length();
		figures = new Figure[n];
		for (int i = 0; i < n; i++) {
			IValue v = elems.get(i);
			IConstructor c = (IConstructor) v;
			if (debug)
				System.err.println("Compose, elem = " + c.getName());
			figures[i] = FigureFactory.make(fpa, c, properties, ctx);
		}
	}

	@Override
	public synchronized boolean mouseInside(int mousex, int mousey) {
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
	public boolean mouseOver(int mousex, int mousey, boolean mouseInParent) {
		if (debug) System.err.println("MouseOver:"+this.getClass()+" "+super.getClass());
		if (super.mouseOver(mousex, mousey, mouseInParent)) return true;
		for (int i = figures.length - 1; i >= 0; i--)
			if (figures[i].mouseOver(mousex, mousey, figures[i].getCenterX(),
					figures[i].getCenterY(), false))
				return true;
	return false;
	}

	/*@Override
	public boolean mouseOver(int mousex, int mousey, float centerX,
			float centerY, boolean mouseInParent) {
		System.err.println("MouseOver:"+this.getClass()+" "+super.getClass());
//		for (int i = figures.length - 1; i >= 0; i--) {
//			if (figures[i].mouseOver(mousex, mousey, figures[i].getCenterX(),
//					figures[i].getCenterY(), false))
//				return true;
//		}
		return super.mouseOver(mousex, mousey, centerX, centerY, mouseInParent);
	}
	*/

	// Visit figures front to back
	@Override
	public synchronized boolean mousePressed(int mouseX, int mouseY, MouseEvent e) {
			if(super.mouseInside(mouseX, mouseY)){
				for (int i = figures.length - 1; i >= 0; i--)
					if (figures[i].mousePressed(mouseX, mouseY, e))
						return true;
				return super.mousePressed(mouseX, mouseY, e);
			}
			return false;
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
