module experiments::VL::xyChart

import experiments::VL::VLCore;
import experiments::VL::VLRender; 
import Map;
import IO;
import Integer;

public alias intTuples  = 
       tuple[str name,list[tuple[int, int]]  values];

public VELEM lineChart(str title, list[intTuples] facts){
// Potential parameters:
   chartWidth = 400;
   chartHeight = 400;
   xtitle = "x-axis";
   ytitle = "y-axis";
   isClosed = false;
   
   // TODO scatter plot
   
   funPlots = [];
   funColors = ();
   xmin = 1000000;
   xmax = -1000000;
   
   ymin = 1000000;
   ymax = -1000000;
   
   // Max and min values in the data
   for(<str fname, list[tuple[int, int]] values> <- facts)
      for(<int x, int y> <- values){
          xmin = min(x, xmin);
          xmax = max(x, xmax);
          ymin = min(y, ymin);
          ymax = max(y, ymax);
      }
  
  // Compute scaling
  xscale = chartWidth  / (xmax - xmin);
  yscale = chartHeight / (ymax - ymin);
  
  // Compute translation
  xshift = (xmin > 0) ? 0 : -xmin;
  yshift = (ymin > 0) ? 0 : -ymin;
  
  // Add vertical axis at x=0
  funPlots += shape([lineColor("darkgrey"), lineWidth(1)],
                     [ vertex((xshift + 0) * xscale, 0),
                       vertex((xshift + 0) * xscale, chartHeight)
                     ]);
  // Add horizontal axis at y=0
  funPlots+= shape([lineColor("darkgrey"), lineWidth(1)],
                     [ vertex(0,          (yshift + 0) * yscale),
                       vertex(chartWidth, (xshift + 0) * yscale)
                     ]);  
  // Add function plots                            
   for(<str fname, list[tuple[int, int]] values> <- facts){
   		fcolorName = palette(size(funColors));
   		funColors[fname] = color(fcolorName);
   		list[VPROP] shapeProps;
   		if(isClosed){
   		   shapeProps = [lineColor(fcolorName), lineWidth(2), fillColor(color(fcolorName, 0.7)), curved(), closed()];
   		}
   		else
   		   shapeProps = [lineColor(fcolorName), lineWidth(2), fillColor(fcolorName), curved()];
   		   
        funPlots += shape(shapeProps,
                          [vertex((xshift + x) * xscale, (yshift + y) * yscale, ellipse([size(5), fillColor(fcolorName), lineWidth(0)])) | <int x, int y> <- values]);
   }
   
   funs = overlay([bottom(), left()], funPlots);
   
   // Background raster with title
   raster = vertical([hcenter(), gap(0,20)],
                   [ text([fontSize(20)], title),
                     box([size(400,400), fillColor("lightgray")])
                   ]);
           
   // Superimpose on the same grid point (with different allignments):
   // - x-axis,
   // - y-axis
   // - raster
   // - function plots
   
   plot = grid([bottom(), left(), gap(0)],
               [ use([bottom(), right()], yaxis(ytitle, chartHeight, ymin, 10, ymax, yscale)),
                 use([top(), left()],     vertical([hcenter(), gap(20)],
                                                   [ xaxis(xtitle, chartWidth,  xmin, 10, xmax, xscale),
                                                     legend(funColors, chartWidth)
                                                   ])),      
                 use([bottom(), left()], raster),
                 funPlots
               ]);
   
   return plot;
}

// Draw: |
//       n

private VELEM xtick(int n){
  return vertical([gap(2), left()], [box([size(1,10), lineWidth(0)]), text([fontSize(10)], "<n>")]);
}

// Draw: n --

private VELEM ytick(int n){
  return horizontal([gap(2), bottom()], [text([fontSize(10)], "<n>"), box([size(10,1), lineWidth(0)])]);
}

public VELEM xaxis(str title, int length, int start, int incr, int end, int scale){
   ticks = grid([gap(incr * scale), width(length), vcenter()], [xtick(n) | int n <- [start, (start + incr) .. end]]);
  
   return vertical([gap(20), hcenter()], 
                   [ ticks,
                     text([fontSize(14)], title)
                   ]);
}

public VELEM yaxis(str title, int length, int start, int incr, int end, int scale){
   ticks = grid([gap(incr * scale), width(1), right()], [ytick(n) | int n <- [end, (end - incr) .. start]]);
   
   return horizontal([gap(20), vcenter()], 
                   [ text([fontSize(14), textAngle(-90)], title),
                     ticks
                   ]);
}

private VELEM legendItem(str name, Color c){
  return horizontal([gap(2), vcenter()], [text([fontSize(10)], "<name> = "), box([size(20,2), lineWidth(0), fillColor(c)])]);
}

private VELEM legend(map[str, Color] funColors, int w){
   return box([center(), gap(20,20)], 
               use(align([width(w), gap(10), center()], [legendItem(name, funColors[name]) | name <- funColors])));
}

public void p1(){
	render(lineChart("Test Title", 
	                 [ <"f", [<0, 50>, <10,50>, <20,50>, <30, 50>, <40, 50>, <50, 50>, <60,50>]>, 
                       <"g", [<50,0>, <50,50>, <50,100>]>,
                       <"h", [<0,0>, <10,10>, <20,20>, <30,30>, <40,40>, <50,50>, <60,60>]>,
                       <"i", [<0, 60>, <10, 50>, <20, 40>, <30, 30>, <40, 20>, <50, 10>, <60, 0>]>,
                       <"j", [< -20, 20>, < -10, 10>, <0,0>, <10, -10>, <20, -20>]>,
                       <"k", [< -20, 40>, < -10, 10>, <0, 0>, <10, 10>, <20, 40>, <30, 90>]>                
                     ])
           );
}

//----------------------------------  barChart -----------------------------------------------

public alias intSeries  = 
       tuple[str name,list[int]  values];
       
public void barChart(str title, map[str,int] facts){

}

public void barChart(str title, list[str] categories, list[intSeries] facts){

}


/*
public void b1(){
  barChart("Sales Prognosis 1", 
                     ["First Quarter", "Second Quarter"],
           [<"2009", [20,              25]>,
            <"2010", [40,              60]>],
            domainLabel("Quarters"), 
            rangeLabel("Sales")
            );
}

public void b2(){ 
  barChart("Sales Prognosis 2",  
                     ["First Quarter", "Second Quarter"],
           [<"2009", [20,              25]>,
            <"2010", [40,              60]>],
            domainLabel("Quarters"), 
            rangeLabel("Sales"),
            dim3()
            );
}

public void b3(){   
  barChart("Sales Prognosis 3",  
                     ["First Quarter", "Second Quarter"],
           [<"2009", [20,              25]>,
            <"2010", [40,              60]>],
            domainLabel("Quarters"), 
            rangeLabel("Sales"),
            dim3(),
            horizontal()
            );
}

public void b4(){   
  barChart("Sales Prognosis 4",  
                     ["First Quarter", "Second Quarter"],
           [<"2009", [20,              25]>,
            <"2010", [40,              60]>],
            domainLabel("Quarters"), 
            rangeLabel("Sales"),
            dim3(),
            stacked()
            );
}
*/


