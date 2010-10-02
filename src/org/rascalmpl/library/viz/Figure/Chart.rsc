module viz::Figure::Chart

import viz::Figure::Core;
import viz::Figure::Render;
import Map;
import IO;
import Number;
import List;

// Settings for the various chart types (not all implemented yet)

data ChartSetting =            //             supported by
                               // barChart pieChart xyChart
     areaPlot()                //                     x
                               
   | chartSize(num w, num h)   //    x         x      x    
   | curvePlot ()              //                     x 

   | horizontal()              //    x                x   
   | linePlot()                //                     x
   | ring(num h)               //              x 
   | stackedBars()             //    x  
   | subtitle(str txt)         //    x         x      x
   | vertical()                //    x                x 
   | xLabel(str txt)           //    x                x
   | yLabel(str txt)           //    x                x
   ;
 
private num chartWidth = 400;
private num chartHeight = 400;
private str subtitle = "";
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

private void applySettings(list[ChartSetting] settings){
   chartWidth = chartHeight = 400;
   ringHeight = 0;
   subtitle = xTitle = yTitle = "";
   isAreaPlot = isLinePlot = isCurvePlot = isStackedBars = false;
   isVertical = true;
   
   for(ChartSetting setting <- settings){
        
       switch(setting){
       
         case areaPlot(): isAreaPlot = true;
   	     case chartSize(num w, num h): { chartWidth = w; chartHeight = h;}
   	     case curvePlot(): isCurvePlot = true;
   	     case horizontal(): isVertical = false;
   	     case linePlot(): isLinePlot = true;
   	     case ring(num h): ringHeight = h;
   	     case stackedBars() : isStackedBars = true;
         case subtitle(str s): subtitle = s;
         case vertical(): isVertical = true;
         case xLabel(str s): xTitle = s;
         case yLabel(str s): yTitle = s;
       }
    }
}

// Background raster with title and subtitle
  
private Figure raster(str title){
   return vcat([hcenter(), gap(0,20)],
                   [ text([fontSize(titleFontSize)], title),
                     (subtitle == "") ? space([size(0,-20)]) : text([fontSize(subTitleFontSize)], subtitle),
                     box([size(chartWidth,chartHeight), fillColor(rasterColor)])
                   ]);
}

// Draw: |
//       n
// for x-axis

private Figure xtick(num n){
  return vcat([gap(2), left()], [box([size(1,10), lineWidth(0)]), text([fontSize(axisFontSize)], "<n>")]);
}

// Draw: n --
// for y-axis

private Figure ytick(num n){
  return hcat([gap(2), bottom()], [text([fontSize(axisFontSize)], "<n>"), box([size(10,1), lineWidth(0)])]);
}



// X-axis

public Figure xaxis(str title, num length, num start, num incr, num end, num scale){
   ticks = grid([gap(incr * scale), width(length), vcenter()], [xtick(n) | num n <- [start, (start + incr) .. end]]);
   
   return vcat([gap(20), hcenter()], 
                   [ ticks,
                     text([fontSize(subTitleFontSize)], title)
                   ]);
}

// Y-axis

public Figure yaxis(str title, num length, num start, num incr, num end, num scale){

   ticks = grid([gap(incr * scale), width(1), right()], [ytick(n) | num n <- [end, (end - incr) .. start]]);
   return hcat([gap(20), vcenter()], 
                   [ text([fontSize(subTitleFontSize), textAngle(-90)], title),
                     ticks
                   ]);
}

// One item (name + colored box) in legend

private Figure legendItem(str name, Color c){
  return hcat([gap(2), vcenter()], [text([fontSize(10)], "<name> = "), box([size(20,10), lineWidth(0), fillColor(c)])]);
}

// A complete legend

private Figure legend(list[tuple[str, Color]] funColors, num w){
   return box([center(), gap(10,10), fillColor("lightgray")], 
               align([width(w), gap(10), center()], [legendItem(name, col) | <name, col> <- funColors]));
}

// Data for xyChart

public alias NamedPairSeries  = 
       tuple[str name,list[tuple[num xval, num yval]]  values];

