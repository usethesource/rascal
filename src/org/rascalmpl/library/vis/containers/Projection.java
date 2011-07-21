package org.rascalmpl.library.vis.containers;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.NameResolver;

public class Projection extends WithInnerFig {

	String projectOn;
	Figure projection;
	IEvaluatorContext ctx;
	boolean vertical;
	
	public Projection(IFigureApplet fpa, String projectOn, Figure projection,Figure innerFigure,PropertyManager properties, IEvaluatorContext ctx) {
		super(fpa,innerFigure,properties);
		this.projectOn = projectOn;
		this.projection = projection;
		this.properties = innerFigure.properties;
		this.ctx = ctx;
	}
	
	public void init(){
		super.init();
		projection.init();
	}
	
	public void computeFiguresAndProperties(){
		super.computeFiguresAndProperties();
		projection.computeFiguresAndProperties();
	}
	
	public void registerNames(NameResolver resolver){
		super.registerNames(resolver);
		projection.registerNames(resolver);
	}
	
	public void registerValues(NameResolver resolver){
		super.registerValues(resolver);
		projection.registerValues(resolver);
		Figure fscreen = resolver.resolve(projectOn);
		if(fscreen instanceof HScreen){
			HScreen screen = (HScreen) fscreen;
			screen.registerProjection(this);
			vertical = screen.isVertical();
		} else {
			throw RuntimeExceptionFactory.figureException("Cannot project on non-screen:" + projectOn, ctx.getValueFactory().string(projectOn), ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
	}
	

	public void getLikes(NameResolver resolver){
		super.getLikes(resolver);
		projection.getLikes(resolver);
	}
	
	public void finalize(){
		super.finalize();
		projection.finalize();
	}
	

	public void destroy(){
		super.destroy();
		projection.destroy();
	}

	@Override
	public void bbox() {
		innerFig.bbox();
		projection.bbox();
		for(boolean flip : BOTH_DIMENSIONS){
			minSize.setWidth(flip, innerFig.getWidth(flip));
			if(flip == vertical){
				minSize.setWidth(flip,Math.max(minSize.getWidth(flip), projection.minSize.getWidth(flip) / projection.getHShrinkProperty(flip)));
			}
			setResizableX(flip, innerFig.getResizableX(flip));
		}
	}
	
	@Override
	public void layout(){
		innerFig.size.set(size);
		innerFig.globalLocation.set(globalLocation);
		innerFig.layout();
		
	}

	@Override
	public void draw(double left, double top, GraphicsContext gc) {
		innerFig.draw(left, top, gc);
		
	}
}
