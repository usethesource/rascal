module experiments::Processing::piechart

import experiments::Processing::Core;

public void pieChartSetup() {
	//size(200, 200);
	background(100);
	smooth();
	noStroke();
	noLoop();
}

real diameter = 150.0;
list[int] angs = [30, 10, 45, 35, 60, 38, 75, 67];
real lastAng = 0.0;

public void pieChartDraw(){
	for (int ang <- angs){
 		fill(ang * 3.0);
  		arc(width()/2.0, height()/2.0, diameter, diameter, lastAng, lastAng+radians(ang));
  		lastAng += radians(ang);  
	}
}

public void main(){
	P = processing(draw(pieChartDraw), setup(pieChartSetup));
}
