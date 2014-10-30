@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module vis::web::Chart

import vis::web::BarChart;
import util::HtmlDisplay;
import vis::web::markup::D3;
import vis::web::PlotFunction;

import Prelude;

public int a = 4;

public void chart(rel[num x, num y] r) {
 int n = size(r);
 list[int] d = [i|int i<-[0..n]];
 str body = barChart(y_axis = getYAxis(aggregateMethod="avg", plotFunction="bubble", series="i")
 ,orderRule="x");
 Key2Data q = ("i":[], "x":[],"y":[]);
 int i = 0;
 for (<num x, num y><-r) {
        q["i"]=q["i"]+i; q["x"]=q["x"]+x; q["y"]=q["y"]+y;
        i+=1;
        }
  htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, q
       ));
    }
    
 public void chart(rel[num x, num y, str kind] r) {
 int n = size(r);
 list[int] d = [i|int i<-[0..n]];
 str body = barChart(y_axis = getYAxis(aggregateMethod="avg", plotFunction="bubble", series="i")
 ,orderRule="x");
 Key2Data q = ("i":[], "x":[],"y":[]);
 for (<num x, num y, str z><-r) {
        q["i"]=q["i"]+z; q["x"]=q["x"]+x; q["y"]=q["y"]+y;
        }
  htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, q
       ));
    } 
    
public void chart(rel[str , str] r) {
 int n = size(r);
 list[int] d = [i|int i<-[0..n]];
 str body = barChart(y_axis = getYAxis(aggregateMethod="max", plotFunction="bubble", series="i",
 category=true));
 Key2Data q = ("i":[], "x":[],"y":[]);
 int i = 0;
 for (<str x, str y><-r) {
        q["i"]=q["i"]+i; q["x"]=q["x"]+x; q["y"]=q["y"]+y;
        i+=1;
        }
  htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, q
       ));
    }
    
public void chart(map[str, num] m) {
     str body = barChart(y_axis = getYAxis(aggregateMethod="avg"), orderRule="x");
     list[str] d = [x|str x<-domain(m)];
     htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, ("x": d,"y":[m[i]|str i<-d])
       ));
     }
    
public void chart(map[num, num] m) {
     str body = barChart(y_axis = getYAxis(aggregateMethod="avg"), orderRule="x");
     list[int] d = [x|int x<-domain(m)];
     htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, ("x": d,"y":[m[i]|int i<-d])
       ));
     }
 
 public void chart(map[num, list[num]] ml) {
     str body = barChart(y_axis = getYAxis(aggregateMethod="avg", series= "kind"),
     orderRule="x"); 
     if (isEmpty(ml)) return;
     int n = size(ml[getOneFrom(ml)]);
     list[int] d = [x|int x<-domain(ml)];
     htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, [
        <"x", [*[i|i<-d]|g<-[0..n]]>
       ,<"y", [*[ml[i][g]|i<-d]|g<-[0..n]]>
       ,<"kind", [*[g|i<-d]|g<-[0..n]]>
        ]
       ));
     }
     
 public void chart(map[str, list[num]] ml) {
     str body = barChart(y_axis = getYAxis(aggregateMethod="avg", series= "kind"),
     orderRule="x"); 
     if (isEmpty(ml)) return;
     int n = size(ml[getOneFrom(domain(ml))]);
     list[str] d = [x|str x<-domain(ml)];
     htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, [
        <"x", [*[i|i<-d]|g<-[0..n]]>
       ,<"y", [*[ml[i][g]|i<-d]|g<-[0..n]]>
       ,<"kind", [*[g|i<-d]|g<-[0..n]]>
        ]
       ));
     }     

      
public void chart(rel[map[str, num] xlabel2rank, str ylabel] r) {
 int n = size(r);
 list[int] d = [i|int i<-[0..n]];
 str body = barChart(y_axis = getYAxis(aggregateMethod="count", plotFunction="bar", series=""), orderRule="n");
 Key2Data q = ("i":[], "x":[],"y":[], "n":[]);
 int i = 0;
 for (<map[str, num] x, str y><-r) {
        q["i"]=q["i"]+i; q["x"]=q["x"]+"<getOneFrom(x)>_<x[getOneFrom(x)]>"; q["y"]=q["y"]+y;
        q["n"]=q["n"]+x[getOneFrom(x)];
        i+=1;
        }
  htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, q
       ));
    }
    
