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
   

public tuple[int x, int y, int mx, int my] rng(list[lrel[num, num]] dots) {
    tuple[num x, num y, num mx, num my] zz = <99999,99999, 0, 0>;
    for (d<-dots) {
    tuple[num x, num y, num mx, num my] r = <min(d<0>), min(range(d)), max(d<0>), max(range(d))>;
    // println(r);
    zz.x = min(zz.x, r.x);  zz.y = min(zz.y, r.y); 
    zz.mx = max(zz.mx, r.mx); zz.my = max(zz.my, r.my);
    }
    // println(zz);
    return <floor(zz.x)-1, floor(zz.y)-1, ceil(zz.mx)+1, ceil(zz.my)+1>;
   } 
   
tuple[str name, lrel[num, num] f] pf(tuple[str name, list[num] ticks, num(num) g] t) {
   return <t.name, [<x, t.g(x)>|x<-t.ticks]>;
   }
   
public void plot(tuple[str name, lrel[num, num] f] dots ..., num x = 0, num y = 0, num width = -1, num height = -1, int nTickx = 10,
    int nTicky = 10, int nStep=100, int viewWidth= 600, int viewHeight = 600, 
    map[str, str] colorMap = (), map[str, str] styleMap = (),
    str style="points", int symbolSize=20, list[str] xticks =[], list[str] yticks =[]) {
    if (!isEmpty(xticks)) nTickx = size(xticks);
    if (!isEmpty(yticks)) nTicky = size(yticks);
    num tickx = 1.0/(nTickx-1);
    num ticky = 1.0/(nTicky-1);
    num step =  1.0/nStep;  
    num w = width;  num h = height;   
    if (w<0) {
        tuple[int x, int y, int mx, int my] r = rng(dots.f);    
        x = r.x; y = r.y; 
        w = r.mx - r.x;
        if (h<0) h = r.my - r.y;
        }
    // println(x);
   list[str] xt = xticks;
   if (isEmpty(xt)) xt = [labx(i, x, tickx * w)|int i<-[0..nTickx]];
   list[str] yt = yticks;
   if (isEmpty(yt)) yt = [laby(i, y, ticky * h)|int i<-[0..nTicky]];     
   list[tuple[str, str, str, list[tuple[num, num]]]] q = 
   [<d.name, getColor(colorMap, d.name), getStyle(styleMap, d.name), 
       [<(-x+cx)*(nTickx-1)/w, (-y+cy)*(nTicky-1)/h >| <cx, cy> <- d.f]>|d<-dots];       
    PlotData p = <"plot", "plot", xt, yt, q>;
    plot(p, width=viewWidth, height=viewHeight, style=style, symbolSize = symbolSize);  
    }
      
public str plotHtml(loc location, str tagName, tuple[str name, lrel[num, num] f] dots ..., num x = 0, num y = 0, num width = -1, num height = -1, int nTickx = 10,
    int nTicky = 10, int nStep=100, int viewWidth= 600, int viewHeight = 600, 
    map[str, str] colorMap = (), map[str, str] styleMap = (),
    str style="dots", int symbolSize=20, list[str] xticks = [], list[str] yticks = [], num factor = 1.0,
    bool x_axis = true, bool y_axis = true, int margin = 60) {
    if (!isEmpty(xticks)) nTickx = size(xticks);
    if (!isEmpty(yticks)) nTicky = size(yticks);
    num tickx = 1.0/nTickx;
    num ticky = 1.0/nTicky;
    num step =  1.0/nStep; 
    num w = width;  num h = height;  
    if (w<0) {
        tuple[int x, int y, int mx, int my] r = rng(dots.f);
        x = r.x; y = r.y; w = r.mx - r.x; 
        if (height<0) h = r.my - r.y;
        }
    list[str] xt = xticks;
    if (isEmpty(xt)) xt = [labx(i, x, tickx * w)|int i<-[0..nTickx+1]];
    list[str] yt = yticks;
    if (isEmpty(yt)) yt = [laby(i, y, ticky * h)|int i<-[0..nTicky+1]];   
   list[tuple[str, str, str, list[tuple[num, num]]]] q = 
   [<d.name, getColor(colorMap, d.name), getStyle(styleMap, d.name), 
       [<(-x+cx)*nTickx/w, (-y+cy)*nTicky/h >| <cx, cy> <- d.f]>|d<-dots];       
    PlotData p = <tagName, tagName, xt, yt, q>;
    str g = plotEl(location, 
         p, width = viewWidth
     , height = viewHeight 
     , style = style
     , symbolSize = symbolSize
     , factor =  1.0, margin =margin
    , x_axis = x_axis, y_axis = y_axis);
    // println(xt);
    // println(yt);
    return g;
    }  
public void plotFunction(tuple[str name, list[num] ticks, num(num) g] t..., str name = "plotFunction",  num x = 0, num y = 0, num width = -1, num height = -1, int nTickx = 10,
    int nTicky = 10, int nStep=100, int viewWidth= 600, int viewHeight = 600, 
    map[str, str] colorMap = (), map[str, str] styleMap = (),
    str style="splines", int symbolSize=20, list[str] xticks = []
        , list[str] yticks = []) {
    list[tuple[str name, lrel[num, num] r]] dt = [pf(<d.name, d.ticks, d.g>)|d<-t];
    plot(dt, x  = x,  y = y, width = width, height = height, nTickx = nTickx,
    nTicky = nTicky,  nStep=nStep, viewWidth= viewWidth, viewHeight = viewHeight,
    colorMap = colorMap, styleMap = styleMap, style=style, symbolSize=symbolSize
    ,xticks = xticks, yticks = yticks);
    }
    
public void main() {  
    list[tuple[str name, lrel[num, num] r]] dt = [pf(<"<i+2>", [x|num x<-[-1,-0.99..1]],
    	num (num x) (num ii) {
    	return num (num x) {return 0.05*ii*x*x;};
    	}(i)>)
    |int i<-[1,5..20]]; 
    println(dt);
    map[str, str] blue = ("<i>":(i%2==0?"blue":"red")|i<-[0..2]);
    int n = 20;
    tuple[str name, lrel[num, num] r] dots1 = <"0", [<arbReal(), arbReal()*2>|int i<-[0..n]]>;
    tuple[str name, lrel[num, num] r] dots2 = <"1", [<arbReal(), arbReal()*2>|int i<-[0..n]]>;
    plot(dt+[dots1, dots2] , x= -1, y=0, width = -1, nTickx = 10, height = 2, nTicky = 10, nStep=30, style="dots", symbolSize=40,
    colorMap=blue, styleMap=("0":"points","1":"points"), style="splines"
    );
    // plotFunction(num(num x){return -1.00000001;}  , cos, width = 6, y = -1, height = 2, nTickx = 6, nTicky = 4
    // , nStep=100, viewWidth = 600,viewHeight= 400);
    }