public Figure xyChart(str title, list[NamedPairSeries] facts, ChartSetting settings ... ){

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
  funPlots += shape([lineColor("darkgrey"), lineWidth(1), connectedShape()],
                     [ vertex((xshift + 0) * xscale, 0),
                       vertex((xshift + 0) * xscale, chartHeight)
                     ]);
  // Add horizontal axis at y=0
  funPlots+= shape([lineColor("darkgrey"), lineWidth(1), connectedShape()],
                     [ vertex(0,          (yshift + 0) * yscale),
                       vertex(chartWidth, (xshift + 0) * yscale)
                     ]);  
  // Add function plots                            
   for(<str fname, list[tuple[num, num]] values> <- facts){
   		fcolorName = palette(size(funColors));
   		funColors += <fname, color(fcolorName)>;
   		list[FProperty] shapeProps = [lineColor(fcolorName), lineWidth(2)];
   		
   		if(isAreaPlot)
   		   shapeProps += [fillColor(color(fcolorName, 0.7)), closedShape(), connectedShape()];
   		else
   		   shapeProps += [fillColor(fcolorName)];
   		   
   		if(isCurvePlot)
   		   shapeProps += [curvedShape(), connectedShape()];
   		   
   		if(isLinePlot)
   		   shapeProps += [connectedShape()];
   		   
        funPlots += shape(shapeProps,
                          [vertex((xshift + x) * xscale, (yshift + y) * yscale, ellipse([size(5), fillColor(fcolorName), lineWidth(0)])) | <num x, num y> <- values]);
   }
           
   // Superimpose on the same grid point (with different allignments):
   // - x-axis,
   // - y-axis
   // - raster
   // - function plots
   
   plot = overlay(
               [ use([bottom(), right()], yaxis(yTitle, chartHeight, ymin, 10, ymax, yscale)),
                 use([top(), left()],     vcat([hcenter(), gap(20)],
                                               [ xaxis(xTitle, chartWidth,  xmin, 10, xmax, xscale),
                                                 legend(funColors, chartWidth)
                                               ])),      
                 use([bottom(), left()], raster(title)),
                 funPlots
               ]);
   
   return plot;
}

private list[NamedPairSeries] pdata =
        [ <"f", [<0, 50>, <10,50>, <20,50>, <30, 50>, <40, 50>, <50, 50>, <60,50>]>, 
          <"g", [<50,0>, <50,50>, <50,100>]>,
          <"h", [<0,0>, <10,10>, <20,20>, <30,30>, <40,40>, <50,50>, <60,60>]>,
          <"i", [<0, 60>, <10, 50>, <20, 40>, <30, 30>, <40, 20>, <50, 10>, <60, 0>]>,
          <"j", [< -20, 20>, < -10, 10>, <0,0>, <10, -10>, <20, -20>]>,
          <"k", [< -20, 40>, < -10, 10>, <0, 0>, <10, 10>, <20, 40>, <30, 90>]>                
        ];
        
public void p0(){
     mydata = [
               <"h", [<0,0>, <10,10>, <20,20>, <30,30>, <40,40>, <50,50>, <60,60>]>
               ];
     render(xyChart("P0", 
	                 mydata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis")
                  )
           );
}

// Scatter plot

public void p1(){
	render(xyChart("P1", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis")
                  )
           );
}

// Line plot

public void p2(){
	render(xyChart("P2", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis"),
	                 linePlot()
                  )
           );
}

// Curve plot

public void p3(){
	render(xyChart("P3", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis"),
	                 linePlot(), curvePlot()
                  )
           );
}

// Line/area plot

public void p4(){
	render(xyChart("Test Title P4", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis"),
	                 linePlot(), areaPlot()
                  )
           );
}

// Curve/area plot

public void p5(){
	render(xyChart("Test Title P5", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis"),
	                 linePlot(), curvePlot(), areaPlot()
                  )
           );
}

//----------------------------------  barChart -----------------------------------------------

// Data for barchart

public alias NamedNumbers  = 
       list[tuple[str name, num val] values];
       
public alias NamedNumberSeries =
       list[tuple[str name, list[num] values]];

// barChart with map argument

public Figure barChart(str title, NamedNumbers facts, ChartSetting settings...){
  categories = [];
  ifacts = [];
  for(<k, v> <- facts){
  	categories += [k];
  	ifacts += <k, [v]>;
  }
  //println("categories=<categories>\nifacts=<ifacts>");
  return barChart(title, categories, ifacts, settings);
}

