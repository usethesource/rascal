@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module experiments::vis2::examples::SinAndCos
import experiments::vis2::FigureServer; 
import experiments::vis2::Figure; 
import util::Math;


public Figure box1 = box(fillColor="red", size=<200,200>);
void tbox1(){ ex("box1", box1); }  

void sinAndCos(){
        ex("sinAndCos", 
        	combochart(charts=[
        	       line([<x, round(sin(x/1),0.001)>     | x <- [0.0, 1.0 .. 10.0]], name="Sine Wave"),
        		   line([<x, round(0.5 * cos(x/1), 0.01), "a<x>"> | x <- [0.0, 1.0 .. 10.0]], name ="Cosine Wave",
        		       lineWidth = 0, pointSize = 3),
        		   line([<x, round(0.25 * sin(x/1) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 9.0]], name= "Another sine wave")
        			],
        	options = chartOptions(curveType="function",
           		hAxis = axis(title="Time", minValue = 0, maxValue = 10), 
           		vAxis = axis(title="Voltage"),
           		chartArea = chartArea(width="80%", height = "70%"
           		             ,backgroundColor="antiquewhite"
           		),
           		width=600,
                height=200,
                pointSize= 0,
                lineWidth = 1,
                legend = legend(position="top")
        	    )
           
        ));
}
 
public list[Chart] q = 
     [
     line([<x, round(sin(x/10),0.01)>               | x <- [0.0, 1.0 .. 100.0]], name="Sine Wave"),
     line([<x, round(0.5 * cos(x/10), 0.01)>        | x <- [0.0, 1.0 .. 100.0]], name ="Cosine Wave"),
     line([<x, round(0.25 * sin(x/10) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 100.0]], name= "Another sine wave")
     ];

void ex(str title, Figure f){
	render(title, f);
}