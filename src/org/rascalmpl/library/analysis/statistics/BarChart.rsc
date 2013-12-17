@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module analysis::statistics::BarChart
// module BarChart

// import Relation;
// import Set;
import Prelude;
import analysis::statistics::markup::D3;
import analysis::statistics::markup::Dimple;
import lang::json::IO;
import IO;

alias Axis = tuple[str varName, str aggregateMethod, str plotFunction,str series];

public tuple[int width, int height] svgDim = <1200, 800>;

public tuple[int x, int y, int width, int height] chartBounds =
     <60, 30, 800, 400>;

public tuple[int x, int y, int width, int height, str align] legendBounds =
     <60, 10, 800, 20, "right">;
 

@doc{
 Synopsis: 
    Writes the files index.html and data.json in loc location defining a barchart.
    Colnames must be a list containing 3 column names. 
    The rel relation contains the data, it must consist of tuples of length 3.
    More information can be found in: https://github.com/PMSI-AlignAlytics/dimple/wiki
    
    For Example  the data:
    
    Name  Age Sex
    Piet  25   M
    Anne  40   V
    
    must input 
      for colNames ["Name", "Age", "Sex"], and 
      for relation {<"Piet", 25, "M">, <"Anne", 40, "V">}
    }
 
   
private bool isNull(value v) {
    if (str w:=v) return isEmpty(w);
    if (list[value] w:=v) return isEmpty(w);
    return false;
    }
    
public loc barChart(
      loc location
    , tuple[list[str] hd, list[list[value]] t] relation
    , str title="title"
    , value x_axis="x"
    , value orderRule = ""
    , value series=""
    , list[tagColor] assignColor=[]
    , Axis y_axis =<"y","count", "bar","">
    , Axis y_axis2=<"","max", "line","">
    , bool legend = false
    , bool legend2 = false
    , list[dColor] defaultColors = []
   ) 
    {
   list[map[str, value]] jsonData = [(relation.hd[i] : r[i]|i<-[0..size(relation.hd)])|
          list[value] r<-relation.t];        
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
        expr(isNull(defaultColors)?"":chart.defaultColors("myChart", defaultColors))
        ,
        var(("x":expr(chart.addAxis("myChart", "x",x_axis, ""))))
        ,
        expr(axis.addOrderRule("x", orderRule, "false"))
        ,
        var(("y1":expr(chart.addMeasureAxis("myChart", "y", y_axis[0]))))
        , 
        var(("y2":expr(isNull(y_axis2[0])?"null":chart.addMeasureAxis("myChart", "y", y_axis2[0]))))
        ,
        var(("mySeries":expr(chart.addSeries("myChart", y_axis[3],  "dimple.plot.<y_axis[2]>",  expr("[x, y1]")))))
        ,
        var(("mySeries2":expr(isNull(y_axis2[0])?"null":chart.addSeries("myChart", y_axis2[3],  "dimple.plot.<y_axis2[2]>", expr("[x, y2]")))))
        ,
        expr(!legend || isNull(y_axis[3])?"":chart.addLegend("myChart", legendBounds.x, legendBounds.y, 
                                         legendBounds.width, legendBounds.height, 
                                         legendBounds.align, expr("mySeries")))
        ,
        expr(!legend2 || isNull(y_axis2[3])?"":chart.addLegend("myChart", legendBounds.x, legendBounds.y, 
                                         legendBounds.width, legendBounds.height, 
                                         legendBounds.align, expr("mySeries2")))
        ,
        expr(isNull(y_axis[0])?"":"mySeries.aggregate=dimple.aggregateMethod.<y_axis[1]>")                                 
        ,
        expr(chart.assignColor("myChart", assignColor))
        ,
        expr(isNull(y_axis2[0])?"":"mySeries2.aggregate=dimple.aggregateMethod.<y_axis2[1]>")
        ,
        expr(chart.draw("myChart"))
        );
        str h = html(header, body); 
        writeTextJSonFile(location+"data.json", jsonData); 
        writeFile(location+"index.html", h);    
      return location;    
      }
      
