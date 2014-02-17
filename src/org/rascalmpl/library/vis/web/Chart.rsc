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
import Prelude;

public void chart(rel[int , int] r) {
 str body = barChart(y_axis = getYAxis(aggregateMethod="max", plotFunction="bubble", series="id"));
 rel[int, int] r0 = {};
 rel[int, int] r1= {};
     int i = 0;
     for (<x, y><-r) {
           r0+={<i, x>};
           r1+={<i, y>};
           i+=1;
        }
  htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, "id",<"x", r0>,<"y", r1>
       ));
    }

public void chart(map[int, int] m) {
     str body = barChart(y_axis = getYAxis(aggregateMethod="max"));
     rel[int, int] r = {<x, m[x]>|x<-domain(m)};
     htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, "x",<"y", r>
       ));
     }
     
public void chart(map[int, list[int]] ml) {
     str body = barChart(y_axis = getYAxis(aggregateMethod="max", series= "kind"));
     rel[int, int] r0 = {};
     rel[int, int] r1= {};
     rel[int, int] r2= {}; 
     int i = 0;
     for (int x<-domain(ml)) {
        int j = 0;
        for (y<-ml[x]) {
           r0+={<i, x>};
           r1+={<i, y>};
           r2+={<i, j>};
           i+=1;
           j+=1;
        }
        }
     htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, "id",<"x", r0>,<"y", r1>, <"kind", r2>
       ));
     }
     
public void chartDefault(str s) {htmlDisplay(|tmp:///dimple|, html("","<s>"));}
     
public void main() {
     histogram((1:[3,4], 2:[4,2], 3:[9,1]));
     }