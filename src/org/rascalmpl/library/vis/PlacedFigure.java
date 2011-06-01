package org.rascalmpl.library.vis;

import org.rascalmpl.library.vis.util.Coordinate;

public class PlacedFigure {
	
	public Coordinate coordinate;
	public Coordinate offset;
	public Figure figure;
	
	PlacedFigure(Coordinate coordinate, Figure figure){
		this.coordinate = coordinate;
		this.figure = figure;
		offset = new Coordinate();
	}

}
