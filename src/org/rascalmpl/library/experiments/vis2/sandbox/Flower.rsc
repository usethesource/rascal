module experiments::vis2::sandbox::Flower
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import util::Math;
import Prelude;

Vertices flowShape1(num d) {
     return [line(x-d*x*(1-x), x+d*x*(1-x))| x<-[0.1,0.2..1.1]];
     }
     
Vertices flowShape2(num d) {
     return [line(x+d*x*(1-x), x-d*x*(1-x))| x<-[1,0.9..0]];
     }


Figure leaf(int a) {
         int w = 50; int h = 50;
         num d = 0.5*sqrt(2);
         return rotate(a, box(size=<w, h>, 
            fig = shape([move(0,0)]+flowShape1(0.5)+flowShape2(0.5)
           ,scaleX=<<0,1>,<(1-d)/2*w, (1+d)/2*w>>, scaleY=<<0,1>,<(1-d)/2*h,(1+d)/2*h>> , shapeCurved= false, fillColor= "red")
           ,align= centerMid, fillColor = "none", lineWidth = 0));
         // return rotate(a, box(lineWidth = 0, fig= box(lineWidth = 1, size=<w, h>, align = centerMid, fig = circle(r=10, fillColor = "red"))));
        }

Figure flower() {
    int n  = 16;
    int r = 40;
    list[Figure] fs = 
    [at(r+toInt(r*sin(p)), r+toInt(r*cos(p)), leaf(45-toInt((180*p)/PI())))|p<-[0,2*PI()/n..2*PI()]]
    +at(25, 25, box(lineWidth =0, size=<2*r, 2*r>, align = centerMid, fillColor="none", fig=circle(r=0.6*r
    , fillColor = "yellow", fillOpacity=0.7, fig = circle(r=0.1*r, fillColor="green"))))
    ;
    return overlay(size=<150, 150>, lineWidth = 0, figs=fs);
    }
    
void tflower() = render(flower());