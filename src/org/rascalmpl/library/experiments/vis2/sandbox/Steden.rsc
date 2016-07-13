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
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Render;

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
   
// data DDD = ddd(str name="", int size = 0, list[DDD] children = [], int width =10, int height = 10);
   
public DDD dddSteden(int pos) {
   lrel[str name , int p2013, int p2012 , int ext] v = 
      readCSV(#lrel[str name, int p2013, int p2012, int ext], location, header=true)
        // bool(tuple[str name, int v1, int v2, int v3]  a,  tuple[str name, int v1, int v2, int v3] b){ return a.v3 < b.v3; }
      ;  
      list[DDD] r = [ddd(name=d.name, size=z)|d<-v, int z:=d[pos]];
      return ddd( children = r);
   }
   
public Figure steden(int width = 400, int height = 400, bool tooltip = false) {  
            Figure f = comboChart(fillColor="antiquewhite", charts = exampleSteden(), tickLabels = true,  tooltipColumn = 2, 
           	    options = chartOptions(
           		hAxis = axis(title="Extend", slantedText = true, slantedTextAngle=90), 
           		vAxis = axis(title="Population"),
           		chartArea_ = chartArea(width="80%", height = "40%"),
           		bar_ = bar(groupWidth = "100%"),
           		width=width,
                height=height,
                // animation= animation(startup = true, easing = "in", duration = 500),
                legend_ = legend(position="top")), width = width, height = height);
            // println("comboChart <f.width> <f.height>");
             // f.width = width; f.height = height;
             return f;          
   }
   
 public Figure steden2(bool tooltip= false) {return 
                          hcat(hgap=5, figs=[
                          vcat(
                          figs=[box(fig=text("Population"), grow=1.2, fillColor="antiquewhite")
                               ,d3Treemap(d = dddSteden(1), size=<200, 200>, fillColor="lightskyblue", inTooltip = tooltip)
                               ]),
                          vcat(
                          figs=[box(fig=text("Extend"), grow=1.2, fillColor="antiquewhite")
                               ,d3Treemap(d = dddSteden(3), size=<200, 200>, fillColor="lightpink", inTooltip = tooltip)
                               ])
                          ]);
                         }
                         
public Figure steden3(bool tooltip=false) {return 
                          hcat(hgap=5, figs=[
                          vcat(
                          figs=[box(fig=text("Population"), grow=1.2, fillColor="antiquewhite")
                               ,d3Pack(d = dddSteden(1), size=<200, 200>, fillLeaf="lightskyblue", diameter = 200, inTooltip = tooltip)
                               ]),
                          vcat(
                          figs=[box(fig=text("Extend"), grow=1.2, fillColor="antiquewhite")
                               ,d3Pack(d = dddSteden(3), size=<200, 200>, fillLeaf="lightpink", diameter = 200, inTooltip = tooltip)
                               ])
                          ]);
                         }
   
 public void tsteden() {
       render(box(size=<50, 50>, fillColor="yellow", tooltip=atXY(150, 150, box(lineWidth = 1, fillColor="antiquewhite", fig=steden()))));             
   }
   
 public void fsteden(loc l) = writeFile(l, toHtmlString(
      box(size=<50, 50>, fillColor="yellow", tooltip=frame(atXY(150, 150, box(lineWidth = 1, fig=steden()))))
   )); 
   
 public void tsteden2() {
       render(steden2());             
   }
   
  public void tsteden3() {
       render(steden3());             
   }
   
public void fsteden2(loc l) = writeFile(l, toHtmlString(
   steden2()
   )); 
   
   