@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
// module M3BarChart
module vis::web::examples::M3BarChart

import lang::java::jdt::m3::Core;
import analysis::m3::Core;
import lang::java::m3::TypeSymbol;
import lang::java::m3::Registry;
import lang::java::m3::AST;
import Prelude;

import vis::web::BarChart;
import util::HtmlDisplay;
// import BarChart;
import vis::web::markup::Dimple;
import IO;



/*
list[list[value]] jn(rel[value , value] r) =  [[x[1], x[0]]|x<-r];

list[list[value]] jn(list[list[value]] q, rel[value , value] r) {
    if (isEmpty(q)) return jn(r);
    return [L+[z, y1]| [*L, y1] <- q, <value y2,value z><-r, y1==y2];
    }
    
list[list[value]] jn(rel[value , value] r...) {
   list[list[value]] q= ([]|jn(it, p)|p<-r);
   return [last(e)+head(e, size(e)-1)|e<-q];
}
*/

/* Print number of statements */

rel[str, int] methodSizeF(M3 model) {
    rel[str, int] r={};
    for (e<-declaredMethods(model)) { 
        Declaration ast = getMethodASTEclipse(e[1], model = model);
        if (\method(_, str name, _, _, Statement impl):=ast) {
             int n = 1;
             if (\block(list[Statement] ls):=impl) { n = size(ls);}
              r+=<e[1].path, n>;   
         }
         if (\constructor(str name, _, _, Statement impl):=ast) {
             int n = 1;
             if (\block(list[Statement] ls):=impl) { n = size(ls);}
             r+=<e[1].path, n>;   
             }                          
        }                              
    return r;
    }
// \constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)
// \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)
public rel[str method, str src] methodSrcF(M3 model) {
     return  {<e[1].path, resolveJava(e[0]).file>|e<-declaredMethods(model)};
     }

list[str] sortSrc(M3 model) {
     list[tuple[str, int]] t = [<resolveJava(e).file, getFileLength(resolveJava(e))> | e<-classes(model)];
     t = sort(t, bool(tuple[str, int] a, tuple[str, int] b) { return a[1]<b[1];});
     // println(t);
     return [e[0]|e<-t];
     }
  
 rel[int, str] lengthSource(M3 model) {
     return {<getFileLength(resolveJava(e)), resolveJava(e).file> | e<-classes(model)};
     }
     
rel[str method, str src] methodSrc ={};
rel[str method, int len] methodLength = {};
rel[str method, value proc ] methodProc = {};
rel[str, int] methodSize = {};
list[str] orderRule = [];


// \method(loc decl, list[TypeSymbol] typeParameters, TypeSymbol returnType, list[TypeSymbol] parameters)
public void initialize(loc project) { 
        M3 model = createM3FromEclipseProject(project);
        rel[loc name, TypeSymbol typ] methodReturntype = { d| m <- declaredMethods(model), d<-model@types};
        methodProc = 
           {<n.path, \void()==r?"void":"function">|<n, t> <- methodReturntype, \method(_,_, r,_):=t}
           +
           {<n.path, "constructor" >|<n, t> <- methodReturntype, \constructor(_,_):=t}
           ; 
        methodSrc =  methodSrcF(model);
        methodSize = methodSizeF(model);
        rel[int len, str src] lengthSrc =  lengthSource(model);
        methodLength = invert(lengthSrc o invert(methodSrc)); 
        orderRule= sortSrc(model);
}

/* Use this by entering
import util::HtmlDisplay;
*/
public void main() {
    initialize(|project://ambidexter|);
    str body1 = barChart(
    title="First example" 
    ,x_axis = "src"
    ,y_axis =getYAxis(varName="methodSize",aggregateMethod="max", plotFunction="bubble",series=["method","proc"])  
    ,orderRule= orderRule  
    ,legend=true
    // ,colorAxis=<"methodSize","">
    // ,defaultColors=[getColor(fill="blue")]
    );
    str body2 = barChart( 
     title="Second example" 
    ,x_axis = "src"
    ,y_axis = getYAxis(varName="method",series= "proc")
    ,y_axis2 =getYAxis(varName="methodSize",aggregateMethod="max", plotFunction= "line")
    ,orderRule= orderRule
    ,assignColor=[getTagColor("constructor", fill="green", stroke="green")]
    ,defaultColors=[
    getColor(fill="blue", stroke="blue"), 
    getColor(fill="pink", stroke="pink"),
    getColor(fill="red", stroke="red"),
    getColor(fill="antiquewhite", stroke="antiquewhite")
    ]
    ,legend = true
    );
    htmlDisplay(publish(
        // |project://chart/src/m3|,
        |file:///tmp/m3|,
        barChartHeader("barChart"), body1+body2,
    "method"
    ,<"src", methodSrc>
    ,<"length", methodLength>
    ,<"proc", methodProc>
    ,<"methodSize", methodSize>));
    }



