@license{
  Copyright (c) 2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@synopsis{Simple data visualization using charts}
@description{
This modules provides a simple API to create charts for Rascal
(numerical) data, based on [chart.js](https://chartjs.org/). 
This library mirrors chart.js' JSON-based configuration API more or less one-to-one
using data types of Rascal. Documentation about chart.js should be easy
to interpret.

This module is quite new and may undergo some tweaks in the coming time.
}
@examples{
```rascal-shell
import vis::Charts;
import util::Math;
scatterChart([<x-arbInt(20),x+arbInt(20)> | x <- [1..100]])
scatterChart(["down", "up"], [<x,100-x+arbInt(20)> | x <- [1..100]], [<x,x+arbInt(20)> | x <- [1..100]])
```

```rascal-shell-continue
barChart([<"<x>",x-arbInt(20)> | x <- [1..100]])
barChart(["down", "up"], [<"<x>",100-x+arbInt(20)> | x <- [1..100]], [<"<x>",x+arbInt(20)> | x <- [1..100]])
```

```rascal-shell-continue
lineChart([<"<x>",x+arbInt(20)> | x <- [1..100]])
lineChart(["down", "up"],
    [<"<x>",100-x+arbInt(20)> | x <- [1..100]], 
    [<"<x>",x+arbInt(20)> | x <- [1..100]]
)
lineChart(["down", "up", "even"],
    [<"<x>",100-x+arbInt(20)> | x <- [1..100]], 
    [<"<x>",x+arbInt(20)> | x <- [1..100]], 
    [<"<x>", 70-arbInt(20)> | x <- [1..100]]
)
```

```rascal-shell-continue
pieChart([<"<x>",x+arbInt(25)> | x <- [1..10]])
```
}
@benefits{
* Easy to use for basic charting.
* Uses ((ChartAutoColors)) extension for ease-of-use.
* Support for 8 ((ChartType))s including multiple ((ChartDataSet))s in one chart.
* This API is open to extension via adding common keyword parameters for supporting any extension to the basic chart.js configuration.
* You can roll your own HTML or Server based on the building blocks in this module to include and use extensions, or to combine different charts in the same view.
}
@pitfalls{
* Where `num` is asked, still `rat` values are _not_ supported.
* All `real` values must stay within JVM's `double` datatype
* All `int` values must fit within JVM's `long` datatype
}
module vis::Charts

import lang::html::IO;
import lang::html::AST;
import Content;
import Set;
import List;

@synopsis{A scatterplot from a binary numerical relation.}

Content scatterChart(lrel[num x,num y] v, str title="Scatterplot", ChartAutoColorMode colorMode=\dataset()) 
    = content(title, chartServer(chartData(title, v), \type=scatter(), title=title, colorMode=colorMode, legend=false));

Content scatterChart(list[str] labels, lrel[num x,num y] values ..., str title="Scatterplots", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=scatter(), title=title, colorMode=colorMode));

Content scatterChart(rel[num x ,num y] v, str title="Scatterplot", ChartAutoColorMode colorMode=\dataset()) 
    = content(title, chartServer(chartData(title, v), \type=scatter(), title=title, colorMode=colorMode, legend=false));

Content scatterChart(list[str] labels, rel[num x,num y] values ..., str title="Scatterplots", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=scatter(), title=title, colorMode=colorMode));

@synopsis{A bubblechart from a binary numerical list relation.}
@pitfalls{
* the radius is in raw pixels rather than scaled to the chart's axis
}
Content bubbleChart(lrel[num x,num y, num r] v, str title="Scatterplot", ChartAutoColorMode colorMode=\data()) 
    = content(title, chartServer(chartData(title, v), \type=bubble(), title=title, colorMode=colorMode));

Content bubbleChart(list[str] labels, lrel[num x,num y, num r] values ..., str title="Scatterplots", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(labels, values), \type=scatter(), title=title, colorMode=colorMode));

Content bubbleChart(rel[num x,num y, num r] v, str title="Scatterplot", ChartAutoColorMode colorMode=\data()) 
    = content(title, chartServer(chartData(title, v), \type=scatter(), title=title, colorMode=colorMode));

