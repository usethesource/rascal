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
        ,box(fillColor="antiquewhite", lineWidth = 8, lineColor="blue", align = topLeft, grow  =1.0, fig = box( size=<200, 200>, fillColor = "gold", lineWidth = 8))
        ,box(fig=box(size=<50, 50>,fillColor="red"),align= topLeft,grow = 1.5,fillColor = "antiquewhite")
        ,box(fig=box(size=<50, 50>,fillColor="red"),align= centerMid,grow = 1.5,fillColor = "antiquewhite")
        ,box(fig=box(size=<50, 50>,fillColor="red"),align= bottomRight,grow = 1.5,fillColor = "antiquewhite")
        ,box(size=<50,50>, fig= box(shrink=0.75, fillColor = "yellow"), align = topLeft, fillColor= "green")
        ,box(size=<50,50>, fig= box(shrink=0.75, fillColor = "yellow"), align = centerMid, fillColor= "green")
        ,box(size=<50,50>, fig= box(shrink=0.75, fillColor = "yellow"), align = bottomRight, fillColor= "green")
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
        , resizable=false);
     } 
     
 public void ttests() = render(tests()); 
 
 public void ftests(loc l) = writeFile(l, toHtmlString(
   tests()
 ));
 
// Figure simple() =  box(size=<100, 100>, fillColor ="green", tooltip = frame(fig=box(size=<150, 150>, fillColor= "red")));
 
// Figure simple() =  hcat(figs=[box(size=<30, 30>, fillColor="blue"), box(size=<50, 50>, fillColor="yellow"), box(size=<70, 70>, fillColor=  "red")],align= topLeft);

Figure simple() =  box(size=<100, 100>, fillColor ="yellow", tooltip = klokFrame());
 
 public void tsimple() = render(simple(), resizable=false);
 
 
 public void fsimple(loc l) = writeFile(l, toHtmlString(
   simple()
 )); 
 
 Figure klokBox(int r, int n) =  at(0, n/2, box(size=<10, n>,  rounded=<10, 10>, lineWidth = 1, fillColor="yellow"));

Figure klokFrame() {
     int r = 80; 
     int d = 2;
     int cx = 20 + r;
     int cy = 20 + r; 
     int r1 = 95; 
     list[Figure] cs =
          [circle(r=r, cx= cx, cy = cy, fillColor = "silver", lineColor = "red")]
          +
          [
           at(20-6+r+toInt(sin((PI()*2*i)/12)*r1), toInt(20-5+r-cos((PI()*2*i)/12)*r1), htmlText("<i>", size=<20, 20>))|int i<-[12, 3, 6, 9]
          ]
        
        +[at(20-1+r+toInt(sin((PI()*2*i)/12)*(r)), toInt(20+r-cos((PI()*2*i)/12)*(r)), circle(r=d, fillColor="black"))|int i<-[12, 3, 6, 9]
         ]
        +box(lineWidth = 0, fillColor="none", fig=at(20, 20, rotate(210, box(size=<2*r, 2*r>, fig =klokBox(r, 70), align = centerMid))))      
        +box(lineWidth = 0, fillColor="none", fig=at(20, 20, rotate(180, box(size=<2*r, 2*r>, fig =klokBox(r, 50), align = centerMid))))  
          +[circle(r=5, cx = cx, cy = cy, fillColor = "red")]
        ;
     return box(fig=overlay(figs=cs, size=<220, 220>), size=<250, 250>);
     }
     
Figure demo14() = klokFrame();

void tklok()= render(klokFrame());


void fklok(loc l) = writeFile(l, toHtmlString(klokFrame()));
 
 
 
                 