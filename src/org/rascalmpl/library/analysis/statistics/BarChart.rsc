@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module analysis::statistics::BarChart

// import Relation;
// import Set;
import Prelude;
import analysis::statistics::markup::D3;
import analysis::statistics::markup::Dimple;
import lang::json::IO;
import IO;


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
 
private set[list[value]] jn(rel[value ca, value x] r) { 
    return {[ca, x]|<ca, x><-r};  
    }

private set[list[value]] jn(set[list[value]] r1, rel[value ca2 , value y] r2) {
   return {t+y|t<-r1, <ca2, y><-r2, t[0]== ca2};  
}

private set[list[value]] jn(list[rel[value, value]] rs) {
   if (isEmpty(rs)) return {};
   return (jn(rs[0])|jn(it, e) | e<-tail(rs));  
   }
   
private bool isNull(value v) {
    if (str w:=v) return isEmpty(w);
    if (list w:=v) return isEmpty(w);
    return false;
    }
    
public loc barChart(loc location, list[tuple[list[str] hd, set[list[value]] t]] relation, str title="title", value x_axis="x", value y_axis="y", 
 value orderRule = "", value series="", 
    list[tagColor] assignColor=[], value y_axis2="") {
    // println(relation);
   list[map[str, value]] jsonData = [*[(q.hd[i] : r[i]|i<-[0..size(q.hd)])|
          r<-q.t]|
        tuple[list[str] hd, set[list[value]] t] q<-relation]; 
 // println(jsonData);      
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
        var(("x":expr(chart.addAxis("myChart", "x",x_axis, ""))))
        ,
        expr(axis.addOrderRule("x", orderRule, "false"))
        ,
        var(("y1":expr(chart.addMeasureAxis("myChart", "y", y_axis))))
        , 
        var(("y2":expr(isNull(y_axis2)?"null":chart.addMeasureAxis("myChart", "y", y_axis2))))
        ,
        var(("mySeries":expr(chart.addSeries("myChart", series,  "dimple.plot.bar",  expr("[x, y1]")))))
        ,
        var(("mySeries2":expr(isNull(y_axis2)?"":chart.addSeries("myChart", "",  "dimple.plot.line", expr("[x, y2]")))))
        ,
        expr(isNull(series)?"":chart.addLegend("myChart", legendBounds.x, legendBounds.y, 
                                         legendBounds.width, legendBounds.height, 
                                         legendBounds.align))
        ,
        expr(chart.assignColor("myChart", assignColor))
        ,
        expr(isNull(y_axis2)?"":"mySeries2.aggregate=dimple.aggregateMethod.max")
        ,
        expr(chart.draw("myChart"))
        );
        str h = html(header, body); 
        writeTextJSonFile(location+"data.json", jsonData); 
        writeFile(location+"index.html", h);    
      return location;    
      }
      
