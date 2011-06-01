package org.rascalmpl.library.vis.containers;

import java.util.Vector;

import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Coordinate;

public class HScreen extends WithInnerFig {
	
	Vector<ProjectionPlacement> projections; 
	double borderMin, borderMax;
	
	public HScreen(IFigureApplet fpa, Figure inner, PropertyManager properties) {
		super(fpa,inner,properties);
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
	public void bbox() {
		innerFig.bbox();
		minSize.setWidth(innerFig.minSize.getWidth());
		minSize.setHeight(innerFig.minSize.getHeight());
		innerFigLocation.clear();
		startGatheringProjections();
		double shiftX, shiftY;
		shiftX = shiftY = 0.0f;
		double oldWidth = minSize.getWidth();
		double oldHeight = minSize.getHeight();
		for(ProjectionPlacement p : projections){
			p.fig.bbox();
			placeProjection(p);
			shiftX = Math.max(shiftX,-p.xPos);
			shiftY = Math.max(shiftY,-p.yPos);
			minSize.setWidth(Math.max(minSize.getWidth(),p.xPos + p.fig.minSize.getWidth()));
			minSize.setHeight(Math.max(minSize.getHeight(),p.yPos + p.fig.minSize.getHeight()));
		}
		shiftX = addHorizontalSpacing(shiftX, oldWidth);
		shiftY = addVerticalSpacing(shiftY,oldHeight);
		setBorders(shiftX,shiftY, oldWidth, oldHeight);
		minSize.setWidth(minSize.getWidth() + shiftX);
		minSize.setHeight(minSize.getHeight() + shiftY);
		for(ProjectionPlacement p : projections){
			p.xPos+=shiftX;
			p.yPos+=shiftY;
		}
		innerFigLocation.setX(shiftX);
		innerFigLocation.setY(shiftY);
		//System.out.printf("hscreen w %f h %f shiftX %f shiftY %f\n",innerFigX,innerFigY,shiftX,shiftY);
		setNonResizable();
		super.bbox();
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
		if(minSize.getHeight() > oldHeight){
			minSize.setHeight(minSize.getHeight() + getHGapProperty());
		}
		return shiftY;
	}

	void startGatheringProjections() {
		innerFig.gatherProjections(0.0f, 0.0f, projections, true, getIdProperty(), true);
	}

	void placeProjection(ProjectionPlacement p) {
			p.xPos = p.originalXposition - p.fig.leftAlign();
			boolean gapAbove = p.originalYPosition >=  getVAlignProperty() * innerFig.minSize.getHeight();
			p.yPos = getVAlignProperty() * innerFig.minSize.getHeight() +  (gapAbove ? -p.gap : p.gap) - p.fig.topAlign();
	}

	@Override
	public void draw(double left, double top) {
		setLeft(left);
		setTop(top);
		innerFig.draw(left + innerFigLocation.getX(), top + innerFigLocation.getY());
		for(ProjectionPlacement p : projections){
			p.fig.draw(left + p.xPos, top + p.yPos );
		}
		drawScreen(left, top);
	}

	void drawScreen(double left, double top) {
		//System.out.printf("Horizontal borders %f %f\n", innerFig.getHorizontalBorders().getMinimum(),innerFig.getHorizontalBorders().getMaximum() );
		if(properties.getBooleanProperty(Properties.DRAW_SCREEN_X)){
			fpa.line(left + innerFigLocation.getX() + innerFig.getHorizontalBorders().getMinimum(),
					top + innerFigLocation.getY() + getVAlignProperty() * innerFig.minSize.getHeight(),
					left + innerFigLocation.getX() +  innerFig.getHorizontalBorders().getMaximum(),
					top + innerFigLocation.getY() + getVAlignProperty() * innerFig.minSize.getHeight());
		}
	}
	
	public Extremes getVerticalBorders(){
		return new Extremes(borderMin,borderMax);
	}
	
	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		if(!mouseInside(c.getX(), c.getY())) return false;

		if(innerFig.getFiguresUnderMouse(c, result)) return true;
		for(ProjectionPlacement pfig : projections){
			if(pfig.fig.getFiguresUnderMouse(c, result)){
				break;
			}
		}
		result.add(this);
		return true;
	}
	
	
}
