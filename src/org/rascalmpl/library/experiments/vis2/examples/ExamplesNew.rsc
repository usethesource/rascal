module experiments::vis2::examples::ExamplesNew
import experiments::vis2::FigureServer; 
import experiments::vis2::FigureNew; 
import util::Math;


public Figure box1 = box(fillColor="red", size=<200,200>);
void tbox1(){ ex("box1", box1); }  

void sinAndCosChart(){
        ex("sinAndCosChart", 
        	combo(charts=[line([<x, round(sin(x/10),0.001)>               | x <- [0.0, 1.0 .. 100.0]], name="Sine Wave"),
        		   line([<x, round(0.5 * cos(x/10), 0.01)>        | x <- [0.0, 1.0 .. 100.0]], name ="Cosine Wave"),
        		   line([<x, round(0.25 * sin(x/10) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 100.0]], name= "Another sine wave")
        			],
        	options = chartOptions(curveType="none",
           		hAxis = axis(title="Time"), 
           		vAxis = axis(title="Voltage"),
           		width=500,
                height=200,
                pointSize= 0,
                lineWidth = 1
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