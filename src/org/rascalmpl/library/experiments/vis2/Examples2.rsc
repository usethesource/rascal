module experiments::vis2::Examples2

import experiments::vis2::Figure;
import experiments::vis2::Properties;
import experiments::vis2::FigureServer;

// ********************** Examples **********************

void ex(str title, Figure f){ generateInitialFigure(title, f); }

void ex1(){
	int counter = 0;
	str getCounter() = "... <counter>...";
	ex("ex1", vcat([ box(text("Click me"), onClick((){ counter += 1; }), fontSize(20), gap(2,2), fillColor("whitesmoke")),
					 text(getCounter, size(150, 50), fontSize(30))
				   ], align(left(),vcenter())));
}

void ex2(){
	str color = "red";
	str getFillColor() { return color; }
	ex("ex2", hcat([ text("Enter:", size(150, 50), fontSize(18), font("Helvetica")), 
	                 textfield((str s){ color = s; }, size(100,25)), 
	                 box(fillColor(getFillColor), size(100,100))
				   ], gap(10,10)));
}

void ex3(){
	ex("ex3", figure(("color" : "red"),
				hcat([ text("Enter:", size(150, 50), fontSize(18), font("Helvetica")), 
	                   textfield(def("color"), size(100,25)), 
	                   box(fillColor(use("color")), size(100,100))
				   ], gap(10,10)))
			  );
}

