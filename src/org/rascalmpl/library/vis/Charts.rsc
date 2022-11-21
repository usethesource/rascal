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
using ((AlgebraicDataType))s of Rascal. Documentation about chart.js should be easy
to interpret.

This module is quite new and may undergo some tweaks in the coming time.
}
module vis::Charts

import lang::html::IO;
import Content;
import Set;
import List;

@synopsis{A scatterplot from a binary numerical list relation.}
Content scatterplot(lrel[num x,num y] v, str title="Scatterplot") 
    = content(title, chartServer(chartData(title, v), \type=scatter(), title=title));

@synopsis{A scatterplot from several binary numerical list relations.}
Content scatterplot(list[str] labels, lrel[num x,num y] values ..., str title="Scatterplots")
    = content(title, chartServer(chartData(labels, values), \type=scatter(), title=title));

@synopsis{A scatterplot from a binary numerical list relation.}
Content scatterplot(rel[num x ,num y] v, str title="Scatterplot") 
    = content(title, chartServer(chartData(title, v), \type=scatter(), title=title));

@synopsis{A scatterplot from several binary numerical list relations.}
Content scatterplot(list[str] labels, rel[num x,num y] values ..., str title="Scatterplots")
    = content(title, chartServer(chartData(labels, values), \type=scatter(), title=title));

@synopsis{A bubblechart from a binary numerical list relation.}
@pitfalls{
* the radius is in raw pixels rather than scaled to the chart's axis
}
Content bubblechart(lrel[num x,num y, num r] v, str title="Scatterplot") 
    = content(title, chartServer(chartData(title, v), \type=bubble(), title=title));

@pitfalls{
* the radius is in raw pixels rather than scaled to the chart's axis
}
@synopsis{A bubblechart from several binary numerical list relations.}
Content bubblechart(list[str] labels, lrel[num x,num y, num r] values ..., str title="Scatterplots")
    = content(title, chartServer(chartData(labels, values), \type=scatter(), title=title));

@synopsis{A bubblechart from a binary numerical list relation.}
@pitfalls{
* the radius is in raw pixels rather than scaled to the chart's axis
}
Content bubblechart(rel[num x,num y, num r] v, str title="Scatterplot") 
    = content(title, chartServer(chartData(title, v), \type=scatter(), title=title));

@synopsis{A bubblechart from several binary numerical list relations.}
@pitfalls{
* the radius is in raw pixels rather than scaled to the chart's axis
}
Content bubblechart(list[str] labels, rel[num x,num y, num r] values ..., str title="Scatterplots")
    = content(title, chartServer(chartData(labels, values), \type=scatter(), title=title));    

Content barChart(rel[str label, num val] values, str title="Bar Chart")
    = content(title, chartServer(chartData(values), \type=\bar(), title=title));

Content barChart(lrel[str label, num val] values, str title="Bar Chart")
    = content(title, chartServer(chartData(values), \type=\bar(), title=title));

Content barChart(list[str] labels, rel[str label, num val] values..., str title="Bar Chart")
    = content(title, chartServer(chartData(labels, values), \type=\bar(), title=title));

Content barChart(list[str] labels, lrel[str label, num val] values..., str title="Bar Chart")
    = content(title, chartServer(chartData(labels, values), \type=\bar(), title=title));

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

ChartDataSet chartDataSet(str label, lrel[num x,num y, num r] r)
    = chartDataSet([point(x,y,r=rad) | <x,y,rad> <- r],
        label=label
    );    

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

ChartData chartData(str label, lrel[num,num] values)
    = chartData(
        datasets=[
            chartDataSet(label, values)
        ]
    );

ChartData chartData(str label, lrel[num x,num y, num r] values)
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
    
data Chart 
    = chart(
        ChartType \type = scatter(),
        ChartOptions options = chartOptions(),
        ChartData \data = chartData()
    );

data ChartData 
    = chartData(
        list[str]  labels=[],
        list[ChartDataSet] datasets = []
    );

data ChartDataSet(
        str label="undefined",
        list[str] backgroundColor=[],
        list[str] borderColor=[],
        list[str] color=[]
    )
    = chartDataSet(list[value] \data)
    ;

data ChartDataPoint
    = point(num x, num y, num r = 0);

data ChartType
    = scatter()
    | bar()
    | bubble()
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
        ChartColors colors = chartColors()
    );

data ChartLegend   
    = chartLegend(
        LegendPosition position = top()
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
Response(Request) chartServer(ChartData \data, ChartType \type=\bar(), str title="Chart")
    = chartServer(
        chart(
            \type=\type,
            \data=\data,
            options=chartOptions(
                responsive=true,
                plugins=chartPlugins(
                    legend=chartLegend(
                        position=top()
                    ),
                    title=chartTitle(
                        display=true,
                        text=title
                    ),
                    colors=chartColors(
                        enabled=true
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
HTMLElement plotHTML()
    = html([
        div([
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
        ])
    ]);

HTMLElement hhead(list[HTMLElement] elems) = HTMLElement::head(elems);