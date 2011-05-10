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
	double innerFigX, innerFigY;
	double borderMin, borderMax;
	
	public HScreen(IConstructor innerCons, IFigureApplet fpa, PropertyManager properties,  IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties);
		this.innerFig = FigureFactory.make(fpa, innerCons, this.properties, childProps, ctx);
		projections = new Vector<HScreen.ProjectionPlacement>();
	}
	
	public static class ProjectionPlacement{
		double xPos, yPos , originalXposition, originalYPosition;
		double gap;
		Figure fig;
		public ProjectionPlacement(double originalXposition, double originalYPosition, double gap, Figure fig) {
			this.originalXposition = originalXposition;
			this.originalYPosition = originalYPosition;
			this.gap = gap;
			this.fig = fig;
		}
	}
	
	@Override
	public void bbox(double desiredWidth, double desiredHeight) {
		innerFig.bbox(desiredWidth, desiredHeight);
		width = innerFig.width;
		height = innerFig.height;
		innerFigX = innerFigY = 0.0f;
		startGatheringProjections();
		double shiftX, shiftY;
		shiftX = shiftY = 0.0f;
		double oldWidth = width;
		double oldHeight = height;
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
		//System.out.printf("hscreen w %f h %f shiftX %f shiftY %f\n",innerFigX,innerFigY,shiftX,shiftY);
	}

	void setBorders(double shiftX,double shiftY, double oldWidth, double oldHeight) {
		borderMin = shiftY;
		borderMax = oldHeight + shiftY;
	}

	double addHorizontalSpacing(double shiftX, double oldWidth) {
		return shiftX;
	}
	
	double addVerticalSpacing(double shiftY, double oldHeight) {
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
	public void draw(double left, double top) {
		setLeft(left);
		setTop(top);
		innerFig.draw(left + innerFigX, top + innerFigY);
		for(ProjectionPlacement p : projections){
			p.fig.draw(left + p.xPos, top + p.yPos );
		}
		drawScreen(left, top);
	}

	void drawScreen(double left, double top) {
		//System.out.printf("Horizontal borders %f %f\n", innerFig.getHorizontalBorders().getMinimum(),innerFig.getHorizontalBorders().getMaximum() );
		if(properties.getBooleanProperty(BoolProp.DRAW_SCREEN_X)){
			fpa.line(left + innerFigX + innerFig.getHorizontalBorders().getMinimum(),
					top + innerFigY + getVAlignProperty() * innerFig.height,
					left + innerFigX +  innerFig.getHorizontalBorders().getMaximum(),
					top + innerFigY + getVAlignProperty() * innerFig.height);
		}
	}

	public void gatherProjections(double left, double top, Vector<HScreen.ProjectionPlacement> projections, boolean first, String screenId, boolean horizontal){
		innerFig.gatherProjections(left + innerFigX, top + innerFigY, projections, false, screenId, horizontal);
	}
	
	public Extremes getExtremesForAxis(String axisId, double offset, boolean horizontal){
		Extremes result = super.getExtremesForAxis(axisId, offset, horizontal);
		if(result.gotData()){
			return result;
		} else {
			return innerFig.getExtremesForAxis(axisId, offset, horizontal);
		}
	}
	
	public double getOffsetForAxis(String axisId, double offset, boolean horizontal){
		double result = super.getOffsetForAxis(axisId, offset, horizontal);
		if(result != Double.MAX_VALUE){
			return result;
		} else {
			double off = 0.0f;
			if(horizontal){
				off = innerFigX;
			} else {
				off = innerFigY;
			}
			return innerFig.getOffsetForAxis(axisId, offset + off, horizontal);
		}
	}
	

	public void propagateScaling(double scaleX,double scaleY, HashMap<String,Double> axisScales){
		super.propagateScaling(scaleX, scaleY, axisScales);
		innerFig.propagateScaling(scaleX, scaleY, axisScales);
	}
	

	
	public Extremes getVerticalBorders(){
		return new Extremes(borderMin,borderMax);
	}
	
	
}
