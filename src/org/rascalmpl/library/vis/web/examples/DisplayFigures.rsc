module vis::web::examples::DisplayFigures

import util::Math;
import vis::web::PlotFunction;

int n = 6;

num cos3(num x) = cos(x) * cos(x) * cos(x);

num sin3(num x) = sin(x) * sin(x) * sin(x);

str labx(int d) {
       switch(d) {
        case 0: return "0";
        case 1: return "\u03A0/6";
        case 2: return "\u03A0/3";
        case 3: return "\u03A0/2";
        case 4: return "2\u03A0/3";
        case 5: return "\u03A0/6";
        case 6: return "\u03A0";
        default: return "<d>";
        }
        return "";    
    }
    
 str laby(int d) {
       num r(num v) = round(v,0.01);
       return "<r((d-5.0)/5.0)>";
       }
       
list[str] xLab = [labx(i)|i<-[0..7]];
list[str] yLab = [laby(i)|i<-[0..11]];


lrel[str, lrel[num, num]] dsin = [<"sin",  [<(2*PI()*i/n), sin(2*i*PI()/n)>|i<-[0..n+1]]>];
lrel[str, lrel[num, num]] dcos = [<"cos",   [<(2*PI()*i/n),cos(2*i*PI()/n)>|i<-[0..n+1]]>];
lrel[str, lrel[num, num]] dsin3 = [<"sin3", [<(2*PI()*i/n),sin3(2*i*PI()/n)>|i<-[0..n+1]]>]; 
lrel[str, lrel[num, num]] dcos3 = [<"cos3", [<(2*PI()*i/n),cos3(2*i*PI()/n)>|i<-[0..n+1]]>];

public void main() { 
   plot(dsin+dcos+dsin3+dcos3, y= -1, x= 0, height = 2, width = PI(),
   xticks=xLab, yticks = yLab);
   }
   
/* 
   lrel[str, lrel[num, num]]  circles1 = [<"<r>", [
<r*cos(PI()*i/n), r*sin(5*PI()*i/n)>|i<-[0..2*n+1]]>
|r<-[1]];

lrel[str, lrel[num, num]]  circles2 = [<"<r>", [
<r*r*r*cos(PI()*i/n), r*sin(PI()*i/n)>|i<-[0..2*n+1]]>
|r<-[0.5,0.55..1.05]];
*/
//plot(circles1+circles2, x=-1.5, y=-1.5, width= 3, height = 3, style="splines"); 