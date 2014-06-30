module experiments::vis2::Examples

import experiments::vis2::Figure;
import experiments::vis2::FigureServer;

import String;
import util::Math;

// ********************** Examples **********************

void ex(str title, Figure f){
	render(title, f);
}

void ex(str title, value model, Figure f){
	render(title, model, f);
}

// single box

void ex0(){
	ex("ex0", box());
} 

void ex01(){
	ex("ex01", box(fillColor("red"), size(100,100)));
}  

void ex02(){
	ex("ex02", box(fillColor("red"), lineColor("blue"), lineWidth(10), lineStyle([10,20,10,10]), size(100,100)));
}

void ex03(){
	ex("ex03", box(fillColor("red"), lineColor("blue"), lineWidth(1), size(100,100), pos(50,50)));
}

// Nested box

void ex10(){
	ex("ex10", box(box(fillColor("white"), size(50,100)), fillColor("blue"), size(200,200)));
} 

void ex11(){
	ex("ex11", box(box(fillColor("white"), size(50,100), align(left(), top())), fillColor("blue"), size(200,200), gap(0,0)));
} 

void ex12(){
	ex("ex12", box(box(fillColor("white"), size(50,100), align(right(), top())), fillColor("blue"), size(200,200), gap(0,0)));
} 

void ex13(){
	ex("ex13", box(box(fillColor("white"), size(50,100), align(right(), bottom())), fillColor("blue"), size(200,200), gap(0,0)));
} 

void ex14(){
	ex("ex14", box(box(fillColor("white"), size(50,100), align(left(), bottom())), fillColor("blue"), size(200,200), gap(0,0)));
} 


void ex15(){
	ex("ex15", box(box(fillColor("white"), size(50,100), align(left(), top())), fillColor("blue"), size(200,200), gap(10,10)));
} 

void ex16(){
	ex("ex16", box(box(fillColor("white"), size(50,100), align(right(), top())), fillColor("blue"), size(200,200), gap(10,10)));
} 

void ex17(){
	ex("ex17", box(box(fillColor("white"), size(50,100), align(right(), bottom())), fillColor("blue"), size(200,200), gap(10,10)));
} 

void ex18(){
	ex("ex18", box(box(fillColor("white"), size(50,100), align(left(), bottom())), fillColor("blue"), size(200,200), gap(10,10)));
} 

void ex19(){
	ex("ex19", box(box(box(fillColor("red"), size(20,20)), fillColor("white"), size(50,100), align(left(), bottom())), fillColor("blue"), size(200,200), gap(10,10)));
} 

void ex19a(){
	ex("ex19a", box(box(box(fillColor("red"), size(20,20), align(left(), top())), fillColor("white"), size(50,100), align(left(), bottom())), fillColor("blue"), size(200,200), gap(10,10)));
}

void ex19b(){
	ex("ex19b", box(box(box(fillColor("red"), size(20,20), align(right(), top())), fillColor("white"), size(50,100), align(left(), bottom())), fillColor("blue"), size(200,200), gap(10,10)));
}

void ex19c(){
	ex("ex19c", box(box(box(fillColor("red"), size(20,20), align(right(), bottom())), fillColor("white"), size(50,100), align(left(), bottom())), fillColor("blue"), size(200,200), gap(10,10)));
}

// hcat  
        
void ex20(){
	ex("ex20", hcat([box(fillColor("red"),size(50,100)), box(fillColor("green"), size(200,200)), box(fillColor("blue"), size(10,10))], align(right(), top())));
}

void ex21(){
	ex("ex21", hcat([box(fillColor("red"),size(50,100)), box(fillColor("green"), size(200,200)), box(fillColor("blue"), size(10,10))], align(right(), vcenter())));
}

void ex22(){
	ex("ex22", hcat([box(fillColor("red"),size(50,100)), box(fillColor("green"), size(200,200)), box(fillColor("blue"), size(10,10))], align(right(), bottom())));
}

void ex23(){
	ex("ex23", hcat([box(fillColor("red"),size(50,100)), box(fillColor("green"), size(200,200)), box(fillColor("blue"), size(10,10))], align(right(), bottom()), gap(10,10)));
}

void ex24(){
	ex("ex24", hcat([box(size(2,2),pos(x, y)) | <x, y> <- [<0,0>, <10,10>, <20,20>]]));
}

void ex25(){
	ex("ex25", hcat([box(size(2,2),pos(toInt(x * 2), toInt(100 * sin(x)))) | real x <- [0.0, 0.1 .. 10.0]]));
} 

// hcat in box

