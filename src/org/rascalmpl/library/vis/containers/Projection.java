package org.rascalmpl.library.vis.containers;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureApplet;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;

public class Projection extends LayoutProxy {

	String projectOn;
	Figure projection;
	IEvaluatorContext ctx;
	boolean vertical;
	
	public Projection(IFigureConstructionEnv env,String projectOn, Figure projection,Figure innerFigure,PropertyManager properties) {
		super(innerFigure,properties);
		this.projectOn = projectOn;
		this.projection = projection;
		this.ctx = env.getRascalContext();
	}
	
	public void init(){
		super.init();
		projection.init();
	}
	
	public void computeFiguresAndProperties(ICallbackEnv env){
		super.computeFiguresAndProperties(env);
		projection.computeFiguresAndProperties(env);
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
		super.bbox();
		projection.bbox();
		for(boolean flip : BOTH_DIMENSIONS){
			if(flip == vertical){
				minSize.setWidth(flip,Math.max(minSize.getWidth(flip), projection.minSize.getWidth(flip) / projection.getHShrinkProperty(flip)));
			}
		}
	}
}
