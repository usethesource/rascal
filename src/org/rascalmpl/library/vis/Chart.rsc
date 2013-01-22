@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module vis::Chart

import vis::Figure;
import vis::Render;
import Map;
import IO;
import Number;
import List;

// Settings for the various chart types (not all implemented yet)

data ChartProperty =           //             supported by
                               // barChart pieChart xyChart
     areaPlot()                //                     x
                               
   | chartSize(num w, num h)   //    x         x      x    
   | curvePlot ()              //                     x 

   | horizontal()              //    x                x   
   | linePlot()                //                     x
   | ring(num h)               //              x 
   | stackedBars()             //    x  
   | subTitle(str txt)         //    x         x      x
   | vertical()                //    x                x 
   | xLabel(str txt)           //    x                x
   | yLabel(str txt)           //    x                x
   ;
 
private num chartWidth = 400;
private num chartHeight = 400;
private str subTitle = "";
private str xTitle = "";
private str yTitle = "";
private bool isAreaPlot = false;
private bool isCurvePlot = false;
private bool isLinePlot = false;
private bool isStackedBars = false;
private bool isVertical = true;
private num ringHeight = 0;

private int titleFontSize = 20;
private int subTitleFontSize = 14;
private int axisFontSize = 10;
private str rasterColor = "lightgray";

// read and apply all settings for a chart

private void applySettings(list[ChartProperty] settings){
   chartWidth = 400;
   chartHeight = 400;
   ringHeight = 0;
   subTitle = "";
   xTitle = "";
   yTitle = "";
   isAreaPlot = false;
   isLinePlot = false;
   isCurvePlot = false;
   isStackedBars = false;
   isVertical = true;
   
   for(ChartProperty setting <- settings){
        
       switch(setting){
       
         case areaPlot(): isAreaPlot = true;
   	     case chartSize(num w, num h): { chartWidth = w; chartHeight = h;}
   	     case curvePlot(): isCurvePlot = true;
   	     case horizontal(): isVertical = false;
   	     case linePlot(): isLinePlot = true;
   	     case ring(num h): ringHeight = h;
   	     case stackedBars() : isStackedBars = true;
         case subTitle(str s): subTitle = s;
         case vertical(): isVertical = true;
         case xLabel(str s): xTitle = s;
         case yLabel(str s): yTitle = s;
       }
    }
}

// Background raster with title and subtitle
  
private Figure raster(str title){
   return vcat( [ text(title, fontSize(titleFontSize)),
                  (subTitle == "") ? space(size(0,-20)) : Ftext(subTitle, fontSize(subTitleFontSize)),
                  box(size(chartWidth,chartHeight), fillColor(rasterColor))
                ],
                hcenter(), gap(0,20)
              );
}

// Draw: |
//       n
// for x-axis

private Figure xtick(num n){
  return vcat( [ box(size(1,10), lineWidth(0)), 
                 text("<n>", fontSize(axisFontSize))
               ],
               gap(2), left()
             );
}

// Draw: n --
// for y-axis

private Figure ytick(num n){
  return hcat( [ text("<n>", fontSize(axisFontSize)), 
                 box(size(10,1), lineWidth(0))
               ],
               gap(2), bottom()
             );
}

// X-axis

public Figure xaxis(str title, num length, num \start, num incr, num end, num scale){
   ticks = grid( [ xtick(n) | num n <- [\start, (\start + incr) .. end+1]],
                 gap(incr * scale), width(length), vcenter() 
               );
   
   return vcat( [ ticks,
                  text(title, fontSize(subTitleFontSize))
                ],
                gap(20), hcenter()
              );
}

// Y-axis

public Figure yaxis(str title, num length, num \start, num incr, num end, num scale){

   ticks = grid( [ ytick(n) | num n <- [end, (end - incr) .. \start-1]],
                 gap(incr * scale), width(1), right()
               );
   
   
   return hcat( [ text(title, fontSize(subTitleFontSize), textAngle(-90)),
                  ticks
                ],
                gap(20), vcenter()
              );
}

// One item (name + colored box) in legend

private Figure legendItem(str name, Color c){
  return hcat( [ text("<name> = ", fontSize(10)), 
                 box(size(20,10), lineWidth(0), fillColor(c))
               ],
               gap(2), vcenter()
             );
}

// A complete legend

private Figure legend(list[tuple[str, Color]] funColors, num w){
   return box( hvcat( [legendItem(name, col) | <name, col> <- funColors],
                      width(w), gap(10), center()
                    ),
               center(), gap(10,10), fillColor("lightgray")
             );
}