void ex30(){
	ex("ex30", box(
					hcat([ box(fillColor("red"),size(50,100)), 
						   box(fillColor("green"), size(200,200)), 
						   box(fillColor("blue"), size(10,10))
						 ], align(left(), top()), gap(10,10)),
					fillColor("grey")));
}

void ex31(){
	ex("ex31", box(
					hcat([ box(fillColor("red"),size(50,100)), 
						   box(fillColor("green"), size(200,200)), 
						   box(fillColor("blue"), size(10,10))
						 ], align(left(), top()), gap(10,10)),
					size(400, 400), align(left(), top()), fillColor("grey")));
}

void ex32(){
	ex("ex32", box(
					hcat([ box(fillColor("red"),size(50,100)), 
						   box(fillColor("green"), size(200,200)), 
						   box(fillColor("blue"), size(10,10))
						 ], align(left(), top()), gap(10,10)),
					size(400, 400), align(right(), top()), fillColor("grey")));
}

void ex33(){
	ex("ex33", box(
					hcat([ box(fillColor("red"),size(50,100)), 
						   box(fillColor("green"), size(200,200)), 
						   box(fillColor("blue"), size(10,10))
						 ], align(left(), top()), gap(10,10)),
					size(400, 400), align(right(), bottom()), fillColor("grey")));
}

void ex34(){
	ex("ex34", box(
					hcat([ box(fillColor("red"),size(50,100)), 
						   box(fillColor("green"), size(200,200)), 
						   box(fillColor("blue"), size(10,10))
						 ], align(left(), top()), gap(10,10)),
					size(400, 400), align(left(), bottom()), fillColor("grey")));
}

void ex35(){
	ex("ex35", box(
					hcat([ box(fillColor("red"),size(50,100)), 
						   box(fillColor("green"), size(200,200)), 
						   box(fillColor("blue"), size(10,10))
						 ], align(hcenter(), vcenter()), gap(10,10)),
					size(400, 400), align(left(), top()), fillColor("grey")));
}

void ex36(){
	ex("ex36", box(
					hcat([ box(fillColor("red"),size(50,100)), 
						   box(fillColor("green"), size(200,200)), 
						   box(fillColor("blue"), size(10,10))
						 ], align(hcenter(), vcenter()), gap(10,10)),
					size(400, 400), align(right(), top()), fillColor("grey")));
}

void ex37(){
	ex("ex37", box(
					hcat([ box(fillColor("red"),size(50,100)), 
						   box(fillColor("green"), size(200,200)), 
						   box(fillColor("blue"), size(10,10))
						 ], align(hcenter(), vcenter()), gap(10,10)),
					size(400, 400), align(right(), bottom()), fillColor("grey")));
}

void ex38(){
	ex("ex38", box(
					hcat([ box(fillColor("red"),size(50,100)), 
						   box(fillColor("green"), size(200,200)), 
						   box(fillColor("blue"), size(10,10))
						 ], align(hcenter(), vcenter()), gap(10,10)),
					size(400, 400), align(left(), bottom()), fillColor("grey")));
}


// vcat

void ex40(){
	ex("ex40", vcat([box(fillColor("red"),size(50,100)), box(fillColor("green"), size(200,200)), box(fillColor("blue"), size(10,10))], align(left(), top())));
}

void ex41(){
	ex("ex41", vcat([box(fillColor("red"),size(50,100)), box(fillColor("green"), size(200,200)), box(fillColor("blue"), size(10,10))], align(hcenter(), top())));
}

void ex42(){
	ex("ex42", vcat([box(fillColor("red"),size(50,100)), box(fillColor("green"), size(200,200)), box(fillColor("blue"), size(10,10))], align(right(), top())));
}

void ex43(){
	ex("ex43", vcat([box(fillColor("red"),size(50,100)), box(fillColor("green"), size(200,200)), box(fillColor("blue"), size(10,10))], align(right(), top()), gap(10,10)));
}

void ex44(){
	ex("ex44", vcat([box(fillColor("red"),size(100,100)), box(fillColor("green"), size(200,200))], align(left(), top())));
}

list[num] DATA1 = [ 11, 12, 15, 20, 18, 17, 16, 18, 23, 25 ];

void ex50(){
	ex("ex50", barchart(fillColor("black"), size(400,300), dataset(DATA1)));
}

void ex51(){
	ex("ex50", hcat([barchart(fillColor("blue"), size(400,300), dataset(DATA1)), 
					 box(fillColor("red"),size(100,100)), 
					 barchart(fillColor("black"), size(200,300), dataset(DATA1)),
					 box(fillColor("yellow"),size(50,50))
					], align(right(), bottom())));
}

