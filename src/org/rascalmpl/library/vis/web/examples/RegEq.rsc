module vis::web::examples::RegEq
import vis::web::PlotFunction;
import Prelude;
import util::Math;
import vis::web::markup::D3;
import analysis::statistics::SimpleRegression;

tuple[str name, lrel[num,num] t] getReg(lrel[num, num] v) {
      num c = intercept(v);
      num a = slope(v);
      num q(num x) = c + a*x;
      return pf(<"reg", [0,0.1..1.1], q>);
      }
      
int n = 100;

void main() {
     tuple[str name, lrel[num, num] dots] d  = <"dots", [<arbReal(), arbReal()>|int i<-[0..n]]>;
     tuple[str name, lrel[num,num] t]  z = getReg(d.dots);
     plot([z,d], x= 0, y=0, width = 1, nTickx = 10, height = 1, nTicky = 10, nStep=30,  style="splines", 
     symbolSize=40, styleMap=("dots":"dots"));
     }