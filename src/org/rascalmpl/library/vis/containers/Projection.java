package org.rascalmpl.library.vis.containers;

import java.util.Vector;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class Projection extends WithInnerFig {

	String projectOn;
	Figure projection;
	
	public Projection(IFigureApplet fpa, String projectOn, Figure projection,Figure innerFigure,PropertyManager properties) {
		super(fpa,innerFigure,properties);
		this.projectOn = projectOn;
		this.projection = projection;
	}
	
	public void gatherProjections(double left, double top, Vector<HScreen.ProjectionPlacement> projections, boolean first, String screenId, boolean horizontal){
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
	public void bbox(double desiredWidth, double desiredHeight) {
		innerFig.bbox(desiredWidth, desiredHeight);
		this.width = innerFig.width;
		this.height = innerFig.height;
	}

	@Override
	public void draw(double left, double top) {
		innerFig.draw(left, top);
		
	}
	

	public double leftAlign() {
		return innerFig.leftAlign();
	}

	public double rightAlign() {
		return innerFig.rightAlign();
	}

	public double topAlign() {
		return innerFig.topAlign();
	}

	public double bottomAlign() {
		return innerFig.bottomAlign();
	}
	
}
