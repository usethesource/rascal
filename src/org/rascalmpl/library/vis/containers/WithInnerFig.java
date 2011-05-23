package org.rascalmpl.library.vis.containers;

import java.util.HashMap;
import java.util.Vector;

import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Coordinate;


public abstract class WithInnerFig extends Figure {
	
	protected Figure innerFig;
	final static boolean debug = false;
	double innerFigX, innerFigY;
	
	public WithInnerFig(IFigureApplet fpa, Figure inner, PropertyManager properties) {
		super(fpa, properties);
		this.innerFig = inner;
		if(debug)System.err.printf("container.init: width=%f, height=%f, hanchor=%f, vanchor=%f\n", width, height, getHAlignProperty(), getVAlignProperty());
	}
	

	@Override public void destroy(){
		if(innerFig != null)
			innerFig.destroy();
	}
	
	public void gatherProjections(double left, double top, Vector<HScreen.ProjectionPlacement> projections, boolean first, String screenId, boolean horizontal){
		if(innerFig!=null){
			innerFig.gatherProjections(left + innerFigX, top + innerFigY, projections, first, screenId, horizontal);
		}
	}
	
	public void propagateScaling(double scaleX,double scaleY,HashMap<String,Double> axisScales){
		super.propagateScaling(scaleX, scaleY,axisScales);
		if(innerFig != null){
			innerFig.propagateScaling(scaleX, scaleY,axisScales);
		}
	}
	
	public Extremes getExtremesForAxis(String axisId, double offset, boolean horizontal){
		Extremes result = super.getExtremesForAxis(axisId, offset, horizontal);
		if(result.gotData()){
			return result;
		} else if(innerFig != null){
			return innerFig.getExtremesForAxis(axisId, offset, horizontal);
		} else {
			return new Extremes();
		}
	}
	
	public double getOffsetForAxis(String axisId, double offset, boolean horizontal){
		double result = super.getOffsetForAxis(axisId, offset, horizontal);
		if(result != Double.MAX_VALUE){
			return result;
		} else if (innerFig != null){
			double off = 0.0f;
			if(horizontal){
				off = innerFigX;
			} else {
				off = innerFigY;
			}
			return Math.min(offset,offset + off);
		} else {
			return Double.MAX_VALUE;
		}
	}
	
	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		if(!mouseInside(c.getX(), c.getY())) return false;
		if(innerFig != null){
			innerFig.getFiguresUnderMouse(c, result);
		}
		result.add(this);
		return true;
	}
	
	public void computeFiguresAndProperties(){
		super.computeFiguresAndProperties();
		if(innerFig!=null){
			innerFig.computeFiguresAndProperties();
		}
	}
	
	public void registerNames(){
		super.registerNames();
		if(innerFig != null) innerFig.registerNames();
	}
	
}
