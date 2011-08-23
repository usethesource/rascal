package org.rascalmpl.library.vis.util.vector;

public class ConnectionPoint {
	
	Coordinate location;
	double angle;
	
	ConnectionPoint(double x, double y , double angle){
		location = new Coordinate(x,y);
		this.angle = angle;
	}
	
	public Coordinate getLocation(){
		return location;
	}
	
	public double getAngle(){
		return angle;
	}

}
