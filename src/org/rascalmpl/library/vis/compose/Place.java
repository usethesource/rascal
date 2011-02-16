package org.rascalmpl.library.vis.compose;

import java.awt.event.MouseEvent;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/*
 * Given are a first figure (bottomFigure) that contains a second figure (refFigure) with identity id.
 * Place a third figure (topFigure) on top of refFigure
 */
public class Place extends Figure {
	
	private Figure bottomFigure;
	private Figure refFigure;
	private Figure topFigure;


	public Place(FigurePApplet fpa, IPropertyManager properties, IConstructor ctop, IString id, IConstructor cbot, IEvaluatorContext ctx) {
		super(fpa, properties);
		this.bottomFigure = FigureFactory.make(fpa, cbot, properties, ctx);
		this.topFigure = FigureFactory.make(fpa, ctop, properties, ctx);
		this.refFigure = fpa.getRegisteredId(id.getValue());
		if(this.refFigure == null)
			throw RuntimeExceptionFactory.figureException("Cannot place on not (yet) existing figure", id, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}

	@Override
	public void bbox() {
		bottomFigure.bbox();
		topFigure.bbox();
		
		float hanchor = properties.getHanchor();
		float vanchor = properties.getVanchor();
		width = max(bottomFigure.width, hanchor * refFigure.width + topFigure.width/2);
		height = max(bottomFigure.height, vanchor * refFigure.height + topFigure.height/2);
	}

	@Override
	public void draw(float left, float top) {
		setLeft(left);
		setTop(top);
		float hanchor = properties.getHanchor();
		float vanchor = properties.getVanchor();
		bottomFigure.draw(left, top);
		topFigure.draw(refFigure.getLeft() + hanchor * refFigure.width - topFigure.width/2,
				       refFigure.getTop()  + vanchor * refFigure.height - topFigure.height/2);
	}

	@Override
	public boolean mouseInside(int mouseX, int mouseY){
		return bottomFigure.mouseInside(mouseX, mouseY) || 
			   topFigure.mouseInside(mouseX, mouseY);
	}
	
	@Override
	public boolean mouseInside(int mouseX, int mouseY, float centerX,
			float centerY) {
		return bottomFigure.mouseInside(mouseX, mouseY, centerX, centerY) || 
		       topFigure.mouseInside(mouseX, mouseY, centerX, centerY);
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY, float centerX, float centerY, boolean mouseInParent){
		return bottomFigure.mouseOver(mouseX, mouseY, centerX, centerY, mouseInParent) || 
		       topFigure.mouseOver(mouseX, mouseY, centerX, centerY, mouseInParent);
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY, boolean mouseInParent){
		return bottomFigure.mouseOver(mouseX, mouseY, mouseInParent) || 
	           topFigure.mouseOver(mouseX, mouseY, mouseInParent);
	}
	
	@Override
	public boolean mousePressed(int mouseX, int mouseY, MouseEvent e){
		return bottomFigure.mousePressed(mouseX, mouseY, e) || 
        	   topFigure.mousePressed(mouseX, mouseY, e);
	}
	
	@Override
	public void destroy(){
		bottomFigure.destroy();
        topFigure.destroy();
	}
}