// Data for xyChart

public alias NamedPairSeries  = 
       tuple[str name,list[tuple[num xval, num yval]]  values];

public Figure xyChart(str title, list[NamedPairSeries] facts, ChartProperty settings ... ){

   applySettings(settings);
   
   funPlots = [];
   funColors = [];
   xmin = 1000000;
   xmax = -1000000;
   
   ymin = 1000000;
   ymax = -1000000;
   
   // Max and min values in the data
   for(<str fname, list[tuple[num, num]] values> <- facts){
      for(<num x, num y> <- values){
          xmin = min(x, xmin);
          xmax = max(x, xmax);
          ymin = min(y, ymin);
          ymax = max(y, ymax);
      }
   }
    
  // Compute scaling
  xscale = chartWidth  / (xmax - xmin);
  yscale = chartHeight / (ymax - ymin);
  
  // Compute translation
  xshift = (xmin > 0) ? 0 : -xmin;
  yshift = (ymin > 0) ? 0 : -ymin;
  
  //println("xmin=<xmin>, xmax=<xmax>\nymin=<ymin>, ymax=<ymax>");
  //println("xscale=<xscale>, yscale=<yscale>, xshift=<xshift>, yshift=<yshift>");
  
  // Add vertical axis at x=0
  funPlots += shape( [ vertex((xshift + 0) * xscale, 0),
                       vertex((xshift + 0) * xscale, chartHeight)
                     ],
                     lineColor("darkgrey"), lineWidth(1), shapeConnected(true)
                   );
  // Add horizontal axis at y=0
  funPlots+= shape( [ vertex(0,          (yshift + 0) * yscale),
                      vertex(chartWidth, (xshift + 0) * yscale)
                    ],
                    lineColor("darkgrey"), lineWidth(1), shapeConnected(true)
                  );  
  // Add function plots                            
   for(<str fname, list[tuple[num, num]] values> <- facts){
   		fcolorName = palette(size(funColors));
   		funColors += <fname, color(fcolorName)>;
   		list[FProperty] shapeProps = [lineColor(fcolorName), lineWidth(2)];
   		
   		if(isAreaPlot)
   		   shapeProps += [fillColor(color(fcolorName, 0.7)), shapeClosed(true), shapeConnected(true)];
   		else
   		   shapeProps += [fillColor(fcolorName)];
   		   
   		if(isCurvePlot)
   		   shapeProps += [shapeCurved(true), shapeConnected(true)];
   		   
   		if(isLinePlot)
   		   shapeProps += [shapeConnected(true)];
   		   
        funPlots += shape( [ vertex((xshift + x) * xscale, (yshift + y) * yscale, ellipse(size(5), fillColor(fcolorName), lineWidth(0))) | <num x, num y> <- values],
                           shapeProps
                         );
   }
           
   // Superimpose on the same grid point (with different allignments):
   // - x-axis,
   // - y-axis
   // - raster
   // - function plots
   
   plot = overlay(
               [ use( yaxis(yTitle, chartHeight, ymin, 10, ymax, yscale),
                      bottom(), right()
                    ),
               
               
                 use( vcat( [ xaxis(xTitle, chartWidth,  xmin, 10, xmax, xscale),
                              legend(funColors, chartWidth)
                            ],
                            hcenter(), gap(20)
                          ),
                      top(), left()
                     ),      
                 use(raster(title), bottom(), left()),
                 funPlots
               ]);
   
   return plot;
}


//----------------------------------  barChart -----------------------------------------------

// Data for barchart

public alias NamedNumbers  = 
       list[tuple[str name, num val] values];
       
public alias NamedNumberSeries =
       list[tuple[str name, list[num] values]];

public Figure barChart(str title, map[str,num] facts, ChartProperty settings...){
  categories = [];
  ifacts = [];
  for(k <- facts){
  	categories += [k];
  	v = facts[k];
  	ifacts += <k, [v]>;
  }
  //println("categories=<categories>\nifacts=<ifacts>");
  return barChart(title, categories, ifacts, settings);
}

public Figure barChart(str title, NamedNumbers facts, ChartProperty settings...){
  categories = [];
  ifacts = [];
  for(<k, v> <- facts){
  	categories += [k];
  	ifacts += <k, [v]>;
  }
  //println("categories=<categories>\nifacts=<ifacts>");
  return barChart(title, categories, ifacts, settings);
}

