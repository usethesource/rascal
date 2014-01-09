@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
// module M3BarChart
module analysis::statistics::examples::M3BarChart

import lang::java::jdt::m3::Core;
import analysis::m3::Core;
import lang::java::m3::TypeSymbol;
import lang::java::m3::Registry;
import lang::java::m3::AST;
import Prelude;

import analysis::statistics::BarChart;
import util::HtmlDisplay;
// import BarChart;
import analysis::statistics::markup::Dimple;
import IO;

M3 model;

public list[list[value]] jn(rel[value , value] r) =  [[x[1], x[0]]|x<-r];

public list[list[value]] jn(list[list[value]] q, rel[value , value] r) {
    if (isEmpty(q)) return jn(r);
    return [L+[z, y1]| [*L, y1] <- q, <value y2,value z><-r, y1==y2];
    }
    
public list[list[value]] jn(rel[value , value] r...) {
   list[list[value]] q= ([]|jn(it, p)|p<-r);
   return [last(e)+head(e, size(e)-1)|e<-q];
}

/* Print number of statements */

rel[loc, int] methodSize() {
    rel[loc, int] r={};
    for (e<-declaredMethods(model)) { 
        Declaration ast = getMethodASTEclipse(e[1], model = model);
        if (\method(_, str name, _, _, Statement impl):=ast) {
             int n = 1;
             if (\block(list[Statement] ls):=impl) { n = size(ls);}
              r+=<e[1], n>;   
         }
         if (\constructor(str name, _, _, Statement impl):=ast) {
             int n = 1;
             if (\block(list[Statement] ls):=impl) { n = size(ls);}
             r+=<e[1], n>;   
             }                          
        }                              
    return r;
    }
// \constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)
// \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)
public rel[loc method, str src] declMethods() {
     methodSize();
     return  {<e[1], e[0].file>|e<-declaredMethods(model)};
     }

list[str] sortSrc() {
     list[tuple[str, int]] t = [<e.file, getFileLength(resolveJava(e))> | e<-classes(model)];
     t = sort(t, bool(tuple[str, int] a, tuple[str, int] b) { return a[1]<b[1];});
     return [e[0]|e<-t];
     }
  
 rel[int, str] lengthSource() {
     return {<getFileLength(resolveJava(e)), e.file> | e<-classes(model)};
     }

// \method(loc decl, list[TypeSymbol] typeParameters, TypeSymbol returnType, list[TypeSymbol] parameters)
public tuple[list[str], list[list[value]]] getMethodsWorking(loc project) { 
        model = createM3FromEclipseProject(project);
        rel[loc name, TypeSymbol typ] methodReturntype = { d| m <- declaredMethods(model), d<-model@types};
        rel[value method, value proc ] methodProc = 
           {<n, \void()==r?"void":"function">|<n, t> <- methodReturntype, \method(_,_, r,_):=t}
           +
           {<n, "constructor" >|<n, t> <- methodReturntype, \constructor(_,_):=t}
           ;
       
        rel[loc method, str src] methodSrc =  declMethods();
        rel[int len, str src] lengthSrc =  lengthSource();
        rel[loc method, int len] methodLength = invert(lengthSrc o invert(methodSrc)); 
        list[list[value]] q = jn(methodSrc, methodLength, methodProc, methodSize()); 
        return <["method", "src", "length", "proc", "methodSize"], q>; 
}

public rel[value, value] simplifyFile(rel[value, value] q) {
      if (rel[loc, loc] r :=q)
          return {<e[0].file, e[1].file>  | e <- r};
      if (rel[loc, value] r :=q)
          return {<e[0].file, e[1]>  | e <- r};
      if (rel[value, loc] r :=q)
          return {<e[0], e[1].file> | e <- r};
      return q;
      }
      

public tuple[list[str], list[list[value]]] dataMatrix = getMethodsWorking(|project://dotplugin|);


public loc chart = barChart(|project://chart/src/m3|
    ,dataMatrix
    ,title="First example" 
    ,x_axis = "src"
    ,y_axis = <"method","count", "bar", "proc">
    ,y_axis2 =<"methodSize","max", "line", "">
    ,orderRule= sortSrc()
    ,assignColor=[getTagColor("constructor", fill="green", stroke="green")]
    ,legend = true
    );

/*
public loc otherChart = barChart(|project://chart/src/m3|
    ,dataMatrix
    ,title="Second example" 
    ,x_axis = "src"
    ,y_axis =<"methodSize","max", "bubble","method">
    ,orderRule= sortSrc()
    ,defaultColors=[getColor(fill="blue")]
    );
*/

/* Use this by entering
import util::HtmlDisplay;
*/
public void main() {
    htmlDisplay(chart);
    }



