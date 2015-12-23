module experiments::vis2::sandbox::Tutor
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import util::Eval;
// import util::Math;
import Prelude;

Figure title(Figure f) {
      int n = 40;
      loc lc = |tmp://sandbox/aap|;
      //writeFile(lc ,f); 
      iprintToFile(lc ,f); 
      str s = readFile(lc);
      if (n<size(s) && findFirst(s,"\n")==-1) { 
         str t = substring(s, n, size(s));
         t = replaceFirst(t, ",", "\<br\>,");
         s=substring(s, 0, n)+t;
         }
      return vcat(figs= [htmlText(replaceAll(replaceAll(s,"\n", "\<br\>"), "\"","\\\"")
      , fontStyle="italic", height =125),f]);
      }

public void render1(Figure f, str fillColor = "none", Alignment align = <0.5, 0.5>, tuple[int, int] size = <0, 0>) = render(f, borderWidth=1
, fillColor = fillColor, align = align, size = size
);


public void tut1()= render(box());


Figures  tut() =
// 0
   [box(lineColor="green", lineWidth=16) 
// 1                                                   
   ,box(shrink=0.50) 
// 2 
   ,box(fillColor="green")
// 3  
   ,box(fillColor="green", lineColor= "red", hshrink=0.7, vshrink=0.2) 
// 4 
   ,box(fillColor="antiquewhite", fig=
        box(fillColor="green", size=<100, 100>
          ,lineWidth=8, lineColor="red")
       ,lineWidth= 6,lineColor="blue"
       )
 // 5
   ,ngon(n=5 ,grow= 1.5, fig = 
         ngon(n=5, r=50,  fillColor="green" ,lineColor="red"
             )
        ,lineWidth=16,lineColor="blue"
        )
 // 6 
   ,box(grow=1.8,lineWidth = 4, fig=text("Hallo",  fontSize=12))
// 7 
   ,ellipse(grow = 1.5, fig=
            ellipse(rx=100, ry=50, fillColor="green"
                   ,lineColor="red"
                   )
           ,lineWidth=4, lineColor="blue")
// 8 
   ,box(grow = 1.5,fig=hcat(figs=[box(size=<50, 50>, fillColor="yellow")]))
// 9
    ,ellipse(fig=circle(r=50, fillColor="green"
       ,lineColor="red")
       ,lineWidth =16, lineColor="blue", hgrow = 1.5)
// 10
    , hcat(fillColor = "white", figs = [
         box(fillColor = "mediumblue",fillOpacity= 0.05),
         box(fillColor = "mediumblue",fillOpacity= 0.2),
         box(fillColor = "mediumblue",fillOpacity= 0.4),
         box(fillColor = "mediumblue",fillOpacity= 0.6)
      ])
 // 11
    , hcat(align = topMid, figs = [ box(vshrink=0.2,hshrink=0.5, fillColor="red"),
			        box(vshrink=0.8,fillColor = "yellow"),
			        box(vshrink=0.5,hshrink=0.2, fillColor="green")
			      ]
			      )
   ];
   
 Figure _tuts() = vcat(figs=[title(f)|f<-tut()], borderWidth = 8, vgap= 8, borderStyle="ridge");
 
 void tuts() = render(_tuts());
 
 void ftuts(loc f) = writeFile(f, toHtmlString(_tuts()));
 

public void ftut7(loc f) = writeFile(f, toHtmlString(_tut7()));

public void ftut5(loc f) = writeFile(f, toHtmlString(_tut5()));


void tutor1()=render1(_tutor1());

Figure _tutor2() = hcat(fillColor = "white", figs = [
box(fillColor = "mediumblue",fillOpacity= 0.05),
box(fillColor = "mediumblue",fillOpacity= 0.2),
box(fillColor = "mediumblue",fillOpacity= 0.4),
box(fillColor = "mediumblue",fillOpacity= 0.6),
box(fillColor = "mediumblue",fillOpacity= 0.8),
box(fillColor = "mediumblue",fillOpacity= 1.0)
] );

void tutor2()=render1(_tutor2());


Figure _tutor4()= hcat(align = bottomMid, figs = [ box(vshrink=0.2,hshrink=0.5, fillColor="red"),
			        box(vshrink=0.8,fillColor = "yellow"),
			        box(vshrink=0.5,hshrink=0.2, fillColor="green")
			      ]
			      );
			    