public Figure barChart(str title, list[str] categories, NamedNumberSeries facts, ChartProperty settings...){
   
   applySettings(settings);
 
   funPlots = [];
   funColors = [];
   funColorsMap = ();
   
   nbars = 0;
   nseries = size(facts);
   
   ymin = 1000000;
   ymax = -1000000;
   ysummax = -1000000;
   
   // Max and min values in the data
   for(<str fname, list[num] values> <- facts){
      n = size(values);
      if(n > nbars)
          nbars = n;
      ysum = 0;
      for(num y <- values){
          ysum += y;
          ymin = min(y, ymin);
          ymax = max(y, ymax);
      }
      ysummax = max(ysum, ysummax);
  }
  
  // Compute scaling and sizes
  
  yscale = chartHeight / (isStackedBars ? ysummax : ymax);
  groupWidth = 2 * chartWidth / (3 * nseries +1); 
  groupGap = groupWidth / 2;   
  
  barWidth = 2 * groupWidth / (3 * nbars - 1);
  barGap = barWidth / 2;                       
  
  if(isStackedBars){
     barWidth = groupWidth;
     barGap = 0;
  }
  
  //println("barWidth=<barWidth>, barGap=<barGap>, groupWidth=<groupWidth>, groupGap=<groupGap>");
  
  // Compute translation

  //println("yscale=<yscale>");
  fns = ();
  for(<str fname, list[num] values> <- facts){
    fcolorName = palette(size(funColors));
    funColors += <fname, color(fcolorName)>;
    funColorsMap[fname] = color(fcolorName);
    for(int i <- [0 .. size(values)]){
        num bw = barWidth;
        num bh = values[i] * yscale;
        if(!isVertical)
           <bw, bh> = <bh, bw>;
     	fns[i] = (fns[i] ? []) + box(size(bw, bh), lineWidth(0), fillColor(funColorsMap[fname]));
     }
  }
  for(num i <- [0 .. size(categories)]){
    if(fns[i]?)
  	   funPlots += isStackedBars ? (isVertical ? vcat(reverse(fns[i]), bottom(), gap(0))
  	                                           : hcat(fns[i], bottom(), gap(0)))
  	                             : (isVertical ? hcat(fns[i], left(), hcenter(), gap(barGap))
  	                                           : vcat(fns[i], bottom(), left(), gap(barGap)));
  }
  
  if(isVertical)
 
 	return grid([ use( yaxis(yTitle, chartHeight, 0, 10, isStackedBars ? ysummax : ymax, yscale),
 	                   bottom(), right()
 	                 ),
                                                       
                  use( raster(title),
                       bottom(), left() 
                     ),
                 
                  hcat([ space(size(groupGap,20)), 
                         grid(funPlots,
                              bottom(), width(chartWidth), gap(groupWidth + groupGap)
                             )
                       ]),
                 
                  hcat([ space(size(groupGap,20)), 
                         grid( [ space(text(cat, hcenter(), fontSize(axisFontSize)), size(groupWidth,20), gap(2)) | cat <- categories],
                               top(), width(chartWidth), gap(groupWidth + groupGap) 
                             )
                       ],
                       top()             
                      ),
              
                  use( vcat( [ space(size(chartWidth, 20)),
                               text(xTitle, fontSize(subTitleFontSize)),
                               legend(funColors, chartWidth)
                             ],
                             gap(20), hcenter()            
                           ),
                       top(), left()                       
                     )
                ],
                bottom(), left(), gap(0)
               );
   else
   
   	return grid([ use(xaxis(yTitle, chartHeight, 0, 10, isStackedBars ? ysummax : ymax, yscale),
   	                  top(), left()
   	                 ),
                                                       
                  use(raster(title),
                      bottom(), left()
                     ),
                 
                  vcat([ grid(funPlots,
                              bottom(), width(100), gap(groupWidth + groupGap)
                             ),
                         space(size(20,groupGap))
                       ]),
                 
                  use(vcat([ grid([ space(text(cat, vcenter(), fontSize(axisFontSize), textAngle(-90)), size(20,groupWidth), gap(2)) | cat <- categories],
                                  bottom(), width(10), gap(groupWidth + groupGap)
                                 ), 
                             space(size(10,groupGap))                 
                           ],
                           right()        
                          ),
                       bottom(), right()                 
                      ),
              
                   use(vcat([ space(size(chartWidth, 40)),
                              legend(funColors, chartWidth)
                            ],
                            gap(20), hcenter()           
                           ),
                       top(), left()                    
                      ),
                                                
                   use(hcat([ text(xTitle, fontSize(subTitleFontSize), textAngle(-90)),
                              space(size(20, chartHeight))
                            ],
                            gap(20), vcenter()           
                           ),
                       bottom(), right()        
                      )
                ],
                bottom(), left(), gap(0)
               );
   
}



