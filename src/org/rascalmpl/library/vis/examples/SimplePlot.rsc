module vis::examples::SimplePlot

import vis::Figure;
import vis::Render;
import Map;
import IO;
import Number;
import List;

public alias NamedPairSeries  = 
       tuple[str name,list[tuple[num xval, num yval]]  values];

@doc{Create simple line plots.}
public Figure simplePlot(str title, list[NamedPairSeries] facts, PlotProperty settings ... ){
   return _simplePlot(title, facts, settings);
   }

@doc{Create simple line plots. 
    xval are a list  of x values; the ith element which is again a list of 
    y values are the y coord of the plots belonging to the ith x value
    }  
public Figure simplePlot(str title, list[num] xval, list[list[num]] yval, PlotProperty settings ...) {
   if (size(xval)==0 || size(xval)!=size(yval)) return text("<size(xval)>=size(xval)!=size(yval)=<size(yval)>");
   int n = size(yval[0]);
   list[NamedPairSeries] facts = [<"<k>", [<xval[i], yval[i][k]>|int i<-[0.. size(xval)-1]]>|int k<-[0 .. n-1]];
   return _simplePlot(title, facts, settings);
   }
   
data PlotProperty =                                      
     areaPlot()                                          
   | chartSize(num w, num h)   
   | step(num d)
   | curvePlot ()              
   | linePlot()                            
   | subTitle(str txt)        
   | xLabel(str txt)           
   | yLabel(str txt)           
   ;
 
private num chartWidth = 400;
private num chartHeight = 400;
private str subTitle = "";
private str xTitle = "x";
private str yTitle = "y";
private bool isAreaPlot = false;
private bool isCurvePlot = false;
private bool isLinePlot = false;

private int titleFontSize = 20;
private int subTitleFontSize = 14;
private int axisFontSize = 10;
private num stepSize = 25;
private str rasterColor = "lightgray";
private bool aspectRatio = false;

private void applySettings(list[PlotProperty] settings){
   chartWidth = 400;
   chartHeight = 400;
   subTitle = "";
   xTitle = "x";
   yTitle = "y";
   isAreaPlot = false;
   isLinePlot = false;
   isCurvePlot = false;
   aspectRatio = false;
   stepSize = 25;
   
   for(PlotProperty setting <- settings){
        
       switch(setting){
         case areaPlot(): isAreaPlot = true;
   	     case chartSize(num w, num h): { chartWidth = w; chartHeight = h;}
   	     case curvePlot(): isCurvePlot = true;
   	     case linePlot(): isLinePlot = true;
   	     case step(num h): stepSize = h;
   	     case stackedBars() : isStackedBars = true;
         case subTitle(str s): subTitle = s;
         case xLabel(str s): xTitle = s;
         case yLabel(str s): yTitle = s;
         case aspectRatio(): aspectRatio = true;
       }
    }
}

Figure makeTitle(str s) {
    // return box(text(s, fontSize(titleFontSize)), width(chartWidth+100), height(titleFontSize), lineWidth(0));
    // return overlay([box(width(chartWidth+100+100), height(titleFontSize), lineWidth(0)), 
    // text(s, fontSize(titleFontSize))]);
    return text(s, fontSize(titleFontSize));
    }

Figure makeSubTitle(str s) {
   return text(s, fontSize(subTitleFontSize));
   } 

num min(num x, num y) {return x<y?x:y;}

num max(num x, num y) {return x<y?y:x;}

