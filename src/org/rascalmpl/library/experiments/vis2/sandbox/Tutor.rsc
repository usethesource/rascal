module experiments::vis2::sandbox::Tutor
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import Prelude;

public void render1(Figure f) = render(f, borderWidth=1);

public void ex1() = render1(box());

public void ex2() = render1(box(shrink = 0.8,  fillColor = "green"));

public void ex3() = render1(box(vshrink = 0.8,  hshrink = 0.6, fillColor = "green"));

public void ex4() = render1(box(shrink = 0.8,
  fig = at(20, 20, box(shrink = 0.4, fillColor = "yellow"))
      ,fillColor = "green"));
      
     
public void ex5() = render1(box(shrink = 0.8,
  fig = hcat(shrink = 0.8,figs=
       [box( fillColor = "yellow")
       ,box( fillColor = "red")
       ])
      ,fillColor = "green"));
      
 
 Figure elFig(num shrink) {
     println(shrink);
     return box(shrink = shrink, fillColor="yellow");
     }
 
 Figures elFigs(int n) = [elFig(1-i/(2*n))|num i<-[0,1..n]];
 
 public void ex6() = render(vcat(size=<200, 200>, figs=elFigs(5)));


/*      
public void fex4(loc l) = writeFile(l, toHtmlString(
    box(shrink = 0.8
  , fig = box(shrink = 0.8,  fillColor = "yellow")
      ,fillColor = "green")
));

public void fex5(loc l) = writeFile(l, toHtmlString(
  box(shrink = 0.8,
  fig = hcat(shrink = 0.8,figs=
       [box( fillColor = "yellow")
       ,box( fillColor = "red")
       ])
      ,fillColor = "green"))
);
*/

public void fex6(loc l) = writeFile(l, toHtmlString(
  vcat(size=<200, 200>, figs=elFigs(5))
));