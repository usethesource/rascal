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

	protected Compose(FigurePApplet fpa,IPropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);	
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
	public boolean mouseInside(int mousex, int mousey){
		for(int i = figures.length-1; i >= 0; i--)
			if(figures[i].mouseInside(mousex, mousey))
				return true;
		return super.mouseInside(mousex, mousey);
	}
	
	@Override
	public boolean mouseInside(int mousex, int mousey,  float centerX, float centerY){
		for(int i = figures.length-1; i >= 0; i--)
			if(figures[i].mouseInside(mousex, mousey, figures[i].getCenterX(), figures[i].getCenterY()))
				return true;
		return super.mouseInside(mousex, mousey, centerX, centerY);
	}

	// Visit figures front to back
	@Override
	public boolean mouseOver(int mousex, int mousey, boolean mouseInParent){
		for(int i = figures.length-1; i >= 0; i--)
			if(figures[i].mouseOver(mousex, mousey, figures[i].getCenterX(), figures[i].getCenterY(), false))
				return true;
		return super.mouseOver(mousex, mousey, mouseInParent);
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey, float centerX, float centerY, boolean mouseInParent){
		for(int i = figures.length-1; i >= 0; i--)
			if(figures[i].mouseOver(mousex, mousey, figures[i].getCenterX(), figures[i].getCenterY(), false))
				return true;
		return super.mouseOver(mousex, mousey, centerX, centerY, mouseInParent);
	}
	
	// Visit figures front to back
	@Override
	public boolean mousePressed(int mousex, int mousey, MouseEvent e){
		for(int i = figures.length-1; i >= 0; i--)
			if(figures[i].mousePressed(mousex, mousey, e))
				return true;
		return super.mousePressed(mousex, mousey, e);
	}
	
//	@Override
//	public boolean mouseDragged(int mousex, int mousey){
//		for(Figure fig : figures)
//			if(fig.mouseDragged(mousex, mousey))
//				return true;
//		return super.mouseDragged(mousex, mousey);
//	}
	
	@Override
	public boolean keyPressed(int key, int keyCode){
		for(int i = figures.length-1; i >= 0; i--)
			if(figures[i].keyPressed(key, keyCode))
				return true;
		return super.keyPressed(key, keyCode);
	}
}