public void chart(rel[map[str, num] xlabel2rank, str ylabel, str kind] r) {
 int n = size(r);
 list[int] d = [i|int i<-[0..n]];
 str body = barChart(y_axis = getYAxis(aggregateMethod="count", plotFunction="bar", series="kind"), orderRule="n");
 Key2Data q = ("i":[], "x":[],"y":[], "n":[], "kind":[]);
 int i = 0;
 for (<map[str, num] x, str y, str kind><-r) {
        q["i"]=q["i"]+i; q["x"]=q["x"]+"<getOneFrom(x)>:<x[getOneFrom(x)]>"; q["y"]=q["y"]+y;
        q["n"]=q["n"]+x[getOneFrom(x)];
        q["kind"]=q["kind"]+kind;
        i+=1;
        }
  htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, q
       ));
    }
      
rel[str , str] loc2str(rel[loc , loc] q) {
       return {
              < x.file, y.file > | <loc x, loc y> <- q  
              };
    }
     
rel[map[str, num] , str, str ] loc2str(rel[map[loc, num] , loc, str] q) {
       return { <(getOneFrom(x).file:x[getOneFrom(x)]), 
                    y.file, z > | <map[loc, num] x, loc y, str z> <- q};
    }
 
rel[map[str, num] , str] loc2str(rel[map[loc, num] , loc] q) {
       return {
              <(getOneFrom(x).file:x[getOneFrom(x)]), 
                    y.file > | <map[loc, num] x, loc y> <- q  
              };
    }
    
rel[map[str, num] , map[str,num]] loc2str(rel[map[loc, num] ,map[loc, num]] q) {
       return {
              <(getOneFrom(x).file:x[getOneFrom(x)]), 
                    (getOneFrom(y).file:y[getOneFrom(y)]) > | <map[loc, num] x,  map[loc, num] y> <- q  
              };
    }
    
public void chart(list[tuple[str name, list[num] ticks, num(num) g]] f) {
    plotFunction(f, width = -1,   height = -1, nTickx = 10, nTicky = 10,
    nStep=100, viewWidth = 600,viewHeight= 400);
    }
    
public void chart(list[tuple[str name, lrel[num, num] r]] f) {
    plot(f, width = -1,   height = -1, nTickx = 10, nTicky = 10,
    nStep=100, viewWidth = 600,viewHeight= 400);
    }
       
public void chart(rel[map[loc, num] , loc, str] r) {
    return chart(loc2str(r));
    }
    
 public void chart(rel[map[loc, num] , loc] r) {
    return chart(loc2str(r));
    } 
      
 public void chart(rel[map[loc, num] , map[loc, num]] r) {
    return chart(loc2str(r));
    }
      
 public void chart(rel[map[str labelx, num rangx] , map[str labely, num rangy]] r) {
 int n = size(r);
 list[int] d = [i|int i<-[0..n]];
 str body = barChart(y_axis = getYAxis(aggregateMethod="max", plotFunction="bubble", series=["i","xn","yn"],
 category=true, orderRule="yn"), orderRule="xn");
 Key2Data q = ("i":[], "x":[],"y":[], "xn":[], "yn":[]);
 int i = 0;
 for (<map[str labelx, num rangx] x, map[str labely, num rangy] y><-r) {
        q["i"]=q["i"]+i; q["x"]=q["x"]+getOneFrom(x); q["y"]=q["y"]+getOneFrom(y);
        q["xn"]=q["xn"]+x[getOneFrom(x)]; q["yn"]=q["yn"]+y[getOneFrom(y)];
        i+=1;
        }
  htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, q
       ));
    }

public void chart(rel[loc , loc] r) {
    return chart(loc2str(r));
    }


          

     
public void chartDefault(str s) {htmlDisplay(|tmp:///dimple|, html("","<s>"));}


