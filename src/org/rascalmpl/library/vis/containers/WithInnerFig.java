package org.rascalmpl.library.vis.containers;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;


public abstract class WithInnerFig extends Figure {
	
	final protected Figure innerFig;
	final static boolean debug = false;
	double innerFigX;
	double innerFigY;
	
	WithInnerFig(IFigureApplet fpa, PropertyManager properties, IConstructor innerCons, IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties);
		if(innerCons != null){
			this.innerFig = FigureFactory.make(fpa, innerCons, this.properties, childProps, ctx);
		} else
			this.innerFig = null;
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
			return min(offset,offset + off);
		} else {
			return Double.MAX_VALUE;
		}
	}
}
