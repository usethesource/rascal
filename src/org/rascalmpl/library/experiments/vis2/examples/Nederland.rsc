@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module experiments::vis2::examples::Nederland

import experiments::vis2::Figure;
import experiments::vis2::FigureServer; 

import Prelude;
import util::Math;
import lang::csv::IO;

void ex(str title, Figure f){
	render(title, f);
}

loc location = |project://rascal/src/org/rascalmpl/library/experiments/vis2/data/Nederland.csv|;


alias Record = tuple[str range, 
         num p1900, num p1910,  num p1920, num p1930, num p1940,
         num p1950, num p1960, num p1970, num p1980, num p1990, num p2000,
         num p2010];

alias Records = list[Record];
         
list[str] d = ["", "1900", "1910", "1920", "1930", "1940", "1950",
                   "1960", "1970", "1980", "1990", "2000", "2010"];
   
public list[Chart] exampleNederland() {
   Records v = readCSV(#Records, location, header=true);
   map[str, Record] m =(t[0]:t| t<-v);
   list[str] ks = [t[0]|t<-v];
   list[Chart] r = [];
   for (k<-ks) {
     lrel[int, num , str]  z = [<i, m[k][i], d[i]> | i<-[1..12]];
     r = r + [bar(z, name = "<k>")];
     }
   return r;
   }
   
public XYLabeledData exampleNederland(int idx) {
   Records v = readCSV(#Records, location, header=true);
   map[str, Record] m =(t[0]:t| t<-v);
   list[str] ks = [t[0]|t<-v];
   lrel[num , str]  z = [<m[k][idx], k> | k<-ks];
   int i = 0; XYLabeledData r = [];
   for (x<-z) {
         r += [<i>+x];
         i = i +1;
       }
   return r;
   }

public void nederland() {   
    // println(exampleNederland());
   	//ex("Nederland", combo(charts = exampleNederland(), options = chartOptions(
    //       		hAxis = axis(title="Year", slantedText = true, slantedTextAngle=90), 
    //       		vAxis = axis(title="Population"),
    //       		chartArea = chartArea(width="80%", height = "40%", backgroundColor="antiquewhite"),
    //       		bar = bar(groupWidth = "80%"),
    //       		width=600,
    //            height=400,
    //            legend = legend(position="top"),
    // 
    //           isStacked = true))) ; 
    ChartOptions options(int k) = chartOptions(
                title = d[k]
           		,width=300
                , height=200
                );
                
    Figures fs = [piechart(exampleNederland(3), options = options(3)),
                  piechart(exampleNederland(4), options = options(4))
                 ];
    Figure f = hcat(figs = fs, align = top);
    ex("Nederland", f) ;  
                 
   }