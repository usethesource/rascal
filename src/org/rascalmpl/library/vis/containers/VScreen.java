package org.rascalmpl.library.vis.containers;

import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class VScreen extends HScreen {
	
	public VScreen(IFigureApplet fpa, Figure inner,
			PropertyManager properties) {
		super(fpa, inner, properties);
	}

	void startGatheringProjections() {
		innerFig.gatherProjections(0.0f, 0.0f, projections, true, getIdProperty(), false);
	}
	
	void placeProjection(ProjectionPlacement p) {
			boolean gapLeft = p.originalXposition >= getHAlignProperty() * innerFig.minSize.getWidth();
			p.xPos = getHAlignProperty() * innerFig.minSize.getWidth() +  (gapLeft ? -p.gap : p.gap) - p.fig.leftAlign();
			p.yPos = p.originalYPosition - p.fig.topAlign();
	}
	
	void drawScreen(float left, float top) {
		System.out.printf("drawing vscreen %f %f %s",  innerFig.getVerticalBorders().getMinimum(),  innerFig.getVerticalBorders().getMaximum(),innerFig);
		if(properties.getBooleanProperty(Properties.DRAW_SCREEN_Y)){
			fpa.line(left + innerFigLocation.getX() + getHAlignProperty() * innerFig.minSize.getWidth(),
					top + innerFigLocation.getY() + innerFig.getVerticalBorders().getMinimum() ,
					left + innerFigLocation.getX() + getHAlignProperty() * innerFig.minSize.getWidth(),
					top + innerFigLocation.getY() + innerFig.getVerticalBorders().getMaximum() );
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
		if(minSize.getWidth() > oldWidth){
			minSize.setWidth(minSize.getWidth() + getHGapProperty());
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
		return new Extremes(0,minSize.getHeight());
	}
	
	
}
