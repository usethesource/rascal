module experiments::vis2::sandbox::Test
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import experiments::vis2::sandbox::Shortcuts;
import util::Math;

import util::Reflective;

import Prelude;
 
public void standard() {
      str dirN = "/tmp/rascal";
      loc dir = |file://<dirN>|;
      loc ok = dir+"ok.html";
      ftests(ok);
      }
 
 public str compare() {
     str dirN = "/tmp/rascal";
     loc dir = |file://<dirN>|;  
     str ok = readFile(dir+"ok.html"); 
     str check = toHtmlString(tests());
     return diff(ok, check);
     }
     
 Figure stack(Figure f) = vcat(vgap=4, figs=[box(fig=text("\<pre\><f>\</pre\>", size=<800, 60>, overflow="auto"), fillColor = "beige"), f]);
 
 Figure tests(  ) {
     return vcat(borderWidth=4, vgap=4, figs= mapper(
        [
         box(size=<100, 100>, fillColor ="green")
        ,box(fig=text("Hallo", fontSize=20, fontColor="darkred"), grow = 1.7, fillColor = "antiquewhite")
        ,box(fillColor="antiquewhite", lineWidth = 8, lineColor="blue", align = centerMid, grow  =1.0
              , fig = box( size=<200, 200>, fillColor = "gold", lineWidth = 8, lineColor = "red"))
        ,box(fig=box(size=<50, 50>,fillColor="red"),align= topLeft,grow = 1.5,fillColor = "antiquewhite")
        ,box(fig=box(size=<50, 50>,fillColor="red"),align= centerMid,grow = 1.5,fillColor = "antiquewhite")
        ,box(fig=box(size=<50, 50>,fillColor="red"),align= bottomRight,grow = 1.5,fillColor = "antiquewhite")
        ,box(size=<50,50>, fig= box(shrink=0.75, fillColor = "yellow"), align = topLeft, fillColor= "green", resizable= true)
        ,box(size=<50,50>, fig= box(shrink=0.75, fillColor = "yellow"), align = centerMid, fillColor= "green", resizable= true)
        ,box(size=<50,50>, fig= box(shrink=0.75, fillColor = "yellow"), align = bottomRight, fillColor= "green", resizable= true)
        ,hcat(figs=[box(size=<30, 30>, fillColor="blue"), box(size=<50, 50>, fillColor="yellow"), box(size=<70, 70>, fillColor=  "red")],align= topLeft)
        ,hcat(figs=[box(size=<30, 30>, fillColor="blue"), box(size=<50, 50>, fillColor="yellow"), box(size=<70, 70>, fillColor=  "red")],align= centerMid)
        ,hcat(figs=[box(size=<30, 30>, fillColor="blue"), box(size=<50, 50>, fillColor="yellow"), box(size=<70, 70>, fillColor=  "red")],align= bottomRight)
        ,hcat(width=200, height=70, figs= [box(shrink= 1.0, fillColor= "blue"), box(shrink= 0.5, fillColor= "yellow"), box(shrink=1.0, fillColor= "red")], align = bottomLeft)
        ,vcat(width=200, height=70, figs= [box(shrink= 1.0, fillColor= "blue"), box(shrink= 0.5, fillColor= "yellow"), box(shrink=1.0, fillColor= "red")], align = bottomLeft)
        ,vcat(figs=[text("a",fontSize=14, fontColor="blue"), text("bb",fontSize=14, fontColor="blue"),text("ccc",fontSize=14, fontColor="blue")], align = topRight)
        ,grid(width=200, height=70, figArray= [[box(shrink= 0.5, fillColor="blue")], [box(shrink=0.3, fillColor="yellow"), box(shrink=0.5, fillColor="red")]], align=bottomLeft)
        ,grid(width=200, height=70, figArray= [[box(shrink= 0.5, fillColor="blue")], [box(shrink=0.3, fillColor="yellow"), box(shrink=0.5, fillColor="red")]], align=centerMid)
        ,graph(width=200, height=200, nodes=[<"a", box(fig=text("aap",fontSize=14, fontColor="blue"), grow=1.6, fillColor="beige")>
                                           , <"b", box(fig=text("noot",fontSize=14, fontColor="blue"), grow=1.6, fillColor="beige")>]
                                     ,edges=[edge("a","b")])
        ], stack)
        , resizable=true);
     } 
     
 public void ttests() = render(tests(), resizable=true); 
 
 public void ftests(loc l) = writeFile(l, toHtmlString(
   tests()
 ));
 
// Figure simple() =  box(size=<100, 100>, fillColor ="green", tooltip = frame(fig=box(size=<150, 150>, fillColor= "red")));
 
// Figure simple() =  hcat(figs=[box(size=<30, 30>, fillColor="blue"), box(size=<50, 50>, fillColor="yellow"), box(size=<70, 70>, fillColor=  "red")],align= topLeft);

Figure simple() =  circle(fillColor ="antiquewhite", lineWidth = 10, lineColor="blue", align = centerMid, fig = at(10, 10, ngon(
n= 9, lineWidth = 20, shrink = 1.0, lineColor = "red", fillColor="yellow")));
 
 public void tsimple() = render(simple(), resizable=true);
 
 
 public void fsimple(loc l) = writeFile(l, toHtmlString(
   simple(), resizable=true
 )); 
 
 Figure eye()= ellipse(rx=60, ry = 30, lineColor="brown", align = centerMid, fillColor="yellow", lineWidth = 6
                      , fig = circle(shrink=1.0, fillColor = "whitesmoke", lineWidth = 4, lineColor = "red"));
                      
 
                      
 //Figure eye()= box(size=<200, 200>, lineColor="brown", fillColor="yellow", lineWidth = 20
 //                     , fig = box(shrink=1.0, fillColor = "whitesmoke", lineWidth = 40, lineColor = "red"));
 
Figure face() = ellipse(grow= 1.2, fig=vcat( figs=[box(size=<50, 50>, lineWidth=0), hcat(figs=[eye(), eye()], hgap = 10)
                                  ,box(size=<50, 150>, lineWidth=0) ,  ellipse(size=<200, 25>, fillColor="orange")
                                  ,box(size=<50, 50>, lineWidth = 0)]
                       ,fillColor= "none"), fillColor="antiquewhite");
    
void tface() = render(face());                     
                      
 
 
                 