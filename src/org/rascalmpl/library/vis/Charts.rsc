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
import IO;

@synopsis{Produces a scatterplot from a binary numerical relation.}
Content scatterplot(rel[num,num] v, str title="Scatterplot") 
    = content(md5Hash(v), scatterplotServer(v,title=title));

@synopsis{Produces a scatterplot from several binary numerical relation.}
@description{
See ((chartDataSet)) on how to produce a ChartDataSet from a binary numerical relation.
}
Content scatterplots(set[ChartDataSet] sets, str title="Scatterplots")
    = content(md5Hash(sets), scatterplotsServer(sets, title=title));

Content barChart(rel[str label, num val] d, str title="Bar Chart")
    = content(md5Hash(d), barchartServer(d, title=title));

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

data ChartDataSet(str label="undefined")
    = chartDataSet(list[value] \data)
    ;

@synopsis{convert a binary relation `rel[num,num]` to a ChartDataSet}
ChartDataSet chartDataSet(str label, rel[num,num] r)
    = chartDataSet([point(x,y) | <x,y> <- r],
        label=label
    );

data ChartDataPoint
    = point(num x, num y);

data ChartType
    = scatter()
    | bar()
    ;

data ChartOptions  
    = chartOptions(
        bool responsive=true,
        ChartPlugins plugins = chartPlugins()  
    );

data ChartPlugins
    = chartPlugins(
        ChartTitle title = chartTitle(),
        ChartLegend legend = chartLegend()
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

Response(Request) barchartServer(rel[str,num] d, str title="Bar Chart")
    = chartServer(
        chart(
            \type=bar(),
            \data=chartData(
                labels=[l | <l,_> <- d],
                datasets=[
                    chartDataSet([n | <_, n> <- d])
                ]
            ),
            options=chartOptions(
                responsive=true,
                plugins=chartPlugins(
                    legend=chartLegend(
                        position=top()
                    ),
                    title=chartTitle(
                        display=true,
                        text=title
                    )
                )
            )
        )
    );
Response (Request) scatterplotServer(rel[num,num] v, str title="Scatterplot") 
    = scatterplotsServer({chartDataSet(title, v)}, title=title);

Response(Request) scatterplotsServer(set[ChartDataSet] sets, str title="Scatterplots")
    = chartServer(
        chart(
            \data=chartData(
                datasets=[*sets]
            ),
            \type=scatter(),
            options=chartOptions(
                responsive=true,
                plugins=chartPlugins(
                    legend=chartLegend(
                        position=top()
                    ),
                    title=chartTitle(
                        display=true,
                        text=title
                    )
                )
            )
        )
    );

private HTMLElement plotHTML()
    = html([
        head([
            script([], src="https://cdn.jsdelivr.net/npm/chart.js")
        ]),
        body([
            div([
                canvas([],id="visualization")
            ]),
            script([
                \data(
                    "var container = document.getElementById(\'visualization\');
                    'fetch(\'/chart\').then(resp =\> resp.json()).then(chart =\> {
                    '   new Chart(container, chart);
                    '})
                    '")
            ], \type="text/javascript")
        ])
    ]);
