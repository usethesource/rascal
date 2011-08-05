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

public abstract class WithInnerFig extends Figure {
	
	final static boolean debug = false;
	public Figure innerFig;
	public Coordinate innerFigLocation;

	public WithInnerFig(Figure inner, PropertyManager properties) {
		super(properties);
		this.innerFig = inner;
		innerFigLocation = new Coordinate();
		if(debug)System.err.printf("container.init: width=%f, height=%f, hanchor=%f, vanchor=%f\n", minSize.getWidth(), minSize.getHeight(), getHAlignProperty(), getVAlignProperty());
	}
	
	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		if(!mouseInside(c.getX(), c.getY())) return false;
		if(innerFig != null){
			
			innerFig.getFiguresUnderMouse(c, result);
		}
		result.add(this);
		return true;
	}
	
	@Override
	public void draw(GraphicsContext gc){
		if(innerFig != null){
			innerFig.draw(gc);
		}
	}
	
	@Override
	public void drawPart(Rectangle r,GraphicsContext gc){
		if(innerFig != null ){
			if(innerFig.isContainedIn(r)){
				innerFig.draw(gc);
			} else if(innerFig.overlapsWith(r)){
				innerFig.drawPart(r, gc);
			}
		}
	}
	
	public void init(){
		super.init();
		if(innerFig!=null){
			innerFig.init();
		}
	}
	
	public void computeFiguresAndProperties(ICallbackEnv env){
		super.computeFiguresAndProperties(env);
		if(innerFig!=null){
			innerFig.computeFiguresAndProperties(env);
		}
	}
	
	public void registerNames(NameResolver resolver){
		super.registerNames(resolver);
		if(innerFig != null) innerFig.registerNames(resolver);
	}
	
	public void registerValues(NameResolver resolver){
		super.registerValues(resolver);
		if(innerFig!=null) innerFig.registerValues(resolver);
	}
	

	public void getLikes(NameResolver resolver){
		super.getLikes(resolver);
		if(innerFig!=null) innerFig.getLikes(resolver);
	}
	
	public void layout(){
		size.set(minSize);
		if(innerFig != null) {
			innerFig.size.set(innerFig.minSize);
			innerFig.layout();
		}
	}
	
	
	public void finalize(){
		super.finalize();
		if(innerFig!=null){
			innerFig.finalize();
		}
	}
	

	public void destroy(){
		super.destroy();
		if(innerFig!=null){
			innerFig.destroy();
		}
	}
	
	public void setSWTZOrder(ISWTZOrdering zorder){
		if(innerFig!=null){
			innerFig.setSWTZOrder(zorder);
		}
	}
	
	public void setLocationOfChildren(){
		if(innerFig!=null){
			innerFig.globalLocation.set(globalLocation);
			innerFig.globalLocation.add(innerFigLocation);
			innerFig.setLocationOfChildren();
		}
	}
	
}
