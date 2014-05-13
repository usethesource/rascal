module vis::web::BarChart

import Prelude;
import vis::web::markup::D3;
import vis::web::markup::Dimple;
import lang::json::IO;
import IO;


alias YAxis = tuple[str varName, str aggregateMethod, str plotFunction,value series, bool showPercent,
num overrideMin, num overrideMax, bool hidden, bool category, str orderRule];

alias ColorAxis = tuple[str varName, value color];

public alias Table = list[tuple[str name, list[value] d]];

public alias Key2Data = map[str name, list[value] d];

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
    num overrideMin=0, num overrideMax=0, bool hidden = false, bool category = false, str orderRule="") {
    return <varName, aggregateMethod, plotFunction, series, showPercent, overrideMin, overrideMax, hidden, category, orderRule>;
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
   return W3(title_, (), title)+W3(script_,(src_: "http://d3js.org/d3.v3.min.js"))+
    W3(script_,(src_: "http:dimplejs.org/dist/dimple.v1.1.2.min.js"));
   }
   
int ident = 0;
    
public str barChart(
    str title="barChart"
    , value x_axis="x"
    , ColorAxis colorAxis = <"", "">
    , value orderRule = ""
    , value series=""
    , list[tagColor] assignColor=[]
    , YAxis y_axis =<"y","count", "bar","", false,0, 0, false, false, "">
    , YAxis y_axis2=<"","max", "line","", false,0, 0, false, false, "" >
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
 
 str body =  W3(h1_, (id_: "header"), title) +
      JavaScript(<svg, dimple.newSvg("body", svgDim.width, svgDim.height)>)+
      JavaScriptJson("\"data.json\"", "error", "dat"
         ,
        <myChart, "new <dimple.chart(svg, "dat")>">
        ,
        <chart.setBounds(myChart, chartBounds.x, chartBounds.y, 
                                         chartBounds.width, chartBounds.height)>                                    
        ,                            
        <isNull(defaultColors)?"":chart.defaultColors(myChart, defaultColors)>
        ,
        <x, chart.addCategoryAxis(myChart, "x",x_axis)>
        ,
        <axis.addOrderRule(x, orderRule, "false")>
  
        ,
        <y1,y_axis[8]?chart.addCategoryAxis(myChart, "y",y_axis[0]):chart.addMeasureAxis(myChart, "y", y_axis[0])>
        , 
        <y2, isNull(y_axis2[0])?"null":
              y_axis2[8]?chart.addCategoryAxis(myChart, "y",y_axis2[0]):
              chart.addMeasureAxis(myChart, "y", y_axis2[0])>
        , 
        <"colorAxis", isNull(colorAxis[0])?"null":chart.addColorAxis(myChart, 
              colorAxis[0],  colorAxis[1])>
             
        ,
        <!isNull(y_axis[9])?axis.addOrderRule(y1, y_axis[9], "false"):"">
        ,
        <!isNull(y_axis2[9])?axis.addOrderRule(y2, y_axis2[9], "false"):"">
        ,
        <mySeries1,chart.addSeries(myChart, y_axis[3],  "dimple.plot.<y_axis[2]>"  
        , isNull(colorAxis[0])?<"[<x>, <y1>]">:<"[<x>, <y1>, colorAxis]">)
        >
        ,
        <mySeries2, isNull(y_axis2[0])?"null":chart.addSeries(myChart, y_axis2[3],  
              "dimple.plot.<y_axis2[2]>", <"[<x>, <y2>]">)
        >
        ,
        <!legend || isNull(y_axis[3])?"":chart.addLegend(myChart, legendBounds.x, legendBounds.y, 
                                         legendBounds.width, legendBounds.height, 
                                         legendBounds.align, <mySeries1>)>
        ,
        <!legend2 || isNull(y_axis2[3])?"":chart.addLegend(myChart, legendBounds.x, legendBounds.y, 
                                         legendBounds.width, legendBounds.height, 
                                         legendBounds.align, <mySeries2>)>
        ,
        <isNull(y_axis[0])?"":"<mySeries1>.aggregate=dimple.aggregateMethod.<y_axis[1]>">                                 
        ,
        <chart.assignColor(myChart, assignColor)>
        ,
        <isNull(y_axis2[0])?"":"<mySeries2>.aggregate=dimple.aggregateMethod.<y_axis2[1]>">
        ,
        <y_axis[4]?"if (<y1>) <y1>.showPercent=<y_axis[4]>":"">
        ,
        <y_axis2[4]?"if (<y2>) <y2>.showPercent=<y_axis2[4]>":"">
        ,
        <y_axis[5]!=y_axis[6]?"if (<y1>) <y1>.overrideMin=<y_axis[5]>":"">
        ,
        <y_axis[5]!=y_axis[6]?"if (<y1>) <y1>.overrideMax=<y_axis[6]>":"">
        ,
        <y_axis2[5]!=y_axis2[6]?"if (<y2>) <y2>.overrideMin=<y_axis2[5]>":"">
        ,
        <y_axis2[5]!=y_axis2[6]?"if (<y2>) <y2>.overrideMax=<y_axis2[6]>":"">
        ,
        <y_axis[7]?"if (<y1>) <y1>.hidden=<y_axis[7]>":"">
        ,
        <y_axis2[7]?"if (<y2>) <y2>.hidden=<y_axis2[7]>":"">
        ,
        <chart.draw(myChart)>
        );
        return body;         
      }
      
  public loc publish(loc location, str header, str body, 
       str id   // record identifier
       ,tuple[str name, rel[value, value] t] relation ... ) // data  {<rec.ident, value>...}
      {
      list[str] hd =  id + [r.name|r<-relation];
      list[list[value]] g = jn([q.t|q<-relation]);
      list[list[value]] g1 = [[g[j][i]|j<-[0..size(g)]]|i<-[0..size(hd)]]; 
      // println(g1);
      Table table = [<hd[i], g1[i]>|i<-[0..size(hd)]];
      // println(table);
      return publish(location, header, body, table);
      }
  
  list[map[str, value]] convert(Table table) {   
      if (isEmpty(table)) return [];
      int n = size(head(table).d);
      //println(table);
      // println(n);
      list[map[str, value]] r =[];
      for (i<-[0..n]) {
          r+=(h:r[i]|<str h, list[value] r><-table);
          }
      return r;
      }
      
 list[map[str, value]] convert(Key2Data table) {
      if (isEmpty(table)) return [];
      int n = size(table[getOneFrom(table)]);
      list[map[str, value]] r =[];
      for (i<-[0..n]) {
          r+=(d:table[d][i]|d<-table);
          }
      return r;
      }     
      
  public loc publish(loc location, str header, str body, Table table)  {
     list[map[str, value]] jsonData = convert(table);
     writeTextJSonFile(location+"data.json", jsonData); 
     writeFile(location+"index.html",  html(header, body));    
     return location; 
     }
     
  public loc publish(loc location, str header, str body, Key2Data table)  {
     list[map[str, value]] jsonData = convert(table);
     writeTextJSonFile(location+"data.json", jsonData); 
     writeFile(location+"index.html",  html(header, body));    
     return location; 
     }
