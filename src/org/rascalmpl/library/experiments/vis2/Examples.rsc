module experiments::vis2::Examples

import experiments::vis2::Figure;
import experiments::vis2::Properties;
import experiments::vis2::Translate;
import util::Math;
import IO;
import util::HtmlDisplay;

// ********************** Examples **********************

void ex(str title, Figure f){
	<w, h> = sizeOf(f);
	s = trPrims(title, w, h, trFig(f));
	println(s);
	htmlDisplay(|file:///tmp/<title>.html|, s);
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
	ex("ex02", box(fillColor("red"), lineColor("blue"), lineWidth(1), size(100,100), pos(50,50)));
}     

// Nested box

void ex10(){
	ex("ex10", box(box(fillColor("red"), size(50,100)), fillColor("blue"), size(200,200), gap(10,10)));
} 

void ex11(){
	ex("ex11", box(box(fillColor("red"), size(50,100), align(left(), top())), fillColor("blue"), size(200,200), gap(0,0)));
} 

void ex12(){
	ex("ex12", box(box(fillColor("red"), size(50,100), align(right(), top())), fillColor("blue"), size(200,200), gap(0,0)));
} 

void ex13(){
	ex("ex13", box(box(fillColor("red"), size(50,100), align(right(), bottom())), fillColor("blue"), size(200,200), gap(0,0)));
} 

void ex14(){
	ex("ex14", box(box(fillColor("red"), size(50,100), align(left(), bottom())), fillColor("blue"), size(200,200), gap(0,0)));
} 


void ex15(){
	ex("ex15", box(box(fillColor("red"), size(50,100), align(left(), top())), fillColor("blue"), size(200,200), gap(10,10)));
} 

void ex16(){
	ex("ex16", box(box(fillColor("red"), size(50,100), align(right(), top())), fillColor("black"), size(200,200), gap(10,10)));
} 

void ex17(){
	ex("ex17", box(box(fillColor("red"), size(50,100), align(right(), bottom())), fillColor("blue"), size(200,200), gap(10,10)));
} 

void ex18(){
	ex("ex18", box(box(fillColor("red"), size(50,100), align(left(), bottom())), fillColor("blue"), size(200,200), gap(10,10)));
} 

void ex19(){
	ex("ex19", box(box(box(fillColor("green"), size(20,20)), fillColor("red"), size(50,100), align(left(), bottom())), fillColor("blue"), size(200,200), gap(10,10)));
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

/*
t1 = tree(box(fillColor("green")),
          [ box(fillColor("red")),
     	    box(fillColor("blue"))
     	  ],
          std(size(50)), std(gap(20))
    	);
*/
