module experiments::vis2::examples::SinAndCos
import experiments::vis2::FigureServer; 
import experiments::vis2::Figure; 
import util::Math;


public Figure box1 = box(fillColor="red", size=<200,200>);
void tbox1(){ ex("box1", box1); }  

void sinAndCos(){
        ex("sinAndCos", 
        	combo(charts=[
        	       line([<x, round(sin(x/1),0.001)>     | x <- [0.0, 1.0 .. 10.0]], name="Sine Wave"),
        		   line([<x, round(0.5 * cos(x/1), 0.01), "a<x>"> | x <- [0.0, 1.0 .. 10.0]], name ="Cosine Wave",
        		       lineWidth = 0, pointSize = 3),
        		   line([<x, round(0.25 * sin(x/1) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 9.0]], name= "Another sine wave")
        			],
        	options = chartOptions(curveType="function",
           		hAxis = axis(title="Time"), 
           		vAxis = axis(title="Voltage"),
           		width=500,
                height=200,
                pointSize= 0,
                lineWidth = 1,
                legend = legend(position="top")
        	    )
           
        ));
}
 
public list[Chart] q = 
     [
     line([<x, round(sin(x/10),0.01)>               | x <- [0.0, 1.0 .. 100.0]], name="Sine Wave"),
     line([<x, round(0.5 * cos(x/10), 0.01)>        | x <- [0.0, 1.0 .. 100.0]], name ="Cosine Wave"),
     line([<x, round(0.25 * sin(x/10) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 100.0]], name= "Another sine wave")
     ];

void ex(str title, Figure f){
	render(title, f);
}