public Figure barChart(str title, list[str] categories, NamedNumberSeries facts, ChartSetting settings...){
   
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
    for(int i <- [0 .. size(values)-1]){
        num bw = barWidth;
        num bh = values[i] * yscale;
        if(!isVertical)
           <bw, bh> = <bh, bw>;
     	fns[i] = (fns[i] ? []) + box([size(bw, bh), lineWidth(0), fillColor(funColorsMap[fname])]);
     }
  }
  for(num i <- [0 .. size(categories)-1]){
    if(fns[i]?)
  	   funPlots += isStackedBars ? (isVertical ? vcat([bottom(), gap(0)], reverse(fns[i]))
  	                                    : hcat([bottom(), gap(0)], fns[i]))
  	                             : (isVertical ? hcat([left(), hcenter(), gap(barGap)], fns[i])
  	                                    : vcat([bottom(), left(), gap(barGap)], fns[i]));
  }
  
  if(isVertical)
 
 	return grid([bottom(), left(), gap(0)],
                [ use([bottom(), right()], yaxis(yTitle, chartHeight, 0, 10, isStackedBars ? ysummax : ymax, yscale)),
                                                       
                  use([bottom(), left()], raster(title)),
                 
                  hcat([ space([size(groupGap,20)]), 
                               grid([bottom(), width(chartWidth), gap(groupWidth + groupGap)], funPlots)
                             ]),
                 
                  hcat([top()], [ space([size(groupGap,20)]), 
                                        grid([ top(), width(chartWidth), gap(groupWidth + groupGap)], 
                                             [ space([size(groupWidth,20), gap(2)], 
                                                     text([hcenter(), fontSize(axisFontSize)], cat)) | cat <- categories]
                                            )
                                       ]),
              
                  use([top(), left()], vcat([ gap(20), hcenter()],
                                                [ space([size(chartWidth, 20)]),
                                                  text([fontSize(subTitleFontSize)], xTitle),
                                                  legend(funColors, chartWidth)
                                                ]))
               ]);
   else
   
   	return grid([bottom(), left(), gap(0)],
                [ use([top(), left()], xaxis(yTitle, chartHeight, 0, 10, isStackedBars ? ysummax : ymax, yscale)),
                                                       
                  use([bottom(), left()], raster(title)),
                 
                  vcat([ 
                             grid([bottom(), width(100), gap(groupWidth + groupGap)], funPlots),
                             space([size(20,groupGap)])
                           ]),
                 
                 use([bottom(), right()], 
                      vcat([right()],[ grid([bottom(), width(10), gap(groupWidth + groupGap)], 
                                                [ space([size(20,groupWidth), gap(2)], 
                                                       text([vcenter(), fontSize(axisFontSize), textAngle(-90)], cat)) | cat <- categories]
                                               ), 
                                          space([size(10,groupGap)])       
                                                 
                                         ])),
              
                  use([top(), left()], vcat([gap(20), hcenter()],
                                                [ space([size(chartWidth, 40)]),
                                                  legend(funColors, chartWidth)
                                                ])),
                                                
                  use([bottom(), right()], hcat([gap(20), vcenter()],
                                                [ text([fontSize(subTitleFontSize), textAngle(-90)], xTitle),
                                                  space([size(20, chartHeight)])
                                                ]))
               ]);
   
}


public void b0(){
  render(barChart("Sales Prognosis 0", [<"a", 10>, <"b", 20>, <"c", 30>],
                 xLabel("Item"), 
                  yLabel("Value")
            ));
}


public void b1a(){
  render(barChart("Sales Prognosis 1", 
                  ["First Quarter", "Second Quarter"],
                  [ <"2009", [20]>,
                    <"2010", [40]>
                  ],
                  xLabel("Quarters"), 
                  yLabel("Sales")
            ));
}

public void b1(){
  render(barChart("Sales Prognosis 1", 
                  ["First Quarter", "Second Quarter"],
                  [ <"2009", [20,              25]>,
                    <"2010", [40,              60]>
                  ],
                  xLabel("Quarters"), 
                  yLabel("Sales")
            ));
}

public void b2(){
  render(barChart("Sales Prognosis 1", 
                  ["First Quarter", "Second Quarter"],
                  [ <"2009", [20,              25]>,
                    <"2010", [40,              60]>
                  ],
                  xLabel("Quarters"), 
                  yLabel("Sales"),
                  stackedBars()
            ));
}