lrel[num,num] DATA2 = [<5, 20>, <480, 90>, <250, 50>, <100, 33>, <330, 95>,<410, 12>, <475, 44>, <25, 67>, <85, 21>, <220, 88>, <600, 150>];

void ex60(){
	ex("ex60", scatterplot(fillColor("black"), size(400,300), dataset(DATA2)));
}

void ex61(){
	ex("ex61", hcat([scatterplot(fillColor("blue"), size(400,300), dataset(DATA2)), 
					 //box(fillColor("red"),size(100,100)), 
					 scatterplot(fillColor("black"), size(400,300), dataset(DATA2))
					 //box(fillColor("yellow"),size(50,50))
					], align(right(), bottom())));
}

void ex62(){
	ex("ex62", hcat([scatterplot(fillColor("blue"), size(400,300), dataset(DATA2)), 
					 //box(fillColor("red"),size(100,100)), 
					 barchart(fillColor("black"), size(400,300), dataset(DATA1))
					 //box(fillColor("yellow"),size(50,50))
					], align(left(), vcenter())));
}


Figures nodes1 = [ /* 0 */	hcat([box(fillColor("green"), size(50,50)),box(fillColor("yellow"), size(50,50)), box(fillColor("gray"), size(50,50))]),
          		   /* 1 */	box(fillColor("red"), fillOpacity(0.4), size(100,100)),
     	    	   /* 2 */	box(fillColor("lightblue"), rounded(10,10), lineStyle([1,1,1,1,1,1]), size(150,150))
     	  		];
Edges edges1 = [edge(0,1, lineColor("orange")), edge(1,2, lineWidth(3), lineOpacity(0.3)), edge(2,0, lineStyle([4,2,4,2]))];        

void ex70(){
	ex("ex70", graph(nodes1, edges1, size(500,500)));
}

void ex71(){
	ex("ex71", hcat([graph(nodes1, edges1, size(400,400)),
					 scatterplot(fillColor("blue"), size(400,300), dataset(DATA2)),
					 barchart(fillColor("black"), size(400,300), dataset(DATA1))
					], gap(50,50)));
}

//void ex72(){
//	ex("ex72", hcat([graph(nodes1, edges1, size(400,400)),
//					 scatterplot(fillColor("blue"), size(400,300), dataset(DATA2)),
//					 texteditor(size(200,200)),
//					 barchart(fillColor("black"), size(400,300), dataset(DATA1))
//					], gap(50,50)));
//}

void ex80(){
	ex("ex80", text("Hello", font("sans-serif"), fillColor("black"), fontSize(20)));
}

void ex81(){
	ex("ex81", box(text("Hello", fillColor("black"), fontSize(20)), fillColor("white")));
}

void ex82(){
	ex("ex82", hcat([ box(text("Hello", fillColor("black")), fillColor("white")),
					  text("World")
					], fontSize(20)));
}

/************** Interaction *****************/

data M200 = m200(int counter = 666);

void ex200(){
	
	render("ex200",  #M200, m200(), Figure (M200 m) {
			return
				vcat([ box(text("Click me", on("click", bind(m.counter, m.counter + 1)), fontSize(20), gap(2,2)), fillColor("whitesmoke")),
					   text(m.counter, size(150, 50), fontSize(30))
				     ]);
			});
}

void ex201(){
	
	render("ex201",  #M200, m200(),  Figure (M200 m) {
			return
				vcat([ box(text("Click me 1", on("click", bind(m.counter, m.counter + 1)), fontSize(20), gap(2,2)), fillColor("whitesmoke")),
					   text(m.counter, size(150, 50), fontSize(30)),
					   box(text("Click me 2", on("click", bind(m.counter, m.counter + 1)), fontSize(20), gap(2,2)), fillColor("whitesmoke")),
					   text(m.counter, size(150, 50), fontSize(50)),
					   text(m.counter, size(150, 50), fontSize(80))
				     ]);
			});
}

void ex202(){
	
	render("ex202",  #M200, m200(), Figure (M200 m) {
			return
				vcat([ buttonInput( "Click me", "Click me", on("click", bind(m.counter, m.counter + 1)), size(80, 40)),
					   text(m.counter, size(150, 50), fontSize(30))
				     ]);
			});
}

void ex203(){
	
	render("ex203",  #M200, m200(), Figure (M200 m) {
			return
				vcat([ buttonInput( "Click me", "Click me", on("click", bind(m.counter, m.counter + 1)), size(80, 40)),
					   text(m.counter, size(150, 50), fontSize(30)),
					   box(size(50,50), lineColor("white")),
					   buttonInput( "Click me", "Click me", on("click", bind(m.counter, m.counter + 1)), size(100, 40)),
					   text(m.counter, size(150, 50), fontSize(50)),
					   text(m.counter, size(150, 50), fontSize(80))
				     ]);
			});
}

