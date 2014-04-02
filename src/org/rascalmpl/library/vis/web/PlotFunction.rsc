@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module vis::web::PlotFunction
import Prelude;
import util::Math;
import lang::json::IO;
import util::HtmlDisplay;
import vis::web::markup::D3;
// import D3;

str labx(int d, num x, num tickx) {
    return "<precision(x+d*tickx, 2)>";
    }

str laby(int d, num y, num ticky) {
    return "<precision(y+d*ticky,2)>";
    }
 
str getColor(map[str, str] colorMap, str name) {
   if (colorMap[name]?) return colorMap[name];
   return "none";
   }
    
str getStyle(map[str, str] styleMap, str name) {
   if (styleMap[name]?) return styleMap[name];
   return "default";
   }   

public void plotFunction(map[str, num(num)] g , list[tuple[num, num]] dots = [], num x = 0, num y = 0, num width = 10.0, num height =10, int nTickx = 10,
    int nTicky = 10, int nStep=100, int viewWidth= 600, int viewHeight = 600, 
    map[str, str] colorMap = (), map[str, str] styleMap = (),
    str style="splines", int symbolSize=20) {
    num tickx = 1.0/nTickx;
    num ticky = 1.0/nTicky;
    num step =  1.0/nStep;
    list[str] xt =[labx(i, x, tickx * width)|int i<-[0..nTickx+1]];
    list[str] yt =[laby(i, y, ticky * height)|int i<-[0..nTicky+1]];
    list[tuple[str, str, str, list[tuple[num, num]]]] w = 
    [<n, getColor(colorMap, n), getStyle(styleMap, n), 
       [<(i*step)*nTickx, (-y+g[n](x+(i*step)*width))*nTicky/height >| i<-[0..nStep+1]]>|n<-g]; 
   tuple[str, str, str, list[tuple[num, num]]] d = 
   <"dots", getColor(colorMap, "dots"), "dots", 
       [<(-x+cx)*nTickx/width, (-y+cy)*nTicky/height >| <cx, cy> <- dots]>;  
    w += d;     
    PlotData p = <"plot", xt, yt, w>;
    plot(p, width=viewWidth, height=viewHeight, style=style, symbolSize = symbolSize);  
    }  
      
 public void plotFunction(num(num) g ...,  list[tuple[num, num]] dots = [], num x = 0, num y = 0, num width = 10.0, num height =10, int nTickx = 10,
    int nTicky = 10, int nStep=100, int viewWidth= 600, int viewHeight = 600,
    map[str, str] colorMap = (), map[str, str] styleMap = (), str style="splines", int symbolSize=20) {
    list[str] names = [];
    map[str, num(num)] r = ();
    int i = 0;
    for (f<-g) {
       r+=("<i>":f);
       i = i+1;
       }
    plotFunction(r, dots = dots, x  = x,  y = y, width = width, height = height, nTickx = nTickx,
    nTicky = nTicky,  nStep=nStep, viewWidth= viewWidth, viewHeight = viewHeight,
    colorMap = colorMap, styleMap = styleMap, style=style, symbolSize=symbolSize);
    }

public void main() {
    
    list[num(num)] g = [
    	num (num x) (num ii) {
    	return num (num x) {return 0.05*ii*x*x;};
    	}(i)
    |int i<-[1,5..20]]; 
    map[str, str] blue = ("<i>":i%2==0?"dots":"splines"|i<-[0..size(g)]);
    int n = 20;
    list[tuple[num, num]] dots = [<arbReal(), arbReal()*2>|int i<-[0..n]];
    plotFunction(g, x= -1, y=0, width = 2, nTickx = 10, height = 2, nTicky = 10, nStep=30, styleMap = blue, style="splines", symbolSize=40,
    dots = dots);
    // plotFunction(num(num x){return -1.00000001;}  , cos, width = 6, y = -1, height = 2, nTickx = 6, nTicky = 4
    // , nStep=100, viewWidth = 600,viewHeight= 400);
    }