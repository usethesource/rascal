module vis::web::PlotFunction
import Prelude;
import util::Math;
import lang::json::IO;
import util::HtmlDisplay;
import vis::web::markup::D3;

str labx(int d, num x, num tickx) {
    return "<precision(x+d*tickx, 2)>";
    }

str laby(int d, num y, num ticky) {
    return "<precision(y+d*ticky,2)>";
    }
    

public void plotFunction(num(num) g ..., num x = 0, num y = 0, num width = 10.0, num height =10, int nTickx = 10,
    int nTicky = 10, int nStep=100, int viewWidth= 600, int viewHeight = 600) {
    num tickx = 1.0/nTickx;
    num ticky = 1.0/nTicky;
    num step =  1.0/nStep;
    list[str] xt =[labx(i, x, tickx * width)|int i<-[0..nTickx+1]];
    list[str] yt =[laby(i, y, ticky * height)|int i<-[0..nTicky+1]];
    list[list[tuple[num, num]]] d = [[<(i*step)*nTickx, (-y+f(x+(i*step)*width))*nTicky/height >| i<-[0..nStep+1]]|f<-g];
    list[tuple[str, str, list[tuple[num, num]]]] w =  [<"<i>","blue", d[i]>|i<-[0..size(d)]];
    PlotData p = <"plot", xt, yt, w>;
    plot(p, width=viewWidth, height=viewHeight);  
    }    
  

public void main() {
    list[num(num)] g = [
    	num (num x) (num ii) {
    	return num (num x) {return 0.05*ii*x*x;};
    	}(i)
    |int i<-[1..20]]; 
    //plotFunction(g, x= 0, y=0, width = 2, nTickx = 10, height = 2, nTicky = 10, nStep=100);
    plotFunction(num(num x){return -1.00000001;}  , cos, width = 6, y = -1, height = 2, nTickx = 6, nTicky = 4
    , nStep=100, viewWidth = 600,viewHeight= 400);
    }