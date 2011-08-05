package org.rascalmpl.library.vis.figure.combine;

import java.util.Vector;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.zorder.ISWTZOrdering;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.Rectangle;

public class FigureWithNonLocalFigure extends LayoutProxy {
	
	// Figure with associated non local figure which is drawed from elsewhere (also overlap ordering should be specified elsewhere)
	
	public Figure nonLocalFigure;
	protected Coordinate nonLocalFigureLoc;
	
	public FigureWithNonLocalFigure(Figure inner, Figure nonLocalFigure, PropertyManager properties) {
		super(inner, properties);
		this.nonLocalFigure = nonLocalFigure;
		nonLocalFigureLoc = new Coordinate();
	}

	public boolean getFiguresinnerFigMouse(Coordinate c,Vector<Figure> result){
		return innerFig.getFiguresUnderMouse(c, result);
	}

	public void init(){
		innerFig.init();
		nonLocalFigure.init();
	}
	
	@Override
	public void bbox(){
		super.bbox();
		nonLocalFigure.bbox();
	}
	
	public void computeFiguresAndProperties(ICallbackEnv env){
		super.computeFiguresAndProperties(env);
		innerFig.computeFiguresAndProperties(env);
		nonLocalFigure.computeFiguresAndProperties(env);
	}
	
	public void registerNames(NameResolver resolver){
		super.registerNames(resolver);
		innerFig.registerNames(resolver);
		nonLocalFigure.registerNames(resolver);
	}
	
	public void registerValues(NameResolver resolver){
		super.registerValues(resolver);
		innerFig.registerValues(resolver);
		nonLocalFigure.registerValues(resolver);
	}
	

	public void getLikes(NameResolver resolver){
		super.getLikes(resolver);
		innerFig.getLikes(resolver);
		nonLocalFigure.getLikes(resolver);
	}
	
	public void finalize(){
		super.finalize();
		innerFig.finalize();
		nonLocalFigure.finalize();
	}

	public void setSWTZOrder(ISWTZOrdering zorder){
		innerFig.setSWTZOrder(zorder);
	}

	public void destroy(){
		super.destroy();
		innerFig.destroy();
		nonLocalFigure.destroy();
	}
	
	public void draw(GraphicsContext gc) {
		innerFig.draw(gc);
	}
	
	@Override
	public void drawPart(Rectangle r,GraphicsContext gc){
		innerFig.drawPart(r, gc);
	}
	
	@Override
	public void  setLocationOfChildren(){
		if(nonLocalFigure != null){
			nonLocalFigure.globalLocation.set(globalLocation);
			nonLocalFigure.globalLocation.add(nonLocalFigureLoc);
		}
	}
}
