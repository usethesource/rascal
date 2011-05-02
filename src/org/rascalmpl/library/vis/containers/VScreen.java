package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.descriptions.BoolProp;

public class VScreen extends HScreen {
	
	public VScreen(IConstructor innerCons, IFigureApplet fpa,
			PropertyManager properties, IList childProps, IEvaluatorContext ctx) {
		super(innerCons, fpa, properties, childProps, ctx);
	}

	void startGatheringProjections() {
		innerFig.gatherProjections(0.0f, 0.0f, projections, true, getIdProperty(), false);
	}
	
	void placeProjection(ProjectionPlacement p) {
			boolean gapLeft = p.originalXposition >= getHAlignProperty() * innerFig.width;
			p.xPos = getHAlignProperty() * innerFig.width +  (gapLeft ? -p.gap : p.gap) - p.fig.leftAlign();
			p.yPos = p.originalYPosition - p.fig.topAlign();
	}
	
	void drawScreen(float left, float top) {
		System.out.printf("drawing vscreen %f %f %s",  innerFig.getVerticalBorders().getMinimum(),  innerFig.getVerticalBorders().getMaximum(),innerFig);
		if(properties.getBooleanProperty(BoolProp.DRAW_SCREEN_Y)){
			fpa.line(left + innerFigX + getHAlignProperty() * innerFig.width,
					top + innerFigY + innerFig.getVerticalBorders().getMinimum() ,
					left + innerFigX + getHAlignProperty() * innerFig.width,
					top + innerFigY + innerFig.getVerticalBorders().getMaximum() );
		}
	}
	
	void setBorders(float shiftX,float shiftY, float oldWidth, float oldHeight) {
		borderMin = shiftX;
		borderMax = oldWidth + shiftX;
	}
	
	float addHorizontalSpacing(float shiftX, float oldWidth) {
		if(shiftX > 0.0f){
			shiftX += getHGapProperty();
		} 
		if(width > oldWidth){
			width += getHGapProperty();
		}
		return shiftX;
	}
	
	float addVerticalSpacing(float shiftY, float oldHeight) {
		return shiftY;
	}
	
	public Extremes getHorizontalBorders(){
		return new Extremes(borderMin,borderMax);
	}
	
	public Extremes getVerticalBorders(){
		return new Extremes(0,height);
	}
	
	
}
