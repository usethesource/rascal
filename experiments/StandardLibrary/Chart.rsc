module Chart

data chartSetting =            // supported by
                               // barChart pieChart xyChart
     area()                    //                   x
   | dim3()                    // x        x                              
   | domainLabel(str txt)      // x                 x
   | rangeLabel(str txt)       // x                 x
   | horizontal()              // x        x        x
   | noLegend()                // x        x        x
   | noSectionLabels()         //          x
   | noToolTips()              // x        x        x
   | ring()                    //          x
   | seriesLabels(list[str] s) // x
   | stacked()                 // x  
   | subtitle(str txt)         // x        x        x  
   | vertical()                // x        x        x

   ;
    

@doc{draw a pie chart}
@javaClass{org.meta_environment.rascal.std.Chart.PieChart}
public void java pieChart(str title, value facts, value settings...);  // TODO value -> chartSetting

@doc{draw a bar chart}
@javaClass{org.meta_environment.rascal.std.Chart.BarChart}
public void java barChart(str title, value facts, value settings...);

@doc{draw an xy chart}
@javaClass{org.meta_environment.rascal.std.Chart.XYChart}
public void java xyChart(str title, value facts, value settings...);
