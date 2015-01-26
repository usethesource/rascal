module experiments::vis2::examples::RegEq
import Prelude;
import util::Math;
import analysis::statistics::SimpleRegression;
import experiments::vis2::vega::VegaChart;
import experiments::vis2::FigureServer;
import experiments::vis2::Figure; 
import experiments::vis2::vega::ParameterTypes;

void ex(str title, Figure f){
	render(title, f);
}

lrel[num, num] pf(list[num] ticks, num(num) g) {
   return [<x, g(x)>|x<-ticks];
   }

lrel[num,num] getReg(lrel[num, num] v) {
      num c = intercept(v);
      num a = slope(v);
      num q(num x) = c + a*x;
      return pf([0,0.1..10.1], q);
      }
      
int n = 10;

lrel[num, num] points = [<floor(10*arbReal()), floor(10*arbReal())>|int i<-[0..n]];



Datasets[XYData] datasets = ("points": points
                            ,"line": getReg(points)
                            );
                            

                                   
public void regressionChart(){
      ex("regression", vegaChart(size=<500,200>, datasets=datasets, command=linePlot(shape=("points":"square") 
       ,interpolate=("line":"monotone"), legends = ("color":"fill"))));
      }
    

      

      
