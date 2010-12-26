package org.rascalmpl.library.vis.interaction;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

public class BoolControl extends Figure {
	private String name;
	private boolean on = false;
	private Figure figOn;
	private Figure figOff;
	
	public BoolControl(FigurePApplet fpa, IPropertyManager properties, IString name, IConstructor figOn, IConstructor figOff, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		this.name = name.getValue();
		this.figOn = FigureFactory.make(fpa, figOn, properties, ctx);
		this.figOff = FigureFactory.make(fpa, figOff, properties, ctx);
	}

	@Override
	public void bbox() {
		if(on) {
			figOn.bbox(); 
			width = figOn.width;
			height = figOn.height;
		} else {
			figOff.bbox();
			width = figOff.width;
			height = figOff.height;
		}
	}

	@Override
	public void draw(float left, float top) {
		if(on) figOn.draw(left,top); else figOff.draw(left, top);
	}
	
	@Override
	public boolean mouseInside(int mousex, int mousey){
		return on ? figOn.mouseInside(mousex, mousey)
				  : figOff.mouseInside(mousex, mousey);
	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey){
		if(mouseInside(mousex, mousey)){
			on = !on;
			fpa.setBoolControl(name, on);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		return on ? figOn.mouseOver(mousex, mousey)
				  : figOff.mouseOver(mousex, mousey);
	}
	
	@Override
	public void drawMouseOverFigure(){
		if(on) 
			figOn.drawMouseOverFigure();
		else 
			figOff.drawMouseOverFigure();
	}

}
