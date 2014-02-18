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
import lang::java::jdt::m3::Core;
import analysis::m3::Core;
import lang::java::m3::Registry;
import lang::java::m3::AST;
M3 model;

public void chart(rel[int , int] r) {
 int n = size(r);
 list[int] d = [i|int i<-[0..n]];
 str body = barChart(y_axis = getYAxis(aggregateMethod="max", plotFunction="bubble", series="i"));
 Key2Data q = ("i":[], "x":[],"y":[]);
 int i = 0;
 for (<x, y><-r) {
        q["i"]=q["i"]+i; q["x"]=q["x"]+x; q["y"]=q["y"]+y;
        i+=1;
        }
  htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, q
       ));
    }
    
 public void chart(rel[loc , loc] r) {
 int n = size(r);
 list[int] d = [i|int i<-[0..n]];
 str body = barChart(y_axis = getYAxis(aggregateMethod="max", plotFunction="bubble", series="i",
 category=true));
 Key2Data q = ("i":[], "x":[],"y":[]);
 int i = 0;
 for (<x, y><-r) {
        q["i"]=q["i"]+i; q["x"]=q["x"]+x.file; q["y"]=q["y"]+y.file;
        i+=1;
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
 for (<x, y><-r) {
        q["i"]=q["i"]+i; q["x"]=q["x"]+x; q["y"]=q["y"]+y;
        i+=1;
        }
  htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, q
       ));
    }
    
public void chart(map[int, int] m) {
     str body = barChart(y_axis = getYAxis(aggregateMethod="max"));
     list[int] d = [x|int x<-domain(m)];
     htmlDisplay(publish(
     |tmp:///dimple|
     ,barChartHeader("barChart"), body, ("x": d,"y":[m[i]|int i<-d])
       ));
     }
     
public void chart(map[int, list[int]] ml) {
     str body = barChart(y_axis = getYAxis(aggregateMethod="max", series= "kind")); 
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
     
public void chartDefault(str s) {htmlDisplay(|tmp:///dimple|, html("","<s>"));}

public void initialize(loc project) { 
      model = createM3FromEclipseProject(project);
      }
     
public void main() {
/*
anno rel[loc from, loc to] M3@extends;            // classes extending classes and interfaces extending interfaces
anno rel[loc from, loc to] M3@implements;         // classes implementing interfaces
anno rel[loc from, loc to] M3@methodInvocation;   // methods calling each other (including constructors)
anno rel[loc from, loc to] M3@fieldAccess;        // code using data (like fields)
anno rel[loc from, loc to] M3@typeDependency;     // using a type literal in some code (types of variables, annotations)
anno rel[loc from, loc to] M3@methodOverrides;    // which method override which other methods
anno rel[loc declaration, loc annotation] M3@annotations;
*/
     // chart({<1,2>, <1,3>, <2,4>});
     initialize(|project://dotplugin|);
     // chart({<|file:///a|, |file:///b|>, <|file:///a|, |file:///d|>});
     chart(model@extends);
     // chart((1:[3,4], 2:[4,2], 3:[9,1]));
     }