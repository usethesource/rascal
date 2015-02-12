@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module experiments::vis2::examples::RegEq
import Prelude;
import util::Math;
import analysis::statistics::SimpleRegression;
import experiments::vis2::FigureServer;
import experiments::vis2::Figure; 


void ex(str title, Figure f){
	render(title, f);
}

lrel[num, num] pf(list[num] ticks, num(num) g) {
   return [<x, g(x)>|x<-ticks];
   }
   
int n = 100;

XYData getReg(lrel[num, num] v) {
      num c = intercept(v);
      num a = slope(v);
      num q(num x) = c + a*x;
      return pf([0,10..n], q);
      }
      
XYData points = [<floor(n*arbReal()), floor(n*arbReal())>|int i<-[0..n]];

                            
list[Chart] getData() {
     return [line(points, name = "points", lineWidth = 0, pointSize = 3),
      line(getReg(points), name = "regression line")
      ];
     }
    
public void regEq() {  
     // println(getData());
     ex("RegressionLine", combo(charts = getData(), 
          options = chartOptions(
           		hAxis = axis(title="X"), 
           		vAxis = axis(title="Y"),
           		width= 1000,
                height= 400,
                lineWidth = 1,
                pointSize = 0,
                interpolateNulls = true,
                legend = legend(position="top"))
           )
       ) ;           
   }
      

      
