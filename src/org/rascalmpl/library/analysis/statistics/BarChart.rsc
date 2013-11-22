@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module analysis::statistics::BarChart

import Relation;
import Set;
import analysis::statistics::D3;
import analysis::statistics::Dimple;
import lang::json::IO;
import IO;


public tuple[int width, int height] svgDim = <590, 700>;

public tuple[int x, int y, int width, int height] chartBounds =
     <60, 30, 510, 400>;

public tuple[int x, int y, int width, int height, str align] legendBounds =
     <60, 10, 510, 20, "right">;
 
// For a working example  see org.rascalmpl.eclipse.library.demo.DrawBarChart.rsc
 
public void barChart(loc location, str title, str x, str y, list[map[str, value]] jsonData,
 value series) {
 str header  = Z(title_, (), title)+
 Z(script_,(src_: "http://d3js.org/d3.v3.min.js"))+
 Z(script_,(src_: "http:dimplejs.org/dist/dimple.v1.1.2.min.js"));
 str body =  Z(h1_, (id_: "header"), title) +
      JavaScript(var((svg_: expr(dimple.newSvg("body", svgDim.width, svgDim.height)))))+
      JavaScriptJson("data.json", "error", "dat",
        var(("myChart":expr("new <dimple.chart("svg", "dat")>")))
        ,
        expr(chart.setBounds("myChart", chartBounds.x, chartBounds.y, 
                                         chartBounds.width, chartBounds.height ))
        ,
        var(("x":expr(chart.addCategoryAxis("myChart", "x",x))))
        ,
        expr(axis.addOrderRule("x", x, "false"))
        ,
        expr(chart.addMeasureAxis("myChart", "y", y))
        ,
        expr(chart.addSeries("myChart", series,  "dimple.plot.bar"))
        ,
        expr(chart.addLegend("myChart", legendBounds.x, legendBounds.y, 
                                         legendBounds.width, legendBounds.height, 
                                         legendBounds.align))
        ,
        expr(chart.draw("myChart"))
        );
        str h = html(header, body); 
        writeTextJSonFile(location+"data.json", jsonData); 
        writeFile(location+"index.html", h);    
      // println(header);
      // println(body);
      // htmlDisplay(location, html(
      //   header
      //,body), dat);      
      }