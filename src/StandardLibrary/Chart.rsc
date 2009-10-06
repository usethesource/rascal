module Chart

@doc{draw a pie chart}
@javaClass{org.meta_environment.rascal.std.Chart.PieChart}
public void java piechart(str title, map[str, int] facts);

@doc{draw a pie chart}
@javaClass{org.meta_environment.rascal.std.Chart.PieChart}
public void java piechart(str title, map[str, real] facts);

@doc{draw a bar chart}
@javaClass{org.meta_environment.rascal.std.Chart.BarChart}
public void java barchart(str title, str domainLabel, str rangeLabel, list[str] series, list[str] categories, list[list[int]] facts);

@doc{draw a bar chart}
@javaClass{org.meta_environment.rascal.std.Chart.BarChart}
public void java barchart(str title, str domainLabel, str rangeLabel, list[str] series, list[str] categories, list[list[real]] facts);
