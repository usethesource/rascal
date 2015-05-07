module experiments::vis2::sandbox::Demo
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import Prelude;

// --------------------------------------------------------------------------

public Figure newNgon(str lc, Figure el) {
      return at(0, 0, ngon(n =7, align = topLeft, lineColor= lc, 
             lineWidth = 20, fillColor = "white", padding=<0,0,0,0>, 
      fig = el));
      }

public Figure demo1() = (idNgon(50) |newNgon(e, it)| 
              e<-["antiquewhite", "yellow", "red","blue" ,"grey","magenta"]);
void tdemo1()  {render(demo1(), debug = false, align = topLeft);}

// --------------------------------------------------------------------------

public Figure newBox(str lc, Figure el) {
      return at(10, 10, box(align = topLeft, lineColor= lc, lineWidth = 20, 
             fillColor = "white", fig = el));
      }
public Figure demo2() = (at(0,0, box(size=<50, 200> , lineWidth=60, align = bottomRight, 
lineColor="silver", fillColor = "yellow", lineOpacity=0.5))|newBox(e, 
          it)| e<-["green", "red", "blue", "grey", "magenta", "brown"]);
void tdemo2(){ render(demo2(), align = bottomRight, debug = false); }

// --------------------------------------------------------------------------


public Figure newEllipse(str lc, Figure el) {
      return at(0, 0, ellipse(align = topLeft, lineColor= lc, lineWidth = 19, 
           fillColor = "white", padding=<0,0,0,0>, 
      fig = el));
      }
public Figure demo3() = (idEllipse(100, 75) |newEllipse(e, 
      it)| e<-["red","blue" ,"grey","magenta", "brown", "green"]);
      
void tdemo3()  {render(demo3(), debug = false, align = topLeft);}
// ---------------------------------------------------------------------------

list[Vertex] innerGridH(int n) {
     num s = 1.0/n;
     return [move(0, y), line(1, y)|y<-[s,2*s..1]];
     }
     
list[Vertex] innerGridV(int n) {
     num s = 1.0/n;
     return [move(x, 0), line(x, 1)|x<-[s,2*s..1]];
     }

list[Vertex] innerSchoolPlot1() {
     return [move(10, i), line(10-i, 10)|i<-[0,0.5..10]];
     }
     
list[Vertex] innerSchoolPlot2() {
     return [move(i, 0), line(0, 10-i)|i<-[0,0.5..10]];
     }  
     
Figure schoolPlot() {
     return  overlay(width = 600, height = 600, figs = [
        shape(innerSchoolPlot1()+innerSchoolPlot2(), fillColor = "none",
        scaleX=<<0,10>,<0,400>>, scaleY=<<0,10>,<400,0>>, width = 400, height = 400, 
        lineColor = "blue"), 
        circle(r=40, cx = 200, cy = 200, fillColor = "yellow"
        ,lineWidth = 10, lineColor = "red", lineOpacity=0.5, fillOpacity=0.5, fig = text("Hello")
        )
        ,at(50, 50, circle(lineWidth=10, lineColor= "red", fillColor = "none",  padding=<10, 10, 10, 10>, fig= at(0,0, 
             box(width=50, height = 50, fillColor = "antiquewhite")
             )))
        ,at(250, 250, circle(lineWidth=10, fillColor="none", lineColor="brown", padding=<5, 5, 5, 5>
        , fig=ngon(n=7, r=40, lineWidth = 10, lineColor = "grey", fillColor = "none")))
        ])
        ;
     } 

Figure simpleGrid(Figure f) {
     return overlay(width = 400, height = 400, figs=[box(fig=shape(innerGridV(10)+innerGridH(10), 
     size=<400, 400>, scaleX=<<0,1>,<0,400>>, scaleY=<<0,1>,<0,400>>, fillColor = "none",
     lineColor = "lightgrey"), fillColor = "none"), f]);
     }
     
Figure labeled(Figure g) {
     return hcat(
        figs = [
           vcat(figs=gridLabelY(), padding=<0,0,0,20>)
           , vcat(lineWidth = 0, figs = [g, hcat(figs = gridLabelX())])
           ]);
     }
     
 list[Figure] gridLabelX() {
     return [box(lineWidth = 0, lineColor = "none", width = 40, height = 20, fig = text("<i>"))|i<-[1..10]];
     }
     
 list[Figure] gridLabelY() {
     return [box(lineWidth = 0, lineColor = "none", width = 40, height = 40, fig = text("<i>"))|i<-[9..0]];
     }
     
 Figure labeledGrid(Figure f) {
    return labeled(simpleGrid(f));   
    }
    
 Figure demo4() = labeledGrid(schoolPlot());
    
 void tfdemo4(loc l) {
      // println(schoolPlot());
      writeFile(l, toHtmlString(demo4(), debug = false, width = 600, height = 600));
      }
      
 void tdemo4() {
      // println(schoolPlot());
      render( 
      demo4(), width = 600, height = 600, align = topLeft, debug = false);
      }
      
void demo() = render(vcat(figs=[demo1(), demo2(), demo3(), demo4()]),
     width = 400, height = 1600);


Figure butt() = button("Click me");
void tbutt()= render(butt(), debug = false);

void tfbutt(loc l)= writeFile(l, toHtmlString(butt(), debug = false));