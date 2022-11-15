@license{
  Copyright (c) 2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@synopsis{Uses vis.js to show charts}
@description{
This modules provides a simple API to create charts for Rascal
(numerical) data, based on [chart.js](https://chartjs.org/)

This module is quite new and may undergo some tweaks in the coming time.
}
module vis::Charts

import lang::html::IO;
import Content;
import IO;

Content scatterplot(rel[num,num] v, str title="Scatterplot") 
    = content(md5Hash(v), scatterplotServer(v,title=title));

data Chart 
    = chart(
        ChartType \type = scatter(),
        ChartOptions options = chartOptions(),
        ChartData \data = chartData()
    );

data ChartData 
    = chartData(
        list[ChartDataSet] datasets = []
    );

data ChartDataSet
    = chartDataSet(
        list[ChartDataPoint] \data = []
    );

data ChartDataPoint
    = point(num x, num y);

data ChartType
    = scatter()
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

Response (Request) scatterplotServer(rel[num,num] v, str title="Scatterplot") {
    // returns the data to load in the scatter plot as a JSON object
    Response reply(get(/^\/data/)) {
        return response(
            chartData(
                datasets=[
                    chartDataSet(
                        \data=[point(x,y) | <x,y> <- v]
                    )
                ]
            )
        );
    }

    // returns the configuration to use for the scatter plot as a JSON object
    Response reply(get(/^\/config/)) {
        return response(
            chart(
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
    }
    
    // returns the main page that also contains the callbacks for retrieving data and configuration
    default Response reply(get(_)) {
        return response(writeHTMLString(scatterplotHTML()));
    }

    return reply;
}

HTMLElement scatterplotHTML()
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
                    'fetch(\'/data\').then(resp =\> resp.json()).then(data =\> {
                    '   fetch(\'/config\').then(resp =\> resp.json()).then(config =\> {
                    '       config[\'data\'] = data;
                    '       new Chart(container, config);
                    '   })  
                    '})
                    '")
            ], \type="text/javascript")
        ])
    ]);