//-------------------------------- pieChart ------------------------------------

public Figure pieChart(str title, map[str, int] facts, ChartProperty settings...){

	applySettings(settings);
 	funColors = [];
 	funColorsMap = ();
 	elems = [];
 	radius = 3*chartWidth/7;
 	ir = (ringHeight == 0) ? 0 : radius - ringHeight;
 	real total = 0.0;
 	for(k <- facts)
 		total += facts[k];
 	
 	angle = 0.0;
 	for(fname <- facts){
 		fcolorName = palette(size(funColors));
 		funColors += <fname, color(fcolorName, 0.6)>;
   		funColorsMap[fname] = color(fcolorName, 0.6);
   		delta = facts[fname] * 360 / total;
    	elems += wedge(text("<facts[fname]>"),
    	               fromAngle(angle), toAngle(angle + delta),
					   height(radius), innerRadius(ir), fillColor(funColorsMap[fname])
					  );
	    angle += delta;
    }
 
    p = overlay(elems, lineWidth(0), lineColor(0));
    
    return vcat( [ text(title, fontSize(20)),
                   (subTitle == "") ? space(size(0,-20)) : text(subTitle, fontSize(10)),
                   overlay( [ box(size(chartWidth,chartHeight), fillColor("lightgray")),
                              p
                            ],
                            center()      
                          ),
                  legend(funColors, chartWidth)
                ],
                hcenter(), gap(20)
               );
}


/*
 * Data formats used in JFreeChart chart functions
 * (Temporary reminder for other chart functions)
 
// A named series of int or real values

public alias intSeries  = 
       tuple[str name,list[int]  values];
       
public alias realSeries = 
       tuple[str name,list[real] values];

// Some charts need multiple values instead of a single one and include
// a category name for each subseries.

public alias intSeriesMultipleData  = 
       tuple[str name,list[tuple[str category, list[int] values]] allvalues];
       
public alias realSeriesMultipleData = 
       tuple[str name,list[tuple[str category, list[real] values2]] allvalues];

// barchart

@doc{draw a bar chart}
@javaClass{org.rascalmpl.library.viz.BarChart}
public java void barChart(str title, map[str,int] facts, ChartProperty settings...);

@doc{draw a bar chart}
@javaClass{org.rascalmpl.library.viz.BarChart}
public java void barChart(str title, map[str,real] facts, ChartProperty settings...);

@doc{draw a bar chart}
@javaClass{org.rascalmpl.library.viz.BarChart}
public java void barChart(str title, list[str] categories, list[intSeries] facts, ChartProperty settings...);

@doc{draw a bar chart}
@javaClass{org.rascalmpl.library.viz.BarChart}
public java void barChart(str title, list[str] categories, list[realSeries] facts, ChartProperty settings...);

// boxPlot aka BoxAndWiskerPlot

@doc{draw a boxPlot}
@javaClass{org.rascalmpl.library.viz.BoxPlot}
public java void boxplot(str title, list[intSeriesMultipleData] facts, ChartProperty settings...);

@doc{draw a boxplot}
@javaClass{org.rascalmpl.library.viz.BoxPlot}
public java void boxplot(str title, list[realSeriesMultipleData] facts, ChartProperty settings...);

// histogram

@doc{draw a histogram}
@javaClass{org.rascalmpl.library.viz.Histogram}
public java void histogram(str title, list[intSeries] facts, int nbins, ChartProperty settings...);

@doc{draw a histogram}
@javaClass{org.rascalmpl.library.viz.Histogram}
public java void histogram(str title, list[realSeries] facts, int nbins, ChartProperty settings...);

// xyChart

@doc{draw an xy chart}
@javaClass{org.rascalmpl.library.viz.XYChart}
public java void xyChart(str title, list[intSeries] facts, ChartProperty settings...);

@doc{draw an xy chart}
@javaClass{org.rascalmpl.library.viz.XYChart}
public java void xyChart(str title, list[realSeries] facts, ChartProperty settings...);

***/
