module analysis::statistics::examples::M3BarChart

import lang::java::jdt::m3::Core;
import analysis::m3::Core;
import lang::java::m3::TypeSymbol;
import lang::java::m3::Registry;
import Prelude;

import analysis::statistics::BarChart;
import IO;

M3 model;

public rel[value, value, value] jn(rel[value, value] r1, rel[value, value] r2)
=  {<x, y1, z>| <x, y1> <- r1, <y2,z><-r2, y1==y2};

set[list[value]] cv(rel[value, value] r) = {[t[0], t[1]]|t <- r};

set[list[value]] cv(rel[value, value, value] r) = {[t[0], t[1], t[2]]|t <- r};

public rel[value src, value name] declMethods() {
     return {<resolveJava(e[0]), e[1]>|e<-declaredMethods(model)};
     }

list[str] sortSrc() {
     list[tuple[str, int]] t = [<resolveJava(e).file, getFileLength(resolveJava(e))> | e<-classes(model)]; 
     t = sort(t, bool(tuple[str, int] a, tuple[str, int] b) { return a[1]<b[1];});
     // println(t);
     return [e[0]|e<-t];
     }
  
 rel[str, int] sourrceLength() {
     return {<resolveJava(e).file, getFileLength(resolveJava(e))> | e<-classes(model)};
     }

public list[tuple[list[str], set[list[value]]]] getMethodsWorking(loc project) {    
        model = createM3FromEclipseProject(project);
        // model = createM3FromDirectory(project);
        rel[loc name, TypeSymbol typ] methodReturntype = { d| m <- declaredMethods(model), d<-model@types};
        rel[value method, value proc ] methodProc = simplifyFile( 
           {<n, \void()==r?"void":"function">|<n, t> <- methodReturntype, \method(_,_, r,_):=t}
           +
           {<n, "constructor" >|<n, t> <- methodReturntype, \constructor(_,_):=t}
           );
        rel[value src, value method] sourceMethod =  simplifyFile(
          // {d | m <- methods(model), d <- model@declarations, m == d.name}
          declMethods()
          ); 
        rel[value src, value proc] methodProc = compose(sourceMethod, methodProc); 
        rel[str src , int len] srcLength =  sourceLength();
        //  "defs", "src","proc","lengthsrc"  
        return [<["src","defs", "proc"], cv(jn(soceMethod, methodIsProc))>,  
            <["src", "lengthsrc"], cv(sourceLength)>]; 
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
      
public rel[value, value] simplifyLength(rel[value, value] q) {
      if (rel[loc, loc] r :=q) { 
          return {<e[0].file, getFileLength(e[1])>  | e <- r};
          }
      if (rel[loc, value] r :=q)
          return {<e[0].file, e[1]>  | e <- r};
      if (rel[value, loc] r :=q)
          return {<e[0].file, getFileLength(e[1])>  | e <- r};
      return q;
      }

public list[tuple[list[str], set[list[value]]]] r = getMethodsWorking(|project://dotplugin|);

public loc chart = barChart(|project://dotplugin/src/dplugin|,  r, title="First example", 
          x_axis= "src",  y_axis = "defs"
          , y_axis2="lengthsrc"
    , series= "proc"
     , orderRule= sortSrc()
    , assignColor=[<"constructor", "green", "green", 1.0 >]);

/* Use this by entering
import util::HtmlDisplay;
    htmlDisplay(chart);
*/