data M204 = m204(str TXT = "abc");

void ex204(){
	render("ex204", #M204, m204(), Figure (M204 m) {
			return
				hcat([ 		
	                   strInput(on("submit", bind(m.TXT)), size(100,25)), 
	                   text(m.TXT, size(150, 50), fontSize(50)),
	                   text(m.TXT, size(150, 50), fontSize(80))
	                  
				   ], gap(20,20));
			});
}

data M205 = m205(str C = "red");

void ex205(){
	render("ex205", #M205, m205(), Figure (M205 m) {
			return
				hcat([ text("Enter:", size(150, 50), fontSize(18)), 
				
	                   strInput(on("submit", bind(m.C)), size(100,25)), 
	                   
	                   box(lineColor(m.C), lineWidth(10), size(100,100)),
	                   
	                   box(lineColor(m.C), lineWidth(10), size(100,100))
				   ], gap(20,20));
			  });
}

data M206 = m206(str C = "red");

void ex206(){
	render("ex206", #M206, m206(), Figure (M206 m) {
			return
				hcat([ text("Enter:", size(150, 50), fontSize(18)), 
				
	                   colorInput(on("change", bind(m.C)), size(100,25)), 
	                   
	                   box(lineColor(m.C), lineWidth(10), size(100,100)),
	                   
	                   box(lineColor(m.C), lineWidth(10), size(100,100))
				   ], gap(20,20));
			  });
}

data M207 = m207(str FC = "red", int LW = 1, int WIDTH = 100, int HEIGHT = 100);

void ex207(){
	render("ex207", #M207, m207(), Figure (M207 m) {
			return
				vcat([
					hcat([ text("  fillColor:", size(150, 50), fontSize(20)), colorInput(on("change", bind(m.FC)), size(100,25)),
				
					       text("lineWidth:", size(150, 50), fontSize(20)), numInput(on("change", bind(m.LW)), size(80,25)),
					
					       text("     width:", size(150, 50), fontSize(20)), numInput(on("change", bind(m.WIDTH)), size(100,25)),
					
					       text("    height:", size(150, 50), fontSize(20)), numInput(on("change", bind(m.HEIGHT)), size(100,25))
					     ]),
					
					box(size(100,100), lineWidth(0)),
					
	                box(fillColor(m.FC), lineWidth(m.LW), width(m.WIDTH), height(m.HEIGHT))
	                   
				   ], gap(30,30));
			  });
}

data M208 = m208(int SEL = 0);

void ex208(){
	render("ex208", #M208, m208(),  Figure (M208 m) {
			return
			hcat([ text("Enter:", size(150, 50), fontSize(18)), 
			
	               numInput(on("change", bind(m.SEL)), size(100,25)),
	               
				   choice(m.SEL, [ box(fillColor("red"), size(100,100)),
								   box(fillColor("white"), size(100,100)),
								   box(fillColor("blue"), size(100,100))
								 ])],
					gap(30,30));
					});
}

data M209 = m209(int SEL = 0);

void ex209(){
	render("ex209", #M209, m209(),  Figure (M209 m) {
			return
			hcat([ text("Enter:", size(150, 50), fontSize(18)), 
			
	               choiceInput(["red", "white", "blue"], on("change", bind(m.SEL)), size(100,25)),
	               
				   choice(m.SEL, [ box(fillColor("red"), size(100,100)),
								   box(fillColor("white"), size(100,100)),
								   box(fillColor("blue"), size(100,100))
								 ])],
					gap(30,30));
					});
}

data M210 = m210(int SLIDER = 50);

void ex210(){
	render("ex210", #M210, m210(), Figure (M210 m) {
			return
			vcat([ hcat([text("0"), rangeInput(0,100,5, on("change", bind(m.SLIDER)), size(150, 50)), text("100")]),
			
				   text(m.SLIDER, size(150, 50), fontSize(30))
	             ],			  
				 gap(10,20));
				 });
}

data M211 = m211(int WIDTH = 50, int HEIGHT = 50);

void ex211(){

	render("ex211", #M211, m211(), Figure (M211 m) {
			return vcat([ hcat([text("WIDTH"), text("0"), rangeInput(0,100,5, on("change", bind(m.WIDTH)), size(150, 50)), text("100")]),
			       hcat([text("HEIGHT"), text("0"), rangeInput(0,100,5, on("change", bind(m.HEIGHT)), size(150, 50)), text("100")]),
			
				   box(width(m.WIDTH), height(m.HEIGHT), fillColor("pink"))
	             ],			  
				 gap(10,20));
		});
}

