package org.rascalmpl.library.vis.figure.keys;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.FigureWithNonLocalFigure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureApplet;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;

public class Projection extends FigureWithNonLocalFigure {

	String projectOn;
	IEvaluatorContext ctx;
	boolean vertical;
	
	public Projection(IFigureConstructionEnv env,String projectOn, Figure projection,Figure innerFigure,PropertyManager properties) {
		super(innerFigure, projection, properties);
		this.projectOn = projectOn;
		this.ctx = env.getRascalContext();
	}
	
	
	public void registerValues(NameResolver resolver){
		super.registerValues(resolver);
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
	

	@Override
	public void bbox() {
		super.bbox();
		for(boolean flip : BOTH_DIMENSIONS){
			if(flip == vertical){
				minSize.setWidth(flip,Math.max(minSize.getWidth(flip), nonLocalFigure.minSize.getWidth(flip) / nonLocalFigure.getHShrinkProperty(flip)));
			}
		}
	}
}
