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

public class Projection extends Figure {

	String projectOn;
	Figure projection;
	Figure innerFig;
	
	public Projection(String projectOn, IConstructor projection,IFigureApplet fpa, PropertyManager properties,
			IConstructor innerCons, IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties);
		this.innerFig = FigureFactory.make(fpa, innerCons, this.properties, childProps, ctx);
		this.projectOn = projectOn;
		this.projection = FigureFactory.make(fpa, projection, this.properties, childProps, ctx);
	}
	
	public void gatherProjections(float left, float top, Vector<HScreen.ProjectionPlacement> projections, boolean first, String screenId, boolean horizontal){
		if(projectOn.equals(screenId) || (projectOn.equals("") && first)){
			if(horizontal){
				projections.add(new HScreen.ProjectionPlacement(left + getHAlignProperty() * innerFig.width, top, getVGapProperty(), projection));
			} else {
				projections.add(new HScreen.ProjectionPlacement(left, top + getVAlignProperty() * innerFig.height, getHGapProperty(), projection));
			}
		} 
		//System.out.printf("Got %s looking for %s", screenId, projectOn);
		innerFig.gatherProjections(left , top , projections, false, screenId, horizontal);
	}

	@Override
	public void bbox(float desiredWidth, float desiredHeight) {
		innerFig.bbox(desiredWidth, desiredHeight);
		this.width = innerFig.width;
		this.height = innerFig.height;
	}

	@Override
	public void draw(float left, float top) {
		innerFig.draw(left, top);
		
	}
	

	public float leftAlign() {
		return innerFig.leftAlign();
	}

	public float rightAlign() {
		return innerFig.rightAlign();
	}

	public float topAlign() {
		return innerFig.topAlign();
	}

	public float bottomAlign() {
		return innerFig.bottomAlign();
	}
	
	
	public Extremes getExtremesForAxis(String axisId, float offset, boolean horizontal){
		Extremes result = super.getExtremesForAxis(axisId, offset, horizontal);
		if(result.gotData()){
			return result;
		} else {
			return innerFig.getExtremesForAxis(axisId, offset, horizontal);
		}
	}
	
	public float getOffsetForAxis(String axisId, float offset, boolean horizontal){
		float result = super.getOffsetForAxis(axisId, offset, horizontal);
		if(result != Float.MAX_VALUE){
			return result;
		} else {
			return innerFig.getOffsetForAxis(axisId, offset, horizontal);
		}
	}
	

	public void propagateScaling(float scaleX,float scaleY,HashMap<String,Float> axisScales){
		super.propagateScaling(scaleX, scaleY,axisScales);
		if(innerFig != null){
			innerFig.propagateScaling(scaleX, scaleY,axisScales);
		}
	}
	
}