data M212 = m212(int SLIDER_VAL = 25);

void ex212(){
	render("ex212", #M212, m212(), Figure (M212 m) {
			return 
			vcat([ rangeInput(0, 50, 5, on("change", bind(m.SLIDER_VAL)), size(200, 50)),
				   box(size(50,50), lineWidth(0)),
				   box(lineWidth(m.SLIDER_VAL), size(150, 50), fillColor("red"))
	             ], align(left(), top()),		  
				 gap(80,80));
				 });
}

data M213 = m213(bool VISABLE = true);

void ex213(){
	render("ex213", #M213, m213(), Figure (M213 m) {
			return 
			vcat([ buttonInput( "hide", "show", on("click", bind(m.VISABLE)),size(50, 50)),
				   
				   visible(m.VISABLE,  box(size(150, 50), fillColor("red")))
	             ], align(left(), top()),		  
				 gap(30,30));
				 });
}

data M214 = m214(bool VISABLE = true);

void ex214(){
	render("ex214", #M214, m214(), Figure (M214 m) {
			return 
			vcat([ checkboxInput(on("click", bind(m.VISABLE)), size(50, 50)),
				   
				   visible(m.VISABLE,  box(size(150, 50), fillColor("red")))
	             ], align(left(), top()),		  
				 gap(30,30));
				 });
}

// Resize scatterplot

data M215 = m215(int SIZE = 300);

void ex215(){

	render("ex215", #M215, m215(), Figure (M215 m) {
			low = 100;
			high = 500;
			return vcat([ hcat([text("SIZE"), text(low), rangeInput(low,high,5, on("change", bind(m.SIZE)), size(500, 50)), text(high)]),
				   
				    scatterplot(fillColor("black"), width(m.SIZE), height(m.SIZE), dataset(DATA2))
	             ],			  
				 gap(10,20));
		});
}

// Tooltip

data M216 = m216(int SIZE = 300);

void ex216(){

	render("ex216", #M216, m216(), Figure (M216 m) {
			low = 100;
			high = 500;
			
			FProperty tooltip(str txt) = on("mouseover", box(text(txt, fontSize(12), lineColor("black")), fillColor("yellow")));
			
			return 
				vcat([ box(size(200,50), lineColor("white")),
					   hcat([ box(fillColor("red"), width(100), height(100),  tooltip("I am a red box")),
						      box(fillColor("blue"), width(100), height(100), tooltip("I am a blue box")),
						      box(fillColor("white"), width(100), height(100))
	                        ])
	                  ],		  
				 gap(10,20));
		});
}


data M217 = m217(str C = "white");

void ex217(){
          
	render("ex217", #M217, m217(), Figure (M217 m) {
			return box(colorInput(on("change", bind(m.C)), size(50,20)), fillColor(m.C), rounded(10,10), gap(20,20), lineStyle([1,1,1,1,1,1]));
		});
}

data M218 = m218(str C1 = "white", str C2 = "blue");

void ex218(){
          
	render("ex218", #M218, m218(), Figure (M218 m) {
			return hcat([ box(colorInput(on("change", bind(m.C1)), size(50,20)), fillColor(m.C1), rounded(10,10), gap(20,20), lineStyle([1,1,1,1,1,1])),
						  box(colorInput(on("change", bind(m.C2)), size(50,20)), fillColor(m.C2), rounded(10,10), gap(20,20), lineStyle([1,1,1,1,1,1]))
						], gap(20,30));
		});
}

data M219 = m219(str C = "blue");

void ex219(){
          
	render("ex219", #M219, m219(), Figure (M219 m) {
			Figures nodes1 = [  /* 0 */	hcat([box(fillColor("green"), size(50,50)),box(fillColor("yellow"), size(50,50)), box(fillColor("gray"), size(50,50))]),
          		       			/* 1 */	box(fillColor("red"), fillOpacity(0.4), size(100,100)),
     	    	      			/* 2 */	box(colorInput(on("change", bind(m.C)), size(50,50)), fillColor(m.C), rounded(10,10), lineStyle([1,1,1,1,1,1]), size(150,150))
     	  					];
    		Edges edges1 = [edge(0,1, lineColor("orange")), edge(1,2, lineWidth(3), gap(20,20), lineOpacity(0.3)), edge(2,0, lineStyle([4,2,4,2]))];  
	
			return graph(nodes1, edges1, size(500,500));
		});
}










