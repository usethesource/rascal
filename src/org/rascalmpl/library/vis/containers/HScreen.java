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
import org.rascalmpl.library.vis.properties.descriptions.BoolProp;

public class HScreen extends Figure {
	
	Figure innerFig;
	Vector<ProjectionPlacement> projections; 
	float innerFigX, innerFigY;
	float borderMin, borderMax;
	
	public HScreen(IConstructor innerCons, IFigureApplet fpa, PropertyManager properties,  IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties);
		this.innerFig = FigureFactory.make(fpa, innerCons, this.properties, childProps, ctx);
		projections = new Vector<HScreen.ProjectionPlacement>();
	}
	
	public static class ProjectionPlacement{
		float xPos, yPos , originalXposition, originalYPosition;
		float gap;
		Figure fig;
		public ProjectionPlacement(float originalXposition, float originalYPosition, float gap, Figure fig) {
			this.originalXposition = originalXposition;
			this.originalYPosition = originalYPosition;
			this.gap = gap;
			this.fig = fig;
		}
	}
	
	@Override
	public void bbox(float desiredWidth, float desiredHeight) {
		innerFig.bbox(desiredWidth, desiredHeight);
		width = innerFig.width;
		height = innerFig.height;
		innerFigX = innerFigY = 0.0f;
		startGatheringProjections();
		float shiftX, shiftY;
		shiftX = shiftY = 0.0f;
		float oldWidth = width;
		float oldHeight = height;
		for(ProjectionPlacement p : projections){
			p.fig.bbox(AUTO_SIZE,AUTO_SIZE);
			placeProjection(p);
			shiftX = max(shiftX,-p.xPos);
			shiftY = max(shiftY,-p.yPos);
			width = max(width,p.xPos + p.fig.width);
			height = max(height,p.yPos + p.fig.height);
		}
		shiftX = addHorizontalSpacing(shiftX, oldWidth);
		shiftY = addVerticalSpacing(shiftY,oldHeight);
		setBorders(shiftX,shiftY, oldWidth, oldHeight);
		width+= shiftX;
		height+=shiftY;
		for(ProjectionPlacement p : projections){
			p.xPos+=shiftX;
			p.yPos+=shiftY;
		}
		innerFigX = shiftX;
		innerFigY = shiftY;
		System.out.printf("hscreen w %f h %f shiftX %f shiftY %f\n",innerFigX,innerFigY,shiftX,shiftY);
	}

	void setBorders(float shiftX,float shiftY, float oldWidth, float oldHeight) {
		borderMin = shiftY;
		borderMax = oldHeight + shiftY;
	}

	float addHorizontalSpacing(float shiftX, float oldWidth) {
		return shiftX;
	}
	
	float addVerticalSpacing(float shiftY, float oldHeight) {
		if(shiftY > 0.0f){
			shiftY += getHGapProperty();
		} 
		if(height > oldHeight){
			height += getHGapProperty();
		}
		return shiftY;
	}

	void startGatheringProjections() {
		innerFig.gatherProjections(0.0f, 0.0f, projections, true, getIdProperty(), true);
	}

	void placeProjection(ProjectionPlacement p) {
			p.xPos = p.originalXposition - p.fig.leftAlign();
			boolean gapAbove = p.originalYPosition >=  getVAlignProperty() * innerFig.height;
			p.yPos = getVAlignProperty() * innerFig.height +  (gapAbove ? -p.gap : p.gap) - p.fig.topAlign();
	}

	@Override
	public void draw(float left, float top) {
		setLeft(left);
		setTop(top);
		innerFig.draw(left + innerFigX, top + innerFigY);
		for(ProjectionPlacement p : projections){
			p.fig.draw(left + p.xPos, top + p.yPos );
		}
		drawScreen(left, top);
	}

	void drawScreen(float left, float top) {
		System.out.printf("Horizontal borders %f %f\n", innerFig.getHorizontalBorders().getMinimum(),innerFig.getHorizontalBorders().getMaximum() );
		if(properties.getBooleanProperty(BoolProp.DRAW_SCREEN_X)){
			fpa.line(left + innerFigX + innerFig.getHorizontalBorders().getMinimum(),
					top + innerFigY + getVAlignProperty() * innerFig.height,
					left + innerFigX +  innerFig.getHorizontalBorders().getMaximum(),
					top + innerFigY + getVAlignProperty() * innerFig.height);
		}
	}

	public void gatherProjections(float left, float top, Vector<HScreen.ProjectionPlacement> projections, boolean first, String screenId, boolean horizontal){
		innerFig.gatherProjections(left + innerFigX, top + innerFigY, projections, false, screenId, horizontal);
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
			float off = 0.0f;
			if(horizontal){
				off = innerFigX;
			} else {
				off = innerFigY;
			}
			return innerFig.getOffsetForAxis(axisId, offset + off, horizontal);
		}
	}
	

	public void propagateScaling(float scaleX,float scaleY, HashMap<String,Float> axisScales){
		super.propagateScaling(scaleX, scaleY, axisScales);
		innerFig.propagateScaling(scaleX, scaleY, axisScales);
	}
	

	
	public Extremes getVerticalBorders(){
		return new Extremes(borderMin,borderMax);
	}
	
	
}
