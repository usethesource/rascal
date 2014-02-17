@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module vis::web::BarChart

import Prelude;
import vis::web::markup::D3;
import vis::web::markup::Dimple;
import lang::json::IO;
import IO;

alias YAxis = tuple[str varName, str aggregateMethod, str plotFunction,value series, bool showPercent,
num overrideMin, num overrideMax, bool hidden];

alias ColorAxis = tuple[str varName, value color];

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

public YAxis getYAxis(str varName="y", str aggregateMethod="count", str plotFunction="bar", value series="", bool showPercent=false,
    num overrideMin=0, num overrideMax=0, bool hidden = false) {
    return <varName, aggregateMethod, plotFunction, series, showPercent, overrideMin, overrideMax, hidden>;
    }

private bool isNull(value v) {
    if (str w:=v) return isEmpty(w);
    if (list[value] w:=v) return isEmpty(w);
    return false;
    }
    
list[list[value]] jn(rel[value , value] r) =  [[x[1], x[0]]|x<-r];

list[list[value]] jn(list[list[value]] q, rel[value , value] r) {
    if (isEmpty(q)) return jn(r);
    return [L+[z, y1]| [*L, y1] <- q, <value y2,value z><-r, y1==y2];
    }
    
list[list[value]] jn(rel[value , value] r...) {
   list[list[value]] q= ([]|jn(it, p)|p<-r);
   return [last(e)+head(e, size(e)-1)|e<-q];
}

public str barChartHeader(str title) {
   return Z(title_, (), title)+Z(script_,(src_: "http://d3js.org/d3.v3.min.js"))+
    Z(script_,(src_: "http:dimplejs.org/dist/dimple.v1.1.2.min.js"));
   }
   
int ident = 0;
    
public str barChart(
    str title="barChart"
    , value x_axis="x"
    , ColorAxis colorAxis = <"", "">
    , value orderRule = ""
    , value series=""
    , list[tagColor] assignColor=[]
    , YAxis y_axis =<"y","count", "bar","", false,0, 0, false>
    , YAxis y_axis2=<"","max", "line","", false,0, 0, false >
    , bool legend = false
    , bool legend2 = false
    , list[dColor] defaultColors = []
   ) 
    {
 str  x =  "x<ident>";   
 str y1 = "y1_<ident>";
 str y2 = "y2_<ident>";
 str mySeries1 = "mySeries1_<ident>";
 str mySeries2 = "mySeries2_<ident>";
 str svg = "svg<ident>";
 str myChart = "myChart<ident>";
 ident+=1;
 str body =  Z(h1_, (id_: "header"), title) +
      JavaScript(var((svg: expr(dimple.newSvg("body", svgDim.width, svgDim.height)))))+
      JavaScriptJson("data.json", "error", "dat",
        var((myChart:expr("new <dimple.chart(svg, "dat")>")))
        ,
        expr(chart.setBounds(myChart, chartBounds.x, chartBounds.y, 
                                         chartBounds.width, chartBounds.height ))
        ,                            
        expr(isNull(defaultColors)?"":chart.defaultColors(myChart, defaultColors))
        ,
        var((x:expr(chart.addAxis(myChart, "x",x_axis, ""))))
        ,
        expr(axis.addOrderRule(x, orderRule, "false"))
        ,
        var((y1:expr(chart.addMeasureAxis(myChart, "y", y_axis[0]))))
        , 
        var((y2:expr(isNull(y_axis2[0])?"null":chart.addMeasureAxis(myChart, "y", y_axis2[0]))))
        , 
        var(("colorAxis":expr(isNull(colorAxis[0])?"null":chart.addColorAxis(myChart, 
              colorAxis[0],  colorAxis[1]))))
        ,
        var((mySeries1:expr(chart.addSeries(myChart, y_axis[3],  "dimple.plot.<y_axis[2]>"  
        , expr(isNull(colorAxis[0])?"[<x>, <y1>]":"[<x>, <y1>, colorAxis]")
        ))))
        ,
        var((mySeries2:expr(isNull(y_axis2[0])?"null":chart.addSeries(myChart, y_axis2[3],  "dimple.plot.<y_axis2[2]>", expr("[<x>, <y2>]")))))
        ,
        expr(!legend || isNull(y_axis[3])?"":chart.addLegend(myChart, legendBounds.x, legendBounds.y, 
                                         legendBounds.width, legendBounds.height, 
                                         legendBounds.align, expr(mySeries1)))
        ,
        expr(!legend2 || isNull(y_axis2[3])?"":chart.addLegend(myChart, legendBounds.x, legendBounds.y, 
                                         legendBounds.width, legendBounds.height, 
                                         legendBounds.align, expr(mySeries2)))
        ,
        expr(isNull(y_axis[0])?"":"<mySeries1>.aggregate=dimple.aggregateMethod.<y_axis[1]>")                                 
        ,
        expr(chart.assignColor(myChart, assignColor))
        ,
        expr(isNull(y_axis2[0])?"":"<mySeries2>.aggregate=dimple.aggregateMethod.<y_axis2[1]>")
        ,
        expr(y_axis[4]?"if (<y1>) <y1>.showPercent=<y_axis[4]>":"")
        ,
        expr(y_axis2[4]?"if (<y2>) <y2>.showPercent=<y_axis2[4]>":"")
        ,
        expr(y_axis[5]!=y_axis[6]?"if (<y1>) <y1>.overrideMin=<y_axis[5]>":"")
        ,
        expr(y_axis[5]!=y_axis[6]?"if (<y1>) <y1>.overrideMax=<y_axis[6]>":"")
        ,
        expr(y_axis2[5]!=y_axis2[6]?"if (<y2>) <y2>.overrideMin=<y_axis2[5]>":"")
        ,
        expr(y_axis2[5]!=y_axis2[6]?"if (<y2>) <y2>.overrideMax=<y_axis2[6]>":"")
        ,
        expr(y_axis[7]?"if (<y1>) <y1>.hidden=<y_axis[7]>":"")
        ,
        expr(y_axis2[7]?"if (<y2>) <y2>.hidden=<y_axis2[7]>":"")
        ,
        expr(chart.draw(myChart))
        
        );
        return body;         
      }
      
  public loc publish(loc location, str header, str body, 
       str id   // record identifier
       ,tuple[str name, rel[value, value] t] relation ... ) // data  {<rec.ident, value>...}
      {
      list[str] hd =  id + [r.name|r<-relation];
      list[list[value]] g = jn([q.t|q<-relation]);
      list[map[str, value]] jsonData = [(hd[i] : r[i]|i<-[0..size(hd)])|
      list[value] r<-g]; 
      writeTextJSonFile(location+"data.json", jsonData); 
      writeFile(location+"index.html",  html(header, body));    
      return location;    
      }
      
