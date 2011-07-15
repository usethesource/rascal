@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::examples::Shape

import vis::Figure;
import vis::Render;
import Number;

import List;
import Set;
import IO;

// Shape: line graph with circle on each point
/*
public void s1(int s){
    dt1 = [10, 20, 10, 30];
	b = shape([ vertex(i * 50, 10 * dt1[i], ellipse(size(s), lineWidth(0), fillColor("red"))) | int i <- [0 .. size(dt1) -1]],
                lineColor("blue"),
                lineWidth(2),
                shapeConnected(true),
	            fillColor("lightgreen")
               );
    render(b);
}

public void s2(int s){
    dt1 = [10, 20, 0, 30];
	b = shape( [ vertex(i * 50, 10 * dt1[i], ellipse(size(s), lineWidth(0), fillColor("red"))) | int i <- [0 .. size(dt1) -1]],
                lineColor("blue"),
                lineWidth(2),
                shapeConnected(true), shapeClosed(true),
	            fillColor("lightgreen")
               );
    render(overlay([b, box(left(), bottom(), size(20))]));
}



// Shape: curved (fitted) graph with circle on each point

public void s3(int s){
    dt1 = [10, 20, 10, 30];
	b = shape([ vertex(i * 50, 10 * dt1[i], ellipse(size(s), lineWidth(0), fillColor("lightblue"))) | int i <- [0 .. size(dt1) -1]],
                lineColor("blue"),
                lineWidth(2),
	            fillColor("lightgreen"),
	            shapeConnected(true),
	            shapeCurved(true)
               );
    render(b);
}

// Two overlayed shapes with closed and curved graphs
public void s4(){
    dt1 = [10, 20, 10, 30];
    dt2 = [15, 10, 25, 20];
	sh1 = shape([ vertex(i * 50, 10 * dt1[i], ellipse(size(10), lineWidth(0), fillColor("white"))) | int i <- [0 .. size(dt1) -1]],
                lineColor("blue"),
                lineWidth(2),
	            fillColor(color("lightblue", 0.5)),
	            shapeCurved(true), shapeClosed(true)               
               );
    sh2 = shape([ vertex(i * 50, 10 * dt2[i], ellipse(size(10), lineWidth(0), fillColor("black"))) | int i <- [0 .. size(dt2) -1]],
                lineColor("green"),
                lineWidth(2),
	            fillColor(color("lightgreen", 0.5)),
	            shapeCurved(true), shapeClosed(true)
               );
    render(overlay([sh1, sh2]));
}
*/
