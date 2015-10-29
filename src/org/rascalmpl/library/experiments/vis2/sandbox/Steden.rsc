@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module experiments::vis2::sandbox::Steden

import Prelude;
import lang::csv::IO;
import experiments::vis2::sandbox::Figure;


loc location = |project://rascal/src/org/rascalmpl/library/experiments/vis2/data/Steden.csv|;

public list[Chart] exampleSteden() {
   lrel[str name , int p2013, int p2012 , int ext] v = 
      readCSV(#lrel[str name, int p2013, int p2012, int ext], location, header=true)
        // bool(tuple[str name, int v1, int v2, int v3]  a,  tuple[str name, int v1, int v2, int v3] b){ return a.v3 < b.v3; }
      ;  
   int i = 0;
   lrel [int, str, int, int, int] r = [];
   for (t<-v) {
       r+= [<t.ext>+t];
       i = i + 1;
       }
   return [bar ([<d[0], d[2], d[1]>|d<-r], name = "2013"), 
           bar ([<d[0], d[3], d[1]>|d<-r], name = "2012")];
   }
   
public Figure steden(int width = 400, int height = 400) {  
            Figure f = combochart(charts = exampleSteden(), tickLabels = true,  tooltipColumn = 2, 
           	    options = chartOptions(
           		hAxis = axis(title="Extend", slantedText = true, slantedTextAngle=90), 
           		vAxis = axis(title="Population"),
           		chartArea = chartArea(width="80%", height = "40%", backgroundColor="antiquewhite"),
           		bar = bar(groupWidth = "100%"),
           		width=width,
                height=height,
                animation= animation(startup = true, easing = "in", duration = 500),
                legend = legend(position="top")));
             return f;          
   }