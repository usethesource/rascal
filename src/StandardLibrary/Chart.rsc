module Chart

data chartSetting =
     xlabel(str txt)
   | ylabel(str txt)
   | horizontal()
   | vertical()
   | noSectionLabels()
   | noLegend()
   | noToolTips()
   | stacked()
   | dim3()
   | circular()
//   | background(int r, int g, int b, real alpha);
   ;
    

@doc{draw a pie chart}
@javaClass{org.meta_environment.rascal.std.Chart.PieChart}
public void java pieChart(str title, map[str, int] facts, value settings...);  // TODO value -> chartSetting

@doc{draw a pie chart}
@javaClass{org.meta_environment.rascal.std.Chart.PieChart}
public void java pieChart(str title, map[str, real] facts, value settings...);

@doc{draw a bar chart}
@javaClass{org.meta_environment.rascal.std.Chart.BarChart}
public void java barchart(str title, str domainLabel, str rangeLabel, list[str] series, list[str] categories, list[list[int]] facts);

@doc{draw a bar chart}
@javaClass{org.meta_environment.rascal.std.Chart.BarChart}
public void java barchart(str title, str domainLabel, str rangeLabel, list[str] series, list[str] categories, list[list[real]] facts);
