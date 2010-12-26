package org.rascalmpl.library.vis.interaction;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

public class BoolControlEffect extends Figure {
	
	private String name;
	private boolean kind;
	private Figure fig;

	public BoolControlEffect(FigurePApplet fpa, IPropertyManager properties, IString name, boolean on, IConstructor fig, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		this.name = name.getValue();
		kind = on;
		this.fig = FigureFactory.make(fpa, fig, properties, ctx);
	}

	private boolean isEnabled(){
		if(fpa.isBoolControl(name))
			return kind;
		else
			return !kind;
	}
	
	@Override
	public void bbox() {
		if(isEnabled()){
			fig.bbox();
			width = fig.width;
			height = fig.height;
		} else
			width = height = 0;
	}

	@Override
	public void draw(float left, float top) {
		if(isEnabled())
			fig.draw(left, top);
		
	}
	@Override
	public boolean mouseOver(int mousex, int mousey){
		return isEnabled() ? fig.mouseOver(mousex, mousey) : false;
	}
	
	@Override
	public void drawMouseOverFigure(){
		if(isEnabled()) 
			fig.drawMouseOverFigure();
		
	}

}
