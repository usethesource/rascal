module experiments::VL::Chart

import experiments::VL::VLCore;
import viz::VLRender;
import Map;
import IO;
import Integer;
import List;

// Settings for the various chart types (not all implemented yet)

data ChartSetting =            //             supported by
                               // barChart pieChart xyChart histogram boxplot
     chartSize(int w, int h)   //    x         x      x         x        x
   | xLabel(str txt)           //    x                x         x        x
   | horizontal()              //    x                x         x        x
   | noSectionLabels()         //              x
   | yLabel(str txt)           //    x                x         x        x
   | ring(int h)               //              x
   | areaPlot()                //                     x
   | linePlot()                //                     x
   | curvePlot ()              //                     x      
   | stackedBars()             //    x  
   | subtitle(str txt)         //    x         x      x         x        x
   | vertical()                //    x                x         x        x
   ;
 
private int chartWidth = 400;
private int chartHeight = 400;
private str subtitle = "";
private str xTitle = "";
private str yTitle = "";
private bool isAreaPlot = false;
private bool isCurvePlot = false;
private bool isLinePlot = false;
private bool isStackedBars = false;
private bool isVertical = true;
private int ringHeight = 0;

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
   	     case chartSize(int w, int h): { chartWidth = w; chartHeight = h;}
         
         case subtitle(str s): subtitle = s;
         
         case xLabel(str s): xTitle = s;
         
         case yLabel(str s): yTitle = s;
         
         case areaPlot(): isAreaPlot = true;
         
         case linePlot(): isLinePlot = true;
         
         case curvePlot(): isCurvePlot = true;
         
         case stackedBars() : isStackedBars = true;
         
         case horizontal(): isVertical = false;
         
         case vertical(): isVertical = true;
         
         case ring(int h): ringHeight = h;
       }
    }
}

// Background raster with title and subtitle
  
private VELEM raster(str title){
   return vcat([hcenter(), gap(0,20)],
                   [ text([fontSize(titleFontSize)], title),
                     (subtitle == "") ? space([size(0,-20)]) : text([fontSize(subTitleFontSize)], subtitle),
                     box([size(chartWidth,chartHeight), fillColor(rasterColor)])
                   ]);
}

// Draw: |
//       n
// for x-axis

private VELEM xtick(int n){
  return vcat([gap(2), left()], [box([size(1,10), lineWidth(0)]), text([fontSize(axisFontSize)], "<n>")]);
}

// Draw: n --
// for y-axis

private VELEM ytick(int n){
  return hcat([gap(2), bottom()], [text([fontSize(axisFontSize)], "<n>"), box([size(10,1), lineWidth(0)])]);
}

// X-axis

public VELEM xaxis(str title, int length, int start, int incr, int end, int scale){
   ticks = grid([gap(incr * scale), width(length), vcenter()], [xtick(n) | int n <- [start, (start + incr) .. end]]);
  
   return vcat([gap(20), hcenter()], 
                   [ ticks,
                     text([fontSize(subTitleFontSize)], title)
                   ]);
}

// Y-axis

public VELEM yaxis(str title, int length, int start, int incr, int end, int scale){
   ticks = grid([gap(incr * scale), width(1), right()], [ytick(n) | int n <- [end, (end - incr) .. start]]);
   
   return hcat([gap(20), vcenter()], 
                   [ text([fontSize(subTitleFontSize), textAngle(-90)], title),
                     ticks
                   ]);
}

// One item (name + colored box) in legend

private VELEM legendItem(str name, Color c){
  return hcat([gap(2), vcenter()], [text([fontSize(10)], "<name> = "), box([size(20,10), lineWidth(0), fillColor(c)])]);
}

// A complete legend

private VELEM legend(map[str, Color] funColors, int w){
   return box([center(), gap(10,10), fillColor("lightgray")], 
               align([width(w), gap(10), center()], [legendItem(name, funColors[name]) | name <- funColors]));
}

// Data for xyChart

public alias intTuples  = 
       tuple[str name,list[tuple[int, int]]  values];