Content bubbleChart(list[str] labels, rel[num x,num y, num r] values ..., str title="Scatterplots", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(labels, values), \type=scatter(), title=title, colorMode=colorMode));    

@synopsis{A bar chart from labeled numbers}
Content barChart(rel[str label, num val] values, str title="Bar Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(values), \type=\bar(), title=title, colorMode=colorMode));

Content barChart(lrel[str label, num val] values, str title="Bar Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(values), \type=\bar(), title=title, colorMode=colorMode));

Content barChart(list[str] labels, rel[str label, num val] values..., str title="Bar Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=\bar(), title=title, colorMode=colorMode));

Content barChart(list[str] labels, lrel[str label, num val] values..., str title="Bar Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=\bar(), title=title, colorMode=colorMode));

@synopsis{A line chart from labeled numbers}
Content lineChart(rel[str label, num val] values, str title="Line Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(values), \type=\line(), title=title, colorMode=colorMode, legend=false));

Content lineChart(lrel[str label, num val] values, str title="Line Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(values), \type=\line(), title=title, colorMode=colorMode, legend=false));

Content lineChart(list[str] labels, rel[str label, num val] values..., str title="Line Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=\line(), title=title, colorMode=colorMode));

Content lineChart(list[str] labels, lrel[str label, num val] values..., str title="Line Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=\line(), title=title, colorMode=colorMode));

@synopsis{A polar area chart from labeled numbers}
Content polarAreaChart(rel[str label, num val] values, str title="Polar Area Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(values), \type=\polarArea(), title=title, colorMode=colorMode));

Content polarAreaChart(lrel[str label, num val] values, str title="Polar Area Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(values), \type=\polarArea(), title=title, colorMode=colorMode));

Content polarAreaChart(list[str] labels, rel[str label, num val] values..., str title="Polar Area Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=\polarArea(), title=title, colorMode=colorMode));

Content polarAreaChart(list[str] labels, lrel[str label, num val] values..., str title="Polar Area Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=\polarArea(), title=title, colorMode=colorMode));

@synopsis{A radar chart from labeled numbers}
Content radarChart(rel[str label, num val] values, str title="Radar Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(values), \type=\radar(), title=title, colorMode=colorMode));

Content radarChart(lrel[str label, num val] values, str title="Radar Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(values), \type=\radar(), title=title, colorMode=colorMode));

Content radarChart(list[str] labels, rel[str label, num val] values..., str title="Radar Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=\radar(), title=title, colorMode=colorMode));

Content radarChart(list[str] labels, lrel[str label, num val] values..., str title="Radar Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=\radar(), title=title, colorMode=colorMode));   

@synopsis{A pie chart from labeled numbers}
Content pieChart(rel[str label, num val] values, str title="Pie Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(values), \type=\pie(), title=title, colorMode=colorMode));

Content pieChart(lrel[str label, num val] values, str title="Pie Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(values), \type=\pie(), title=title, colorMode=colorMode));

Content pieChart(list[str] labels, rel[str label, num val] values..., str title="Pie Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=\pie(), title=title, colorMode=colorMode));

Content pieChart(list[str] labels, lrel[str label, num val] values..., str title="Pie Chart", ChartAutoColorMode colorMode=\dataset())
    = content(title, chartServer(chartData(labels, values), \type=\pie(), title=title, colorMode=colorMode));  

@synopsis{A dougnut chart from labeled numbers}
Content doughnutChart(rel[str label, num val] values, str title="Doughnut Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(values), \type=\doughnut(), title=title, colorMode=colorMode));

Content doughnutChart(lrel[str label, num val] values, str title="Doughnut Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(values), \type=\doughnut(), title=title, colorMode=colorMode));

Content doughnutChart(list[str] labels, rel[str label, num val] values..., str title="Doughnut Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(labels, values), \type=\doughnut(), title=title, colorMode=colorMode));

Content doughnutChart(list[str] labels, lrel[str label, num val] values..., str title="Doughnut Chart", ChartAutoColorMode colorMode=\data())
    = content(title, chartServer(chartData(labels, values), \type=\doughnut(), title=title, colorMode=colorMode));       

@synopsys{converts plain data sources into chart.js datasets}
ChartDataSet chartDataSet(str label, rel[num x, num y] r)
    = chartDataSet([point(x,y) | <x,y> <- r],
        label=label
    );

ChartDataSet chartDataSet(str label, rel[num x, num y, num rad] r)
    = chartDataSet([point(x,y,r=rad) | <x,y,rad> <- r],
        label=label
    );

ChartDataSet chartDataSet(str label, lrel[num x,num y] r)
    = chartDataSet([point(x,y) | <x,y> <- r],
        label=label
    );

ChartDataSet chartDataSet(str label, lrel[num x, num y, num r] r)
    = chartDataSet([point(x,y,r=rad) | <x,y,rad> <- r],
        label=label
    );    

@synopsys{converts plain data sources into the chart.js data representation}
ChartData chartData(rel[str label, num val] v)
    = chartData(
        labels=[l | <l,_> <- v],
        datasets=[
            chartDataSet([n | <_, n> <- v])
        ]
    );    

ChartData chartData(lrel[str label, num val] v)
    = chartData(
        labels=[l | <l,_> <- v],
        datasets=[
            chartDataSet([n | <_, n> <- v])
        ]
    );

ChartData chartData(list[str] labels, lrel[num x, num y] values...)
    = chartData(
        labels=labels,
        datasets=[chartDataSet(labels[i], values[i]) | i <- [0..size(labels)]]
    );

ChartData chartData(list[str] labels, lrel[num x , num y , num r] values...)
    = chartData(
        labels=labels,
        datasets=[chartDataSet(labels[i], values[i]) | i <- [0..size(labels)]]
    );    

ChartData chartData(list[str] labels, rel[num x, num y] values...)
    = chartData(
        labels=labels,
        datasets=[chartDataSet(labels[i], values[i]) | i <- [0..size(labels)]]
    );

ChartData chartData(list[str] labels, rel[num x, num y, num r] values...)
    = chartData(
        labels=labels,
        datasets=[chartDataSet(labels[i], values[i]) | i <- [0..size(labels)]]
    );

ChartData chartData(list[str] setLabels, lrel[str label, num val] values...)
    = chartData(
        // first merge the label sets, while keeping the order as much as possible
        labels=labels, 
        // now sort the datasets accordingly, missing data is represented by `0`
        datasets=[chartDataSet([*(values[i][l]?[0]) | l <- labels], label=setLabels[i]) | i <- [0..size(setLabels)]]
    )
    when list[str] labels := ([] | (l in it) ? it : it + l | r <- values, l <- r<0>)
    ;

ChartData chartData(list[str] setLabels, rel[str label, num val] values...)
    = chartData(
        // first merge the label sets, while keeping the order as much as possible
        labels=labels, 
        // now sort the datasets accordingly, missing data is represented by `0`
        datasets=[chartDataSet([*(values[i][l]?{0}) | l <- labels], label=setLabels[i]) | i <- [0..size(setLabels)]]
    )
    when list[str] labels := ([] | (l in it) ? it : it + l | r <- values, l <- r<0>)
    ;

ChartData chartData(list[str] labels, list[num] values...)
    = chartData(
        labels=labels,
        datasets=[chartDataSet(v) | v <- values]
    );

ChartData chartData(str label, lrel[num x, num y] values)
    = chartData(
        datasets=[
            chartDataSet(label, values)
        ]
    );

ChartData chartData(str label, lrel[num x, num y, num r] values)
    = chartData(
        datasets=[
            chartDataSet(label, values)
        ]
    );    

ChartData chartData(str label, rel[num x, num y] values)
    = chartData(
        datasets=[
            chartDataSet(label, values)
        ]
    );

ChartData chartData(str label, rel[num x, num y, num r] values)
    = chartData(
        datasets=[
            chartDataSet(label, values)
        ]
    );
    
@synopsis{Toplevel chart structure}    
data Chart 
    = chart(
        ChartType \type = scatter(),
        ChartOptions options = chartOptions(),
        ChartData \data = chartData()
    );

@synopsis{Wrapper for a set of datasets, each with a label}
data ChartData 
    = chartData(
        list[str]  labels=[],
        list[ChartDataSet] datasets = []
    );

@synopsis{A dataset is a list of values to chart, with styling properties.}
@description{
The `data` field is a list of supported values, of which the constraints
are not expressible by ((Declarations-AlgebraicDataType))s. These are currently supported:

* ((ChartDataPoint)), with an without a `r`adius
* `num`, but within `double` precision (!) and no `rat`
}
data ChartDataSet(
        str label="",
        list[str] backgroundColor=[],
        list[str] borderColor=[],
        list[str] color=[]
    )
    = chartDataSet(list[value] \data)
    ;

@synopsis{A data point is one of the types of values in a ChartDataSet}
data ChartDataPoint
    = point(num x, num y, num r = 0);

data ChartType
    = scatter()
    | bar()
    | bubble()
    | line()
    | polarArea()
    | radar()
    | pie()
    | doughnut()
    ;

data ChartOptions  
    = chartOptions(
        bool responsive=true,
        ChartPlugins plugins = chartPlugins()  
    );

data ChartPlugins
    = chartPlugins(
        ChartTitle title = chartTitle(),
        ChartLegend legend = chartLegend(),
        ChartColors colors = chartColors(),
        ChartAutoColors autocolors = chartAutoColors()
    );

data ChartAutoColors
    = chartAutoColors(
        ChartAutoColorMode \mode = \data()
    );

data ChartAutoColorMode 
    = \data()
    | \dataset()
    ;

data ChartLegend   
    = chartLegend(
        LegendPosition position = top(),
        bool display=true
    );

data LegendPosition
    = \top()
    | \bottom()
    | \left()
    | \right()
    ;

data ChartTitle
    = chartTitle(
        str text="",
        bool display = true
    );

data ChartColors
    = chartColors(
        bool enabled = true
    );

@synopsis{Utility function that constructs a Chart from ChartData and a given Chart type and a title.}
@description{
A chart has a typical default layout that we can reuse for all kinds of chart types. This function
provides the template and immediately instantiates the client and the server to start displaying the chart
in a browser.
}
Response(Request) chartServer(ChartData theData, ChartType \type=\bar(), str title="Chart", ChartAutoColorMode colorMode=\data(), bool legend=false)
    = chartServer(
        chart(
            \type=\type,
            \data=theData,
            options=chartOptions(
                responsive=true,
                plugins=chartPlugins(
                    legend=chartLegend(
                        position=top(),
                        display=legend
                    ),
                    title=chartTitle(
                        display=true,
                        text=title
                    ),
                    colors=chartColors(
                        enabled=true
                    ),
                    autocolors=chartAutoColors(
                        mode=colorMode
                    )
                ) 
            )
        )
    );

@synopsis{this is the main server generator for any chart value}
@description{
Given a Chart value this server captures the value and serves it
as a JSON value to the HTML client generated by ((plotHTML)).
}
Response (Request) chartServer(Chart ch) {
    Response reply(get(/^\/chart/)) {
        return response(ch);
    }

    // returns the main page that also contains the callbacks for retrieving data and configuration
    default Response reply(get(_)) {
        return response(writeHTMLString(plotHTML()));
    }

    return reply;
}

@synopsis{default HTML wrapper for a chart}
private HTMLElement plotHTML()
    = html([
        div([ // put div here instead of `head` to work around issue
            script([], src="https://cdn.jsdelivr.net/npm/chart.js"),
            script([], src="https://cdn.jsdelivr.net/npm/chartjs-plugin-autocolors")
        ]),
        body([
            div([
                canvas([],id="visualization")
            ]),
            script([
                \data(
                    "var container = document.getElementById(\'visualization\');
                    'const autocolors = window[\'chartjs-plugin-autocolors\'];
                    'Chart.register(autocolors);
                    'fetch(\'/chart\').then(resp =\> resp.json()).then(chart =\> {
                    '   new Chart(container, chart);   
                    '})
                    '")
            ], \type="text/javascript")
        ], style="position: fixed; top:50%; left:50%; transform: translate(-50%, -50%); width:min(75%,800px);")
    ]);
