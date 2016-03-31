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

Figure simple() =  hcat(figs=[box(
, fig=circle(shrink=0.8, fillColor ="antiquewhite", lineWidth = 20, lineColor="blue", align = centerRight
 , fig = ngon(n=5, shrink=0.8, lineWidth = 20,  lineColor = "red", fillColor="yellow", align = centerLeft
, fig = circle(shrink=0.6, lineWidth=8, fillColor = "antiquewhite", align = centerRight, lineColor="green")
)
)
)]);

<<<<<<< HEAD
// Figure simple() = box(grow=1.5, fig=hcat(figs=[box(size=<50, 50>, fillColor="red")]));
=======
//Figure simple() = 
//    box(fig=hcat(size=<600, 400>, hgap = 20, figs=[
//    ellipse(lineWidth=8, lineColor="red", fig = ellipse(lineColor="blue"))
//    , ellipse(lineWidth=8, lineColor="red", fig = ellipse(lineColor="blue"))
 //   ]))
//   ;
>>>>>>> Automatic calculation of priority.
 
 public void tsimple() = render(simple(), resizable=true, size=<600, 600>);
 
 
 public void fsimple(loc l) = writeFile(l, toHtmlString(
   simple(), size=<600, 600>, resizable=true
 )); 
 
 Figure eye()= ellipse(rx=60, ry = 30, lineColor="brown", align = centerMid, fillColor="teal", lineWidth = 6
                      , fig = circle(shrink=1.0, fillColor = "whitesmoke", lineWidth = 4, lineColor = "red"));
                      
 
                      
 //Figure eye()= box(size=<200, 200>, lineColor="brown", fillColor="yellow", lineWidth = 20
 //                     , fig = box(shrink=1.0, fillColor = "whitesmoke", lineWidth = 40, lineColor = "red"));
 
Figure face() = ellipse(grow= 1.2, fig=vcat( figs=[box(size=<50, 50>, lineWidth=0), hcat(figs=[eye(), eye()], hgap = 10)
                  ,polygon(size=<50, 150>, points=[<0, 0>, <1,0>, <0.5, 1>],scaleX=<<0,1>,<0, 50>>,scaleY=<<0,1>,<150, 0>>, fillColor="pink") 
                                     , box(size=<10, 10>, lineWidth= 0)
                                     ,overlay(figs=
                                     [ellipse(size=<200, 25>, fillColor="orange"), at(10, 10, box(size=<180, 4>, fillColor="brown", rounded=<2, 2>))])
                                  ,box(size=<50, 50>, lineWidth = 0)]
                       ,fillColor= "none"), fillColor="antiquewhite");
    
void tface() = render(face());                     
                      
 
public Figure idCircleShrink(num shrink) = circle(shrink= shrink, lineWidth = 4, lineColor = pickColor());

public Figure idEllipseShrink(num shrink) = ellipse(shrink= shrink, lineWidth = 4, lineColor = pickColor());

public Figure idBoxShrink(num shrink) = box(shrink= shrink, lineWidth = 10, lineColor = pickColor());

public Figure idNgonShrink(num shrink) = ngon(n=4, shrink= shrink, lineWidth = 4, lineColor = pickColor());

public Figure newCircle(str lc, Alignment align, Figure el) {
      return circle(r=-1, lineColor= lc, lineWidth = 4, 
           fillColor = "none", padding=<0,0,0,0>, align = align, 
      fig = el, shrink=0.9);
      }
      
public Figure newEllipse(str lc, Alignment align, Figure el) {
      return ellipse(lineColor= lc, lineWidth = 4, 
           fillColor = "none", padding=<0,0,0,0>, align = align, 
      fig = el, shrink=0.9);
      }
      
public Figure newBox(str lc, Alignment align, Figure el) {
      return box(lineColor= lc, lineWidth = 10, 
           fillColor = "none", padding=<0,0,0,0>, align = align, 
      fig = el, shrink=1.0);
      }
      
public Figure newNgon(str lc, Alignment align, Figure el) {
      return ngon(n=4, lineColor= lc, lineWidth = 4, 
           fillColor = "none", padding=<0,0,0,0>, align = align, 
      fig = el, shrink=0.9);
      }
      
public Figure bundle(int n, Alignment align) { resetColor(); return
      (idCircleShrink(0.9) |newCircle(e, align, 
      it)| e<-[pickColor()|int i<-[0..n]])
      ;}
      
public Figure bundle() = overlay(figs=[
               bundle(4, centerLeft), 
               bundle(4, centerRight),
               // bundle(4, centerMid),  
               bundle(4, topMid), 
               bundle(4, bottomMid)
              ])
               ;
      
void tbundle() = render(bundle(), size=<600, 600>);  

public void fbundle(loc l) = writeFile(l, toHtmlString(
   bundle(), resizable=true
 )); 
 
Figure base(int lineWidth, Alignment align, Figure fig = emptyFigure())  = box(lineWidth = 0, align = align, 
                 fig = box(shrink = 0.44, lineWidth = lineWidth, lineColor = pickColor(),
                           fig = fig)
                 );
                 
Figure base(int lineWidth, Figure fig = emptyFigure()){
                resetColor();
                return  overlay(figs = [
               base(lineWidth, topLeft, fig= fig)
             , base(lineWidth,topRight, fig = fig)
             , base(lineWidth,bottomLeft, fig = fig)
             , base(lineWidth,bottomRight, fig = fig)
             ]);}
              
Figure baseRec() { return base(4, fig = base(4, fig= base(4, fig= base(4)))
      //overlay(lineWidth = 0, figs=[
      //  box(lineWidth = 0, align=topLeft, fillColor="none", fig=circle(shrink=0.8, lineWidth = 1))
      //   ,
      //   box(lineWidth = 0, align=bottomRight, fillColor="none", fig=circle(shrink=0.8, lineWidth = 1))
      //])
     );
 }

void tbase() = render(baseRec(), size=<600, 600>);  

public void fbase(loc l) = writeFile(l, toHtmlString(
   baseRec()
 )); 
 
 Figure b(int w, int h) = box(grow=1.2, fig= box(size=<w, h>));
 
 Figure rec() = hcat(figs=[b(10, 40), b(20, 50), b(15, 45)
 // , box()
 ]);
 
 
 void trec() = render(rec(), size=<400, 400>);

public void frec(loc l) = writeFile(l, toHtmlString(
   rec()
 ));
 
 Figure place(str fill) = box(size=<25, 25>, fillColor = fill);

Figure tetris() = 
       grid( vgap=0, hgap= 0
       , 
       figArray=[
       [place("blue"), emptyFigure()]
      ,[place("blue"), emptyFigure()]
      ,[place("blue"), place("blue")]
       ]);
  
void ttetris() = render(tetris());     
                 