void tutor4()=render1(_tutor4());


Figure _tutor5()= hcat(align = centerMid, figs = [ box(vshrink=0.2,hshrink=0.5, fillColor="red"),
			        box(vshrink=0.8,fillColor = "yellow"),
			        box(vshrink=0.5,hshrink=0.2, fillColor="green")
			      ]
			      );
			    
void tutor5()=render1(_tutor5());

Figure b(Figure f) = box(fig = f, size=<200, 200>);

Figure _tutors() =vcat(figs = [ b(_tut1()), b(_tut2()), b(_tut3()), b(_tut4())
                          ,b(_tutor1()), b(_tutor2()), b(_tutor3()), b(_tutor4()), b(_tutor5())]);
                          
void tutors()=render(_tutors(), size=<400, 800>);





public void ex1() = render1(box());

public void fex1(loc l) = writeFile(l, toHtmlString(
   box()
));

public void ex2() = render1(grid(figArray=[[box(shrink = 0.8,  fillColor = randomColor()
       , tooltip = box(fig=htmlText("", size=<250, 20>), fillColor="antiqueWhite")
       ,event = on("mouseenter", void(str e, str n, str v) {
              textProperty("<n>_tooltip#0", text= style(n).fillColor);      
              })  
       )]]
       )
       )
       ;

public void ex3() = render1(box(vshrink = 0.8,  hshrink = 0.6, fillColor = "green"));

public void ex4() = render1(box(shrink = 0.8,
  fig = box(shrink = 0.8, fillColor = "yellow"),fillColor = "green")
      ,fillColor = "white");
      
     
public void ex5() = render1(
    hcat(shrink = 0.8,figs=
       [box(fillColor = "yellow", shrink=0.8)
       ,box( fillColor = "red")
       ], align = bottomRight )
      ,fillColor = "antiqueWhite");
      
 
 Figure elFig(num shrink, bool tt) {
     // println(shrink);
     return 
       ellipse(shrink = shrink, fillColor =  randomColor(),   lineWidth = 2  
       ,fig=box(shrink=0.6, align = centerMid, 
            fig=
             circle( shrink = 0.8, fillColor=randomColor(), lineWidth = 0 
             , tooltip = tt?box(fig=htmlText("", size=<50, 20>, fontSize=10), fillColor="antiqueWhite"):emptyFigure() 
             ,event = on("mouseenter", void(str e, str n, str v) {
              textProperty("<n>_tooltip#0", text= style(n).fillColor);      
              })       
              
          ) 
          , fillColor=randomColor())
       )  
       ;
     }
 
 Figures elFigs(int n, bool tt) = n>0?
    [elFig(1-i/(2*n), tt)|num i<-[0,1..n]]
   :[elFig(1-i/(2*-n), tt)|num i<-[-n,-n-1..0]];
   
public Figure shrink(bool tt) = grid(figArray=[elFigs(5, tt), elFigs(-5, tt)], align = centerMid, borderWidth=1);
 
public void ex6() = render1(hcat(figs=[grid(figArray=[[shrink(true)]])]));
 
public void ex7() = render(ngon(n = 6, angle = 0, fillOpacity=0.5
  ,fig = ngon(n=6, shrink = 0.8, angle=0, fillOpacity=0.5, fillColor = "yellow")
      ,fillColor = "green")
      ,fillColor = "white");


// Figure op(Figure f, str c)   = self(ngon(n = 5, shrink = 0.9, fig = f, fillColor = c, lineWidth = 0));

Figure client(value v) {
    if (tuple[num, str, str] s:=v) return ngon(n = 7, shrink = s[0], lineWidth = 4, fillOpacity=0.1
    , lineOpacity=0.1,lineColor=s[1],
    fillColor=s[2]);
    return emptyFigure();
    }
  
public void ex8() = render1(
   (emptyFigure()|self(client(c))(it, c)|value c<-[<0.90+i/400.0, randomColor(), randomColor()>|int i<-[0..40]])
  );
    
public void fex2(loc l) = writeFile(l, toHtmlString(
   box(shrink = 0.8,  fillColor = "green")
));


public void fex5(loc l) = writeFile(l, toHtmlString(
  box(shrink = 0.8,
  fig = hcat(shrink = 0.8,figs=
       [box( fillColor = "yellow")
       ,box( fillColor = "red")
       ])
      ,fillColor = "green"))
);


