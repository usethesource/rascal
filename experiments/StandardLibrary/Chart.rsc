module Chart

/*
 * Library functions for chart drawing:
 * - barChart
 * - boxPlot
 * - histogram
 * - pieChart
 * - xyChart
 */
 
 /*
  * Settings to refine the actual chart
  */

data chartSetting =            // supported by
                               // barChart pieChart xyChart histogram boxplot
     area()                    //                   x
   | dim3()                    // x        x                              
   | domainLabel(str txt)      // x                 x       x         x
   | horizontal()              // x                 x       x         x
   | noSectionLabels()         //          x
   | rangeLabel(str txt)       // x                 x       x         x
   | ring()                    //          x
   | stacked()                 // x  
   | subtitle(str txt)         // x        x        x       x         x
   | vertical()                // x                 x       x         x

   ;
   
/*
 * Data formats used in chart functions
 */

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
@javaClass{org.meta_environment.rascal.std.Chart.BarChart}
public void java barChart(str title, map[str,int] facts, value settings...);

@doc{draw a bar chart}
@javaClass{org.meta_environment.rascal.std.Chart.BarChart}
public void java barChart(str title, map[str,real] facts, value settings...);

@doc{draw a bar chart}
@javaClass{org.meta_environment.rascal.std.Chart.BarChart}
public void java barChart(str title, list[str] categories, list[intSeries] facts, value settings...);

@doc{draw a bar chart}
@javaClass{org.meta_environment.rascal.std.Chart.BarChart}
public void java barChart(str title, list[str] categories, list[realSeries] facts, value settings...);

// boxPlot aka BoxAndWiskerPlot

@doc{draw a boxPlot}
@javaClass{org.meta_environment.rascal.std.Chart.BoxPlot}
public void java boxplot(str title, list[intSeriesMultipleData] facts, value settings...);

@doc{draw a boxplot}
@javaClass{org.meta_environment.rascal.std.Chart.BoxPlot}
public void java boxplot(str title, list[realSeriesMultipleData] facts, value settings...);

// histogram

@doc{draw a histogram}
@javaClass{org.meta_environment.rascal.std.Chart.Histogram}
public void java histogram(str title, list[intSeries] facts, int nbins, value settings...);

@doc{draw a histogram}
@javaClass{org.meta_environment.rascal.std.Chart.Histogram}
public void java histogram(str title, list[realSeries] facts, int nbins, value settings...);

// piechart

@doc{draw a pie chart}
@javaClass{org.meta_environment.rascal.std.Chart.PieChart}
public void java pieChart(str title, map[str,int] facts, value settings...);  // TODO value -> chartSetting

@doc{draw a pie chart}
@javaClass{org.meta_environment.rascal.std.Chart.PieChart}
public void java pieChart(str title, map[str,real] facts, value settings...);  // TODO value -> chartSetting

// xyChart

@doc{draw an xy chart}
@javaClass{org.meta_environment.rascal.std.Chart.XYChart}
public void java xyChart(str title, list[intSeries] facts, value settings...);

@doc{draw an xy chart}
@javaClass{org.meta_environment.rascal.std.Chart.XYChart}
public void java xyChart(str title, list[realSeries] facts, value settings...);