public void b3(){
  render(barChart("Sales Prognosis 1", 
                  ["First Quarter", "Second Quarter"],
                  [ <"2009", [20,              25]>,
                    <"2010", [40,              60]>
                  ],
                  xLabel("Quarters"), 
                  yLabel("Sales"),
                  horizontal()
            ));
}

public void b4(){
  render(barChart("Sales Prognosis 1", 
                  ["First Quarter", "Second Quarter"],
                  [ <"2009", [20,              25]>,
                    <"2010", [40,              60]>
                  ],
                  xLabel("Quarters"), 
                  yLabel("Sales"),
                  stackedBars(),
                  horizontal()
            ));
}

//-------------------------------- pieChart ------------------------------------

public Figure pieChart(str title, map[str, int] facts, ChartSetting settings...){

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
    	elems += wedge([fromAngle(angle), toAngle(angle + delta),
						height(radius), innerRadius(ir), fillColor(funColorsMap[fname])],
						text("<facts[fname]>")
						);
	    angle += delta;
    }
 
    p = overlay([lineWidth(0), lineColor(0)], elems);
    
    return vcat([hcenter(), gap(20)],
    
                [ text([fontSize(20)], title),
                  (subtitle == "") ? space([size(0,-20)]) : text([fontSize(10)], subtitle),
                   overlay([center()], [ box([size(chartWidth,chartHeight), fillColor("lightgray")]),
                                         p
                                       ]),
                  legend(funColors, chartWidth)
                ]);
}

public void pie0(){
 	render(pieChart("pie0", ("a" : 1, "b" : 1, "c" : 1, "z": 1)));
}

public void pie1(){
 	render(pieChart("pie1", ("a" : 1, "b" : 2, "c" : 10, "z": 50)));
}

public void pie2(){
 	render(pieChart("pie2", ("a" : 1, "b" : 2, "c" : 10, "z": 50),
 	         subtitle("A very, very, very long subtitle don\'t you think?"))
 	
 	);
}

public void pie3(){
 	render(pieChart("pie3", ("a" : 1, "b" : 2, "c" : 10, "z": 50),
 					ring(20)
 	));
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
public void java barChart(str title, map[str,int] facts, ChartSetting settings...);

@doc{draw a bar chart}
@javaClass{org.rascalmpl.library.viz.BarChart}
public void java barChart(str title, map[str,real] facts, ChartSetting settings...);

@doc{draw a bar chart}
@javaClass{org.rascalmpl.library.viz.BarChart}
public void java barChart(str title, list[str] categories, list[intSeries] facts, ChartSetting settings...);

@doc{draw a bar chart}
@javaClass{org.rascalmpl.library.viz.BarChart}
public void java barChart(str title, list[str] categories, list[realSeries] facts, ChartSetting settings...);

// boxPlot aka BoxAndWiskerPlot

@doc{draw a boxPlot}
@javaClass{org.rascalmpl.library.viz.BoxPlot}
public void java boxplot(str title, list[intSeriesMultipleData] facts, ChartSetting settings...);

@doc{draw a boxplot}
@javaClass{org.rascalmpl.library.viz.BoxPlot}
public void java boxplot(str title, list[realSeriesMultipleData] facts, ChartSetting settings...);

// histogram

@doc{draw a histogram}
@javaClass{org.rascalmpl.library.viz.Histogram}
public void java histogram(str title, list[intSeries] facts, int nbins, ChartSetting settings...);

@doc{draw a histogram}
@javaClass{org.rascalmpl.library.viz.Histogram}
public void java histogram(str title, list[realSeries] facts, int nbins, ChartSetting settings...);

// piechart

@doc{draw a pie chart}
@javaClass{org.rascalmpl.library.viz.PieChart}
public void java pieChart(str title, map[str,int] facts, ChartSetting settings...);  

@doc{draw a pie chart}
@javaClass{org.rascalmpl.library.viz.PieChart}
public void java pieChart(str title, map[str,real] facts, ChartSetting settings...);   

// xyChart

@doc{draw an xy chart}
@javaClass{org.rascalmpl.library.viz.XYChart}
public void java xyChart(str title, list[intSeries] facts, ChartSetting settings...);

@doc{draw an xy chart}
@javaClass{org.rascalmpl.library.viz.XYChart}
public void java xyChart(str title, list[realSeries] facts, ChartSetting settings...);

***/