public VELEM xyChart(str title, list[intTuples] facts, ChartSetting settings ... ){

   applySettings(settings);
   
   funPlots = [];
   funColors = ();
   xmin = 1000000;
   xmax = -1000000;
   
   ymin = 1000000;
   ymax = -1000000;
   
   // Max and min values in the data
   for(<str fname, list[tuple[int, int]] values> <- facts){
      for(<int x, int y> <- values){
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
  
  // Add vertical axis at x=0
  funPlots += shape([lineColor("darkgrey"), lineWidth(1), connected()],
                     [ vertex((xshift + 0) * xscale, 0),
                       vertex((xshift + 0) * xscale, chartHeight)
                     ]);
  // Add horizontal axis at y=0
  funPlots+= shape([lineColor("darkgrey"), lineWidth(1), connected()],
                     [ vertex(0,          (yshift + 0) * yscale),
                       vertex(chartWidth, (xshift + 0) * yscale)
                     ]);  
  // Add function plots                            
   for(<str fname, list[tuple[int, int]] values> <- facts){
   		fcolorName = palette(size(funColors));
   		funColors[fname] = color(fcolorName);
   		list[VPROP] shapeProps = [lineColor(fcolorName), lineWidth(2)];
   		
   		if(isAreaPlot)
   		   shapeProps += [fillColor(color(fcolorName, 0.7)), closed(), connected()];
   		else
   		   shapeProps += [fillColor(fcolorName)];
   		   
   		if(isCurvePlot)
   		   shapeProps += [curved(), connected()];
   		   
   		if(isLinePlot)
   		   shapeProps += [connected()];
   		   
        funPlots += shape(shapeProps,
                          [vertex((xshift + x) * xscale, (yshift + y) * yscale, ellipse([size(5), fillColor(fcolorName), lineWidth(0)])) | <int x, int y> <- values]);
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

private list[intTuples] pdata =
        [ <"f", [<0, 50>, <10,50>, <20,50>, <30, 50>, <40, 50>, <50, 50>, <60,50>]>, 
          <"g", [<50,0>, <50,50>, <50,100>]>,
          <"h", [<0,0>, <10,10>, <20,20>, <30,30>, <40,40>, <50,50>, <60,60>]>,
          <"i", [<0, 60>, <10, 50>, <20, 40>, <30, 30>, <40, 20>, <50, 10>, <60, 0>]>,
          <"j", [< -20, 20>, < -10, 10>, <0,0>, <10, -10>, <20, -20>]>,
          <"k", [< -20, 40>, < -10, 10>, <0, 0>, <10, 10>, <20, 40>, <30, 90>]>                
        ];

// Scatter plot

public void p1(){
	render(xyChart("Test Title P1", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis")
                  )
           );
}

// Line plot

public void p2(){
	render(xyChart("Test Title P2", 
	                 pdata, chartSize(400,400), xLabel("The X axis"), yLabel("The Y axis"),
	                 linePlot()
                  )
           );
}

// Curve plot

public void p3(){
	render(xyChart("Test Title P3", 
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

public alias intSeries  = 
       tuple[str name,list[int]  values];
       
public void barChart(str title, map[str,int] facts, ChartSetting settings...){

}

public VELEM barChart(str title, list[str] categories, list[intSeries] facts, ChartSetting settings...){
   
   applySettings(settings);
 
   funPlots = [];
   funColors = ();
   
   nbars = 0;
   nseries = size(facts);
   
   ymin = 1000000;
   ymax = -1000000;
   ysummax = -1000000;
   
   // Max and min values in the data
   for(<str fname, list[int] values> <- facts){
      n = size(values);
      if(n > nbars)
          nbars = n;
      ysum = 0;
      for(int y <- values){
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
  
  println("barWidth=<barWidth>, barGap=<barGap>, groupWidth=<groupWidth>, groupGap=<groupGap>");
  
  // Compute translation

  println("yscale=<yscale>");
  fns = ();
  for(<str fname, list[int] values> <- facts){
    fcolorName = palette(size(funColors));
    funColors[fname] = color(fcolorName);
    for(int i <- [0 .. size(values)-1]){
        int bw = barWidth;
        int bh = values[i] * yscale;
        if(!isVertical)
           <bw, bh> = <bh, bw>;
     	fns[i] = (fns[i] ? []) + box([size(bw, bh), lineWidth(0), fillColor(funColors[fname])]);
     }
  }
  for(int i <- [0 .. size(categories)-1]){
  	funPlots += isStackedBars ? (isVertical ? vcat([bottom(), gap(0)], reverse(fns[i]))
  	                                    : hcat([bottom(), gap(0)], fns[i]))
  	                      : (isVertical ? hcat([left(), hcenter(), gap(barGap)], reverse(fns[i]))
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

public VELEM pieChart(str title, map[str, int] facts, ChartSetting settings...){

	applySettings(settings);
 	funColors = ();
 	elems = [];
 	radius = 3*chartWidth/7;
 	ir = (ringHeight == 0) ? 0 : radius - ringHeight;
 	real total = 0.0;
 	for(v <- range(facts))
 		total += v;
 	
 	angle = 0.0;
 	for(fname <- facts){
 		fcolorName = palette(size(funColors));
   		funColors[fname] = color(fcolorName, 0.6);
   		delta = facts[fname] * 360 / total;
    	elems += wedge([fromAngle(angle), toAngle(angle + delta),
						height(radius), innerRadius(ir), fillColor(funColors[fname])],
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

public void pie1(){
 	render(pieChart("p1", ("a" : 1, "b" : 2, "c" : 10, "z": 50)));
}

public void pie2(){
 	render(pieChart("p2", ("a" : 1, "b" : 2, "c" : 10, "z": 50),
 	         subtitle("A very, very, very long subtitle don't you think?"))
 	
 	);
}

public void pie3(){
 	render(pieChart("p3", ("a" : 1, "b" : 2, "c" : 10, "z": 50),
 					ring(20)
 	));
}