public void fex6(loc l) = writeFile(l, toHtmlString(
  vcat(size=<200, 200>, figs=elFigs(1), borderWidth = 1)
));


Figure _tst()= vcat(lineWidth = 1, figs = [overlay(id="aap", figs=[overlay(figs=[box(size=<100, 100>)])])]);

void tst() = render(_tst());


Figure triangle(int angle) = ngon(n=3, size=<50, 50>, angle = angle, fillColor = "yellow");

Figure _star() = grid(vgap=0, hgap= 0, figArray=[
       [emptyFigure(), triangle(-90), emptyFigure()]
      ,[triangle(180), box(size=<50, 50>, fillColor="red"), triangle(0)]
      ,[emptyFigure(), triangle(90), emptyFigure()]
       ]);

public void star()= render(_star());

Figure place(str fill) = box(size=<25, 25>, fillColor = fill);


Figure _tetris1() = 
       grid( vgap=0, hgap= 0
       , 
       figArray=[
       [place("blue"), emptyFigure()]
      ,[place("blue"), emptyFigure()]
      ,[place("blue"), place("blue")]
       ]);
       
Figure emptFigure() = box(size=<10, 10>);
       
Figure _tetris2() = 
       grid(vgap=0, hgap= 0,
       figArray=[
       [emptyFigure(), place("blue")]
      ,[emptyFigure(), place("blue")]
      ,[place("blue"), place("blue")]
       ]);
       
Figure _tetris3() = 
       grid(vgap=0, hgap= 0,
       figArray=[
       [place("red"), place("red")]
      ,[place("red"), place("red")]
       ]);
       
Figure _tetris4() = 
       grid(vgap=0, hgap= 0,
       figArray=[
       [place("yellow"), place("yellow"), place("yellow")]
      ,[emptyFigure(), place("yellow"), emptyFigure()]
       ]);
       
Figure _tetris5() = 
       grid(vgap=0, hgap= 0, 
       figArray=[
       [emptyFigure(), place("darkmagenta"), place("darkmagenta")]
      ,[place("darkmagenta"), place("darkmagenta"), emptyFigure()]
       ]);
       
Figure _tetris6() = 
       grid(vgap=0, hgap= 0, 
       figArray=[
       [place("brown")]
      ,[place("brown")]
      ,[place("brown")]
      ,[place("brown")]
       ]);
       
public Figure tetris() = hcat(borderStyle="ridge", borderWidth = 4, 
lineWidth = 1, align = bottomRight, 
figs=[_tetris1(), _tetris2(), _tetris3(), _tetris4(), _tetris5(), _tetris6()]);
       
public void tetris1() = render(tetris());

public void ftetris1(loc l) = writeFile(l, toHtmlString(
    grid(hgap=4, vgap = 4, id="aap", figArray=[[_tetris1(),  _tetris2()]])
));

public Figure tip() = box(size=<150, 150>, tooltip = box(size=<50, 50>,resizable=false), resizable=false);

public void ttip() = render(tip());

public void ftip(loc l) = writeFile(l, toHtmlString(tip()));

// public Figure tipf() = box(size=<50, 50>, fillColor=  "red", visibility = "visible");

public Figure tipf() = vcat(fillColor="grey", figs=[text("aap", size=<40, 20>, fontColor="black"), text("noot", size=<40, 20>)]);

public Figure tipi() = box(size=<100, 100>, fillColor="yellow", tooltip=tipf());

public Figure tipk() = box(size=<50, 50>, fillColor="red");

public Figure tipo() = overlay(figs=[at(0, 0, tipi()), at(100, 100, tipi())], resizable=false);

public Figure tipg() = graph(size=<300, 300>, nodes=[<"A", tipi()>, <"B", tipi()>], edges=[edge("A","B")]);

public void ttipo() = render(tipo());

public void ttipg() = render(tipg());

public void ftipo(loc l) = writeFile(l, toHtmlString(tipo()));

public void twrong()=render(at(10, 10,  box(size=<50, 50>, fillColor = "white", tooltip=box(size=<20, 20>, fillColor="red"))));

public Figure txt() = overlay(figs=[box(fig = circle(r=30), tooltip = text( "noot")), text("mies")]);

public void ttxt() = render(txt(), borderWidth=1);

public void ftxt(loc l) = writeFile(l, toHtmlString(txt()));
