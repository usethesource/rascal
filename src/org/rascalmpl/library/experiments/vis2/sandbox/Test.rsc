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
     
 Figure stack(Figure f) = vcat(vgap=4, figs=[box(fig=text("\<pre\><figToString(f)>\</pre\>", size=<800, 60>, overflow="auto"), fillColor = "beige"), f]);
 
 Figure tests(  ) {
     return vcat(borderWidth=4, vgap=4, figs= mapper(
        [
         box(size=<100, 100>, fillColor ="green")
        ,box(fig=text("Hallo", fontSize=20, fontColor="darkred"), grow = 1.7, fillColor = "antiquewhite")
        ,box(fillColor="antiquewhite", lineWidth = 8, lineColor="blue", align = centerMid, grow  =1.0
              , fig = box( size=<200, 200>, fillColor = "gold", lineWidth = 8, lineColor = "red"))
        ,box(fig=box(size=<50, 50>,fillColor="red", lineWidth =1),align= topLeft,grow = 1.5,fillColor = "antiquewhite", lineWidth = 1)
        ,box(fig=box(size=<50, 50>,fillColor="red", lineWidth =1),align= centerMid,grow = 1.5,fillColor = "antiquewhite", lineWidth = 1)
        ,box(fig=box(size=<50, 50>,fillColor="red", lineWidth =1),align= bottomRight,grow = 1.5,fillColor = "antiquewhite", lineWidth = 1)
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
        , resizable=true);
     } 
     
 public void ttests() = render(tests()); 
 
 public void ftests(loc l) = writeFile(l, toHtmlString(
   tests()
 ));
 
//Figure simple() =  box(lineWidth= 0, size=<50, 50>, fillColor = "green", tooltip=
//   box(lineWidth=1, fillColor="whitesmoke", fig=hcat(size=<150, 150>, lineWidth = 0, borderWidth = 0, hgap= 0, figs=[
//  box(lineWidth = 6, lineColor="magenta", fillColor= "antiquewhite")
// ,box(lineWidth = 6, size=<50, 50>, lineColor="magenta", fillColor= "whitesmoke")
//  ]))
 // )
 // ;
 
// Figure simple() =  hcat(figs=[box(size=<30, 30>, fillColor="blue"), box(size=<50, 50>, fillColor="yellow"), box(size=<70, 70>, fillColor=  "red")],align= topLeft);

Figure simple() {
  Figure f = circle(shrink=0.6, lineWidth=8, fillColor = "antiquewhite", align = centerRight, lineColor="green");
  return hcat(figs=[box(
   , fig=circle(shrink=0.8, fillColor ="antiquewhite", lineWidth = 20, lineColor="blue", align = centerRight
    , fig = ngon(n=5, shrink=0.8, lineWidth = 20,  lineColor = "red", fillColor="yellow", align = centerMid
    ,fig = circle(shrink=0.6, lineWidth=8, fillColor = "antiquewhite", lineColor="green"
   // ,event=on("click", box(fig=atXY(100, 100, 
  //  box(vgrow=2.0, lineWidth = 4, lineColor="grey", fig=text("\<pre\><f>\</pre\>"), fillColor = "whitesmoke"))/*, fillColor="none"*/))
 //,tooltip = box(size=<15, 15>, fillColor="green")
   )
)
)
)]);}

// Figure simple() = box(grow=1.5, fig=hcat(figs=[box(size=<50, 50>, fillColor="red")]));
//Figure simple() = 
//    box(fig=hcat(size=<600, 400>, hgap = 20, figs=[
//    ellipse(lineWidth=8, lineColor="red", fig = ellipse(lineColor="blue"))
//    , ellipse(lineWidth=8, lineColor="red", fig = ellipse(lineColor="blue"))
 //   ]))
//   ;

 
 public void tsimple() = render(simple(), size=<600, 600>);
 
 
 public void fsimple(loc l) = writeFile(l, toHtmlString(
   simple(), size=<600, 600>, resizable=true
 )); 
 
 public void psimple(loc l) = renderSave(simple(), l
    ,width = 400, height = 400, javaLoc=|file:///ufs/bertl/jdk1.8.0_77|
    );
 
 Figure eye()= ellipse(rx=60, ry = 30, lineColor="brown", align = centerMid, fillColor="teal", lineWidth = 6
                      , fig = circle(shrink=1.0, fillColor = "whitesmoke", lineWidth = 4, lineColor = "red"));
                      
 
                      
 //Figure eye()= box(size=<200, 200>, lineColor="brown", fillColor="yellow", lineWidth = 20
 //                     , fig = box(shrink=1.0, fillColor = "whitesmoke", lineWidth = 40, lineColor = "red"));
 
Figure face() = ellipse(grow= 1.2, fig=vcat( figs=[box(size=<50, 50>, lineWidth=0), hcat(figs=[eye(), eye()], hgap = 10)
                  ,polygon(size=<50, 150>, points=[<0, 0>, <1,0>, <0.5, 1>],scaleX=<<0,1>,<0, 50>>,scaleY=<<0,1>,<150, 0>>, fillColor="pink") 
                                     , box(size=<10, 10>, lineWidth= 0)
                                     ,overlay(figs=
                                     [ellipse(size=<200, 25>, fillColor="orange"), atXY(10, 10, box(size=<180, 4>, fillColor="brown", rounded=<2, 2>))])
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

loc location = |project://rascal/src/org/rascalmpl/library/experiments/vis2/data/tutor.html|;  


Figure tut() = box(fillColor="yellow", size=<50, 50>
      // ,event = on("click", void(str e, str n , str v) {println("<e>");})
     , panel= box(lineWidth= 0, fig=atXY(60, 60, box(lineColor="black", lineWidth=2,  fig = text(readFile(location))))));

public void ttut() {render(tut(), cssFile = "tutor.css", size=<800, 800>);}

public void ftut(loc l) = writeFile(l, toHtmlString(
   tut(), cssFile = "tutor.css", size=<800, 800>
 ));
 
 public Figure elp() = ellipse(lineWidth = 10,   fig=box(size=<50, 100>, fillColor="yellow", lineWidth =6));
 
 public void telp() = render(elp());
 
 
Figure quest(str p, int x, int y) {
    bool isName(value v) =  str s:=v && /[0-9]/!:=s;
    bool isNotEmpty(value v) =  str s:=v && !isEmpty(s);
    bool isTel(value v) = str s:=v && size(s)==10 && s[0]=="0";
    bool lowConstraint(value v) = real d:=v && d>=100;
   
    list[Constraint] constraints = [<isName, "name contains digit">, <isNotEmpty,"enter name, please">];
       return hcat(figs=[
        makeFormAction(box(fig=text("aap"), grow=1.5,fillColor="yellow", visibility="visible"),
        // buttonInput("aap", width = 200),  
         [
         <p+"sexe", <["male", "female"], "">, "sexe", [<isNotEmpty, "choose sexe, please">]>
         , <p+"first", "Bert", "first name", constraints>
         ,<p+"last",  emptyStr, "last name",  constraints >
         ,<p+"telnumber", emptyStr, "tel. number", [<isTel, "is not a telephone number">]>,
        <p+"amount", emptyEuro, "amount", [<lowConstraint, "Start amount @ too low">]>
         ]
         ,event= on(void(str e, str n, str v){println("aap");})
         ,x=x, y = y, visibility = "hidden")
       ,
       box(size=<50, 50>,  fillColor = randomColor())
       ]);
    }

void tquest() = render(vcat(figs=[quest("a", 50, 50), quest("b", 300, 300)]));

public void fquest(loc l) = writeFile(l, toHtmlString(
   vcat(figs=[quest("a", 10, 10), quest("b",500, 500)])
 )); 
 
 
 Figure half()=hcat(height = 120, figs = [strInput(fillColor="yellow", nchars = 20)], fillColor="lightGrey");
 
 void thalf() = render(half());
 
 public void fhalf(loc l) = writeFile(l, toHtmlString(
    half()
 )); 
 
 
 
 Figure funLine() {
      int w = 400; 
      int h = 1600;
      int offset = 5;
      num hc = 0.5;
      list[int] prim = primes(30);
      tuple[int, int](num x, num y) c = lattice(w, h, 4, 25);
      int(num) cx = latticeX(w, 4);
      int(num) cy = latticeY(h, 25);
      map[int, tuple[int x, int y]] from = ();
      map[int, tuple[int x, int y]] to = ();
      
      int multiplicity(int d, int p) {
            int r=0;
            while (d%p==0) {
               r = r + 1;
               d = d/p;
               }
            return r;
            }
      
      Figure cell1(int d, int x , int y) {
      from[d] = <x, y>;
      return  atXY(c(x, y), box(fig=text("<d>"), size=c(0.25, hc), fillColor="yellow"));
      }
      
      Figure cell2(int d, int x , int y) {
      // println(d);
      to[d] = <x, y>;
      return  atXY(c(x, y), box(fig=text("<d>"), size=c(0.25, hc), fillColor="antiquewhite"));
      }
      
      Figure connect(int f, int t) {
         tuple[int , int] rv = <cx(to[t].x)-cx(from[f].x+0.25), cy(to[t].y+hc/2)-cy(from[f].y+hc/2)>;
         return overlay(figs=[
         shape([move(cx(from[f].x+0.25), cy(from[f].y+hc/2)), line(cx(to[t].x),  cy(to[t].y+hc/2))]
         // , endMarker = ngon(n=3, r=10, fillColor = "purple", lineWidth = 0)
         )
         ,circle(r=4, fillColor="firebrick", cx = cx(from[f].x+0.25)+0.2*rv[0], cy = cy(from[f].y+hc/2)+0.2*rv[1]
          , tooltip=box(fig=text("<multiplicity(f, t)>"), fillColor="floralwhite", size=<50, 50>)
         //  , tooltip = box(size=<100, 100>, fig=box(size=<50, 50>), fillColor="red")
         )
         ]);
         }
      
      Figures fs =  [cell1(i, 0, i-10) |int i<-[10..25]];
      Figures  ts = [cell2(prim[i], 3,  i+offset)|int i<-[0..5]];
      Figures cs = [*[connect(i, p)|p<-prim, i!=p, i%p==0]| int i<-[10..25]];
      return overlay(width=w, height = h, figs= fs +ts + cs
           );
      }
      
 void tfunLine() = render(funLine());
 
 public void ffunLine(loc l) = writeFile(l, toHtmlString(
    funLine()
 )); 
                 
