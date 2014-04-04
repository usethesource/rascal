module vis::web::examples::RegEq
import vis::web::PlotFunction;
import Prelude;
import util::Math;
import vis::web::markup::D3;
import analysis::statistics::SimpleRegression;

list[PF] getReg(lrel[num, num] v) {
      num c = intercept(v);
      num a = slope(v);
      num q(num x) = c + a*x;
      return [f(q), p(v)];
      }
      
int n = 1000;

void main() {
     lrel[num, num] dots = [<arbReal(), arbReal()>|int i<-[0..n]];
     list[PF] z = getReg(dots);
     plotFunction(z, x= 0, y=0, width = 1, nTickx = 10, height = 1, nTicky = 10, nStep=30,  style="splines", 
     symbolSize=40);
     }