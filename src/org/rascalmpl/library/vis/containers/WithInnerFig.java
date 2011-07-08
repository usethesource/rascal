package org.rascalmpl.library.vis.containers;

import java.util.Vector;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.NameResolver;


public abstract class WithInnerFig extends Figure {
	

	protected Figure innerFig;
	final static boolean debug = false;
	Coordinate innerFigLocation;

	public WithInnerFig(IFigureApplet fpa, Figure inner, PropertyManager properties) {
		super(fpa, properties);
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
	
	public void init(){
		super.init();
		if(innerFig!=null){
			innerFig.init();
		}
	}
	
	public void computeFiguresAndProperties(){
		super.computeFiguresAndProperties();
		if(innerFig!=null){
			innerFig.computeFiguresAndProperties();
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
	
}