private Figure _simplePlot(str title, list[NamedPairSeries] facts, list[PlotProperty] settings){

   applySettings(settings);
   
   funPlots = [];
   funColors = [];
   num xmin = 1000000;
   num xmax = -1000000;
   
   num ymin = 1000000;
   num ymax = -1000000;
   
   // Max and min values in the data
   for(<str fname, list[tuple[num, num]] values> <- facts){
      for(<num x, num y> <- values){
          xmin = min(x, xmin);
          xmax = max(x, xmax);
          ymin = min(y, ymin);
          ymax = max(y, ymax);
      }
   }
   if (!aspectRatio) {
      xmin = min(xmin, ymin);
      ymin = min(xmin, ymin);
      xmax = max(xmax, ymax);
      ymax = max(xmax, ymax);
      }
    
  // Compute scaling
  xscale = chartWidth  / (xmax - xmin);
  yscale = chartHeight / (ymax - ymin);
  
  // Compute translation
  xshift = (xmin > 0) ? 0 : -xmin;
  yshift = (ymin > 0) ? 0 : -ymin;
  
  //println("xmin=<xmin>, xmax=<xmax>\nymin=<ymin>, ymax=<ymax>");
  //println("xscale=<xscale>, yscale=<yscale>, xshift=<xshift>, yshift=<yshift>");
               
   for(<str fname, list[tuple[num, num]] values> <- facts){
   		fcolorName = palette(size(funColors));
   		funColors += <fname, color(fcolorName)>;
   		list[FProperty] shapeProps = [lineColor(fcolorName), lineWidth(2)];
   		
   		if(isAreaPlot)
   		   shapeProps += [fillColor(color(fcolorName, 0.7)), shapeClosed(true), shapeConnected()];
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
               [ use( yaxis(yTitle,  ymin, stepSize, ymax, yscale),
                      bottom(), left()
                    )         
                 , use( xaxis(xTitle, xmin, stepSize, xmax, xscale), 
                      bottom(), left()
                     )     
                  ,funPlots
               ], bottom());
   return vcat([makeTitle(title), hcat([ylabels(yTitle,  ymin, stepSize, ymax, yscale), 
             vcat([plot, xlabels(xTitle, xmin, stepSize, xmax, xscale), makeSubTitle(xTitle)]),legend(funColors, 100)], top())]);
}


private Figure xtick(num n){
    return box(size(1,chartWidth), lineWidth(0));
}

private Figure xlabel(num n){
    return text("<n>", fontSize(axisFontSize));
}

private Figure ytick(num n){
  return box(size(chartHeight,1), lineWidth(0)) ;                
  }
  
private Figure ylabel(num n){
    // return grid([box(size(60, axisFontSize), lineWidth(0)), text("<n>" ,fontSize(axisFontSize), right())], right(), width(60));
    return text("<n>" ,fontSize(axisFontSize), right());
}

// X-axis

private Figure xaxis(str title, num start, num incr, num end, num scale){
   println("START:<start> incr:<incr> end: <end> scale: <scale>");
   Figure ticks = grid( [ xtick(n) | num n <- [start, (start + incr) .. end]],
                 gap(incr * scale), width(chartWidth), top() // vcenter() 
               );
   println("grid:<grid>");
   return ticks;
}

private Figure xlabels(str title, num start, num incr, num end, num scale){
   println("START:<start> incr:<incr> end: <end> scale: <scale>");
   Figure ticks = grid( [ xlabel(n) | num n <- [start, (start + incr) .. end]],
                 gap(incr * scale), width(chartWidth), top() // vcenter() 
               );
   println("grid:<grid>");
   return ticks;
}

// Y-axis

private Figure yaxis(str title,  num start, num incr, num end, num scale){
   Figure ticks = grid( [ ytick(n) | num n <- [end, (end - incr) .. start]],
                 gap(incr * scale), height(chartHeight), left() // right()
               );
   return ticks;
}

private Figure ylabels(str title,  num start, num incr, num end, num scale){
   Figure ticks = hcat([makeSubTitle(title), grid( [ ylabel(n) | num n <- [end, (end - incr) .. start]],
                 gap(incr * scale), height(chartHeight), right()
               )], vcenter());
   return ticks;
}

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

public void plotDemo() {
     // render(simplePlot("aap", [<"noot", [<i, (i/10)*(i/10)>|int i<-[-100,-90..100]]>, <"mies", [<i, i>|int i<-[-100,-90..100]]>],  chartSize(400, 400), curvePlot(), step(10), xLabel("x"), yLabel("y")));
     render(simplePlot("simple plot", [i|int i<-[-100,-90..100]],[[i, (i/10)*(i/10), 0.5*i]|int i<-[-100,-90..100]], chartSize(400, 400), curvePlot(), step(10)));
    }


