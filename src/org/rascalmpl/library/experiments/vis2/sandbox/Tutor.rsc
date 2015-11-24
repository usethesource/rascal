module experiments::vis2::sandbox::Tutor
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import util::Math;
import Prelude;

str randomColor() =  colors[arbInt(size(colors))];

public void render1(Figure f, str fillColor = "white", Alignment align = <0.5, 0.5>) = render(f, borderWidth=1
, fillColor = fillColor, align = align);

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
      
 
 Figure elFig(num shrink) {
     println(shrink);
     return 
       ellipse(shrink = shrink, fillColor =  randomColor(),   lineWidth = 2  
       ,fig=box(shrink=0.8, align = centerMid, 
            fig=
             circle( shrink = 0.8, fillColor=randomColor(), lineWidth = 0 
             , tooltip = box(fig=htmlText("", size=<50, 20>, fontSize=10), fillColor="antiqueWhite") 
             ,event = on("mouseenter", void(str e, str n, str v) {
              textProperty("<n>_tooltip#0", text= style(n).fillColor);      
              })       
              
          ) 
          , fillColor=randomColor())
       )  
       ;
     }
 
 Figures elFigs(int n) = n>0?
    [elFig(1-i/(2*n))|num i<-[0,1..n]]
   :[elFig(1-i/(2*-n))|num i<-[-n-1,-n-2..-1]];
 
 public void ex6() = render1(
  grid(figArray=[elFigs(5), elFigs(5)], align = centerMid, borderWidth=1)
  );
 
 public void ex7() = render1(grid(figArray=
 [[box(shrink=0.8, fillColor ="yellow"), circle(shrink=0.8)],[box(shrink=0.6), box(shrink=0.8)]],  align = bottomRight, borderWidth = 1)
 );

 public void ex8() = render(grid(borderWidth = 1, figArray=[[box(size=<10, 10>)], [box(size=<10, 10>),
   box(size=<10, 10>)]]));
    
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