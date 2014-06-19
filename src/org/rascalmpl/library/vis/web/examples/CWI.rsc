module vis::web::examples::CWI

import util::Math;
import IO;
import List;
import vis::web::PlotFunction;
import vis::web::markup::D3;
import util::HtmlDisplay;


int n = 500;

tuple[num, num] q(num r, num teta) = <r*cos(teta), r*sin(teta)>;

//tuple[num, num] q(num r, num teta) {
//   // println("<r*cos(teta)> <r*sin(teta)>");
//   println("<r> <teta>");
//   return <r*cos(teta), r*sin(teta)>;
// }

num t(num x) = (10*PI()*x/n);

num t(num x, int k) = (k*PI()*x/n);

num f1(num x) = 1 + cos(x);
lrel[str, lrel[num, num]] f4 = [<"epicycloide",  [<8*cos(t(i))-3*cos(8*t(i)/3), 8 * sin(t(i))- 3 * sin(8*t(i)/3) >|i<-[0..n+1]]>];
lrel[str, lrel[num, num]] j4 = [<"epitrochoide",  [<10*cos(t(i))-4*cos(10*t(i)/3), 10 * sin(t(i))- 4 * sin(10*t(i)/3) >|i<-[0..n+1]]>];
lrel[str, lrel[num, num]] f7  = [<"evolvente",  [<cos(t(i))+t(i)*sin(t(i)), sin(t(i))-t(i)*cos(t(i)) >|i<-[0..n+1]]>];
lrel[str, lrel[num, num]] i1  = [<"lissajous",  [<2*sin(4*t(i)), cos(6*t(i)) >|i<-[0..n+1]]>];
lrel[str, lrel[num, num]] e6   = [<"cardioide",  [q(1+cos(t(i)), t(i))|i<-[0..n+1]]>];
lrel[str, lrel[num, num]] e5   = [<"cochleoide",  [q(sin(t(i))/t(i), t(i))|i<-[1..n+1]]>];
lrel[str, lrel[num, num]] a3   = [<"conchoide",  [q((4*cos(2*t(i,1))+1)/cos(t(i,1)), t(i,1))|i<-[n/2..3*n/2], abs(cos(t(i,1)))>0.3 ]>];
//lrel[str, lrel[num, num]] c4   = [<"kampyle",  [q(1/(cos(t(i,2))*cos(t(i,2))), t(i,2))|i<-[1..n+1],abs(cos(t(i,2)))>0.000001 ]>];
//list[tuple[num, num]] z=  [q(-sqrt(cos(2*t(i,1))), t(i,1))|i<-[-n/4+1,-n/4+2..n/4]];
//lrel[str, lrel[num, num]] h5   = [<"lemniscaat", z
//+[q(sqrt(cos(2*t(i,1))), t(i,1))|i<-[n/4-1, n/4-2..-n/4]]+head(z)>];
lrel[str, lrel[num, num]] i3   = [<"rhodonea", [q(cos(2*t(i,2)), t(i, 2))|i<-[0..n+1]]>];


int Vwidth = 200;
int Vheight = 200;


public void main() {
     // plotFunction(cardioide, style="splines");
     loc location = |file:///tmp/flower|;
     list[list[str]] bodies =[];
     list[str] body = [];
     num f = 1;
     int margin = 5;
     body += plotHtml(location, "f4", f4, style="splines", symbolSize=1,
     x = -12, y = -12, width = 24, height = 24, viewWidth = Vwidth, viewHeight = Vwidth,
     factor = f,  margin = margin, x_axis = false, y_axis = false
     );
      body += plotHtml(location, "j4", j4, style="splines", symbolSize=1
      , viewWidth = Vwidth, viewHeight = Vheight
      , factor = f,  margin = margin, x_axis = false, y_axis = false  
      , x=0, y=0   
     );    
      body += plotHtml(location, "f7", f7, style="splines", symbolSize=1   
      , viewWidth = Vwidth, viewHeight = Vheight
      , factor = f,  margin = margin, x_axis = false, y_axis = false
      , x=0, y=0     
     ); 
      body += plotHtml(location, "i1", i1, style="splines", symbolSize=1   
      , viewWidth = Vwidth, viewHeight = Vheight
      , factor = f,  margin = margin, x_axis = false, y_axis = false
      , x=0, y=0     
     );    
     bodies=[body];   
     body = [];   
      body += plotHtml(location, "e6", e6, style="splines", symbolSize=1   
      , viewWidth = Vwidth, viewHeight = Vheight
      , factor = f,  margin = margin, x_axis = false, y_axis = false
      , x=0, y=0     
     ); 
     body += plotHtml(location, "e5", e5, style="splines", symbolSize=1   
      , viewWidth = Vwidth, viewHeight = Vheight
      , factor = f,  margin = margin, x_axis = false, y_axis = false
      , x=0, y=0     
     );  
     body += plotHtml(location, "a3", a3, style="splines", symbolSize=1  
      , viewWidth = Vwidth, viewHeight = Vheight
      , factor = f,  margin = margin, x_axis = false, y_axis = false
      , x=0, y=0     
     ); 
      body += plotHtml(location, "i3", i3, style="splines", symbolSize=1  
      , viewWidth = Vwidth, viewHeight = Vheight
      , factor = f,  margin = margin, x_axis = false, y_axis = false
      , x=0, y=0     
     );        
     bodies += [body];
     // println(bodies);      
     str header =CSS((
            "td":
            ("border-collapse":"collapse", "vertical-align":"top")     
             )
             );    
      writeFile(location+"index.html", html(header, 
               W3(table_, "<for (bod<-bodies) {> 
                  <W3(tr_, "<for (b<- bod) {> 
                            <W3(td_, b)> <}>")>
                  <}>"
      )));
      htmlDisplay(location+"index.html");
     }
