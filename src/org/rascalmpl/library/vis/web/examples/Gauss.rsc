module vis::web::examples::Gauss

import Prelude;
import util::Math;
import vis::web::PlotFunction;
import util::Math;
import analysis::statistics::SimpleRegression;

     
public num(num) f(num sigma) = num(num x) {return exp(-0.5*x*x /(sigma*sigma) )/(sqrt(2*PI())*sigma);};
public list[tuple[str name, list[num] ticks, num(num) g]] 
    gauss= [<"<i>", [-1,-0.9..1.001], f(i)>|i<-[0.1,0.2..1]];
    
public void main() {
     plotFunction(gauss, style="splines");
     }