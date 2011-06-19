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
	boolean flip;
	double projectionsHeight;
	boolean bottom;
	
	public HScreen(boolean flip, boolean bottom, IFigureApplet fpa, Figure inner, PropertyManager properties) {
		super(fpa,inner,properties);
		projections = new Vector<HScreen.ProjectionPlacement>();
		this.flip = flip;
		projectionsHeight = 0;
		this.bottom = bottom;
	}
	
	static class ProjectionPlacement{
		Projection projection;
		Coordinate location;
		public ProjectionPlacement(Projection projection) {
			this.projection = projection;
			location = new Coordinate();
		}
	}
	
	public void init(){
		super.init();
		projections.clear();
		
	}
	
	void registerProjection(Projection projection){
		projections.add(new ProjectionPlacement(projection));
	}
	
	public boolean isVertical(){
		return flip;
	}
	
	@Override
	public void bbox() {
		innerFig.bbox();
		projectionsHeight = 0;
		
		for(ProjectionPlacement p : projections){
			Figure actualProjection = p.projection.projection;
			projectionsHeight = Math.max(projectionsHeight,
					actualProjection.minSize.getHeight(flip) / actualProjection.getVShrinkProperty(flip));
		}
		if(getVGrowProperty(flip)!= 1.0){
			minSize.setHeight(flip,Math.max(innerFig.minSize.getHeight(flip) * getVGrowProperty(flip),
					projectionsHeight / ( getVGrowProperty(flip)-1.0)));
		} else {
			minSize.setHeight(innerFig.minSize.getHeight(flip) + projectionsHeight + getLineWidthProperty());
		}
		minSize.setWidth(flip, innerFig.minSize.getWidth(flip));
		for(boolean flip : BOTH_DIMENSIONS){
			setResizableX(flip, innerFig.getResizableX(flip));
		}
	}
	
	@Override
	public void layout(){
		
		if(getVGrowProperty(flip) != 1.0){
			innerFig.takeDesiredHeight(flip, size.getHeight(flip) / getVGrowProperty(flip));
		} else {
			innerFig.takeDesiredHeight(flip, size.getHeight(flip) - projectionsHeight + getLineWidthProperty());
		}
		innerFig.size.setWidth(flip, size.getWidth(flip));
		double screenTop, screenHeight;
		screenHeight = size.getHeight(flip) - innerFig.size.getHeight(flip);
		if(bottom){
			innerFigLocation.set(0,0);
			
			screenTop = innerFig.size.getHeight(flip);
		} else {
			screenTop = 0.0;
			innerFigLocation.set(flip,0,size.getHeight(flip) - innerFig.size.getHeight(flip)); 
		}
		for(boolean flip : BOTH_DIMENSIONS){
			innerFig.globalLocation.setX(flip, globalLocation.getX(flip) + innerFigLocation.getX(flip));
		}
		innerFig.layout();
		for(ProjectionPlacement p : projections){
			Figure projectFrom = p.projection.innerFig;
			Figure projection = p.projection.projection;
			//System.out.printf("Set innerfig %s %s\n",projectFrom.globalLocation.getX(flip),globalLocation.getX(flip));
			p.location.setX(flip, projectFrom.globalLocation.getX(flip) - globalLocation.getX(flip));
			p.location.setY(flip, screenTop);
			projection.takeDesiredWidth(flip, projectFrom.size.getWidth(flip) / projection.getHShrinkProperty(flip));
			projection.takeDesiredHeight(flip, screenHeight / projection.getVShrinkProperty(flip));
			//System.out.printf("Set innerfig %s %s\n",projectFrom.size.getWidth(flip) ,projection.size.getWidth(flip));
			p.location.addX(flip, (projectFrom.size.getWidth(flip) - projection.size.getWidth(flip)) * projection.getHAlignProperty(flip));
			p.location.addY(flip, (screenHeight - projection.size.getHeight(flip)) * projection.getVAlignProperty(flip));
			projection.layout();
		}
	}

	@Override
	public void draw(double left, double top) {
		setLeft(left);
		setTop(top);
		innerFig.draw(left + innerFigLocation.getX(), top + innerFigLocation.getY());
		for(ProjectionPlacement p : projections){
			p.projection.projection.draw(left + p.location.getX(), top +  p.location.getY() );
		}
		//drawScreen(left, top);
	}

	void drawScreen(double left, double top) {
		//System.out.printf("Horizontal borders %f %f\n", innerFig.getHorizontalBorders().getMinimum(),innerFig.getHorizontalBorders().getMaximum() );
			if(bottom){
				fpa.line(left + innerFigLocation.getX() , top + innerFigLocation.getY() + innerFig.size.getHeight(flip),
						 left + innerFigLocation.getX() + innerFig.size.getWidth() , top + innerFigLocation.getY() + innerFig.size.getHeight(flip));
			} else {
				fpa.line(left + innerFigLocation.getX() , top + innerFigLocation.getY(),
					 left + innerFigLocation.getX() + innerFig.size.getWidth() , top + innerFigLocation.getY());
			}
	}

	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		if(!mouseInside(c.getX(), c.getY())) return false;

		if(innerFig.getFiguresUnderMouse(c, result)) return true;
		for(ProjectionPlacement pfig : projections){
			if(pfig.projection.projection.getFiguresUnderMouse(c, result)){
				break;
			}
		}
		result.add(this);
		return true;
	}
	
	
}
