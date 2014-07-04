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

void box0(){
	ex("box0", box());
} 

void box1(){
	ex("box1", box(fill="red", size=<100,100>));
}  

void box2(){
	ex("box2", box(fill="red", stroke="blue", strokeWidth=10, strokeDashArray= [10,20,10,10], size=<100,100>));
}

void box3(){
	ex("box3", box(fill="red", stroke="blue", strokeWidth=1, size=<100,100>, pos=<50,50>));
}

// Nested box

void box4(){
	ex("box4", box(fig=box(fill="white", size=<50,100>), fill="blue", size=<200,200>));
} 

void box5(){
	ex("box5", box(fill="blue", size=<200,200>, gap=<0,0>,
				   fig=box(fill="white", size=<50,100>, align=<left(), top()>)));
} 

void box6(){
	ex("box6", box(fig=box(fill="white", size=<50,100>, align=<right(), top()>), fill="blue", size=<200,200>, gap=<0,0>));
} 

void box7(){
	ex("box7", box(fig=box(fill="white", size=<50,100>, align=<right(), bottom()>), fill="blue", size=<200,200>, gap=<0,0>));
} 

void box8(){
	ex("box8", box(fig=box(fill="white", size=<50,100>, align=<left(), bottom()>), fill="blue", size=<200,200>, gap=<0,0>));
} 


void box9(){
	ex("box9", box(fig=box(fill="white", size=<50,100>, align=<left(), top()>), fill="blue", size=<200,200>, gap=<10,10>));
} 

void box10(){
	ex("box10", box(fig=box(fill="white", size=<50,100>, align=<right(), top()>), fill="blue", size=<200,200>, gap=<10,10>));
} 

void box11(){
	ex("box11", box(fig=box(fill="white", size=<50,100>, align=<right(), bottom()>), fill="blue", size=<200,200>, gap=<10,10>));
} 

void box12(){
	ex("box12", box(fig=box(fill="white", size=<50,100>, align=<left(), bottom()>), fill="blue", size=<200,200>, gap=<10,10>));
} 

void box13(){
	ex("box13", box(fig=box(fig=box(fill="red", size=<20,20>), fill="white", size=<50,100>, align=<left(), bottom()>), fill="blue", size=<200,200>, gap=<10,10>));
} 

void box14(){
	ex("box14", box(fig=box(fig=box(fill="red", size=<20,20>, align=<left(), top()>), fill="white", size=<50,100>, align=<left(), bottom()>), fill="blue", size=<200,200>, gap=<10,10>));
}

void box15(){
	ex("box15", box(fig=box(fig=box(fill="red", size=<20,20>, align=<right(), top()>), fill="white", size=<50,100>, align=<left(), bottom()>), fill="blue", size=<200,200>, gap=<10,10>));
}

void box16(){
	ex("box16", box(fig=box(fig=box(fill="red", size=<20,20>, align=<right(), bottom()>), fill="white", size=<50,100>, align=<left(), bottom()>), fill="blue", size=<200,200>, gap=<10,10>));
}

// hcat  
        
void hcat1(){
	ex("hcat1", hcat(figs=[box(fill="red",size=<50,100>), box(fill="green", size=<200,200>), box(fill="blue", size=<10,10>)], align=<right(), top()>));
}

void hcat2(){
	ex("hcat2", hcat(figs=[box(fill="red",size=<50,100>), box(fill="green", size=<200,200>), box(fill="blue", size=<10,10>)], align=<right(), vcenter()>));
}

void hcat3(){
	ex("hcat3", hcat(figs=[box(fill="red",size=<50,100>), box(fill="green", size=<200,200>), box(fill="blue", size=<10,10>)], align=<right(), bottom()>));
}

void hcat4(){
	ex("hcat4", hcat(figs=[box(fill="red",size=<50,100>), box(fill="green", size=<200,200>), box(fill="blue", size=<10,10>)], align=<right(), bottom()>, gap=<10,10>));
}

void hcat5(){
	ex("hcat5", hcat(figs=[box(size=<2,2>,pos=<x, y>) | <x, y> <- [<0,0>, <10,10>, <20,20>]]));
}

void hcat6(){
	ex("hcat6", hcat(figs=[box(size=<2,2>,pos=<toInt(x * 2), toInt(100 * sin(x))>) | real x <- [0.0, 0.1 .. 10.0]]));
} 

// hcat in box

void box_hcat1(){
	ex("box_hcat1", box(
					fig=hcat(figs=[ box(fill="red",size=<50,100>), 
						   box(fill="green", size=<200,200>), 
						   box(fill="blue", size=<10,10>)
						 ], align=<left(), top()>, gap=<10,10>),
					fill="grey"));
}

void box_hcat2(){
	ex("box_hcat2", box(
					fig=hcat(figs=[ box(fill="red",size=<50,100>), 
						   box(fill="green", size=<200,200>), 
						   box(fill="blue", size=<10,10>)
						 ], align=<left(), top()>, gap=<10,10>),
					size=<400,400>, align=<left(), top()>, fill="grey"));
}

void box_hcat3(){
	ex("box_hcat3", box(
					fig=hcat(figs=[ box(fill="red",size=<50,100>), 
						   box(fill="green", size=<200,200>), 
						   box(fill="blue", size=<10,10>)
						 ], align=<left(), top()>, gap=<10,10>),
					size=<400,400>, align=<right(), top()>, fill="grey"));
}

void box_hcat4(){
	ex("box_hcat4", box(
					fig=hcat(figs=[ box(fill="red",size=<50,100>), 
						   box(fill="green", size=<200,200>), 
						   box(fill="blue", size=<10,10>)
						 ], align=<left(), top()>, gap=<10,10>),
					size=<400,400>, align=<right(), bottom()>, fill="grey"));
}

void box_hcat5(){
	ex("box_hcat5", box(
					fig=hcat(figs=[ box(fill="red",size=<50,100>), 
						   box(fill="green", size=<200,200>), 
						   box(fill="blue", size=<10,10>)
						 ], align=<left(), top()>, gap=<10,10>),
					size=<400,400>, align=<left(), bottom()>, fill="grey"));
}

void box_hcat6(){
	ex("box_hcat6", box(
					fig=hcat(figs=[ box(fill="red",size=<50,100>), 
						   box(fill="green", size=<200,200>), 
						   box(fill="blue", size=<10,10>)
						 ], align=<hcenter(), vcenter()>, gap=<10,10>),
					size=<400,400>, align=<left(), top()>, fill="grey"));
}

void box_hcat7(){
	ex("box_hcat7", box(
					fig=hcat(figs=[ box(fill="red",size=<50,100>), 
						   box(fill="green", size=<200,200>), 
						   box(fill="blue", size=<10,10>)
						 ], align=<hcenter(), vcenter()>, gap=<10,10>),
					size=<400,400>, align=<right(), top()>, fill="grey"));
}

void box_hcat8(){
	ex("box_hcat8", box(
					fig=hcat(figs=[ box(fill="red",size=<50,100>), 
						   box(fill="green", size=<200,200>), 
						   box(fill="blue", size=<10,10>)
						 ], align=<hcenter(), vcenter()>, gap=<10,10>),
					size=<400,400>, align=<right(), bottom()>, fill="grey"));
}

void box_hcat9(){
	ex("box_hcat9", box(
					fig=hcat(figs=[ box(fill="red",size=<50,100>), 
						   box(fill="green", size=<200,200>), 
						   box(fill="blue", size=<10,10>)
						 ], align=<hcenter(), vcenter()>, gap=<10,10>),
					size=<400,400>, align=<left(), bottom()>, fill="grey"));
}


// vcat

void vcat1(){
	ex("vcat1", vcat(figs=[box(fill="red",size=<50,100>), box(fill="green", size=<200,200>), box(fill="blue", size=<10,10>)], align=<left(), top()>));
}

void vcat2(){
	ex("vcat2", vcat(figs=[box(fill="red",size=<50,100>), box(fill="green", size=<200,200>), box(fill="blue", size=<10,10>)], align=<hcenter(), top()>));
}

void vcat3(){
	ex("vcat3", vcat(figs=[box(fill="red",size=<50,100>), box(fill="green", size=<200,200>), box(fill="blue", size=<10,10>)], align=<right(), top()>));
}

void vcat4(){
	ex("vcat4", vcat(figs=[box(fill="red",size=<50,100>), box(fill="green", size=<200,200>), box(fill="blue", size=<10,10>)], align=<right(), top()>, gap=<10,10>));
}

void vcat5(){
	ex("vcat5", vcat(figs=[box(fill="red",size=<100,100>), box(fill="green", size=<200,200>)], align=<left(), top()>));
}

list[num] DATA1 = [ 11, 12, 15, 20, 18, 17, 16, 18, 23, 25 ];

void barchart1(){
	ex("barchart1", barchart(fill="black", size=<400,300>, dataset=DATA1));
}

void barchart2(){
	ex("barchart2", hcat(figs=[barchart(fill="blue", size=<400,300>, dataset=DATA1), 
					 box(fill="red",size=<100,100>), 
					 barchart(fill="black", size=<200,300>, dataset=DATA1),
					 box(fill="yellow",size=<50,50>)
					], align=<right(), bottom()>));
}

lrel[num,num] DATA2 = [<5, 20>, <480, 90>, <250, 50>, <100, 33>, <330, 95>,<410, 12>, <475, 44>, <25, 67>, <85, 21>, <220, 88>, <600, 150>];

void scatterplot1(){
	ex("scatterplot1", scatterplot(fill="black", size=<400,300>, dataset=DATA2));
}

void scatterplot2(){
	ex("scatterplot2", hcat(figs=[scatterplot(fill="blue", size=<400,300>, dataset=DATA2), 
					 //box(fill="red",size=<100,100>), 
					 scatterplot(fill="black", size=<400,300>, dataset=DATA2)
					 //box(fill="yellow",size=<50,50>)
					], align=<right(), bottom()>));
}

void scatterplot3(){
	ex("scatterplot3", hcat(figs=[scatterplot(fill="blue", size=<400,300>, dataset=DATA2), 
					 //box(fill="red",size=<100,100>), 
					 barchart(fill="black", size=<400,300>, dataset=DATA1)
					 //box(fill="yellow",size=<50,50>)
					], align=<left(), vcenter()>));
}

void linechart1(){
	ex("linechart1", lineChart(fill="blue", size=<400,300>));
}


map[str,Figure] nodes1 = 
			     ( "N0" :	box(fill="yellow", rounded=<1,1>, strokeWidth=3),
          		   "N1" :   box(fill="red", strokeDashArray=[1,1,1,1,1,1]),
     	    	   "N2" :	box(fill="lightblue", rounded=<15,15>)
     	  		);
list[Figure] edges1 = [ edge("N0","N1", "N0-N1", stroke="orange"), 
						edge("N1","N2", "N1-N2", strokeWidth=3, strokeOpacity=0.3), 
						edge("N2","N0", "N2-N0", strokeDashArray=[4,2,4,2]),
						edge("N0","N2", "N0-N2", strokeDashArray=[4,2,4,2])
					  ];        

void graph1(){
	ex("graph1", graph(nodes=nodes1, edges=edges1, size=<250,250>));
}


void graph2(){
	ex("graph2", hcat(figs=[graph(nodes=nodes1, edges=edges1, size=<250,250>),
					 scatterplot(fill="blue", size=<400,300>, dataset=DATA2),
					 barchart(fill="black", size=<400,300>, dataset=DATA1)
					], gap=<50,50>));
}
public void graph3(){
     nodes =
        ("A": box(size=<20,20>, fill="green"),
     	 "B": box(size=<20,20>, fill="red"),
     	 "C": box(size=<20,20>, fill="blue"),
     	 "D": box( size=<20,20>, fill="purple"),
     	 "E": box(size=<20,20>, fill="lightblue"),
     	 "F": box(size=<20,20>, fill="orange")
     	);
     	
    edges = 
    	[ edge("A", "B", ""),
    	  edge("B", "C", ""),
    	  edge("C", "D", ""),
    	  edge("D", "E", ""),
    	  edge("E", "F", ""),
    	  edge("F", "A", "")
    	];
    	    
    render("graph3", graph(nodes=nodes, edges=edges, size=<400,400>,gap=<40,40>));
}

public void graph4(){

	b = box(fill="whitesmoke");
    states = ( 	"CLOSED": box(fill="#f77"), 
    			"LISTEN": b,
    			"SYN RCVD" : b,
				"SYN SENT": b,
                "ESTAB":	 box(fill="#7f7"),
                "FINWAIT-1" : b,
                "CLOSE WAIT": b,
                "FINWAIT-2": b,
                   
                "CLOSING": b,
                "LAST-ACK": b,
                "TIME WAIT": b
                );
 	
    edges = [	edge("CLOSED", 		"LISTEN",  	 "open"),
    			edge("LISTEN",		"SYN RCVD",  "rcv SYN"),
    			edge("LISTEN",		"SYN SENT",  "send"),
    			edge("LISTEN",		"CLOSED",    "close"),
    			edge("SYN RCVD", 	"FINWAIT-1", "close"),
    			edge("SYN RCVD", 	"ESTAB",     "rcv ACK of SYN"),
    			edge("SYN SENT",   	"SYN RCVD",  "rcv SYN"),
   				edge("SYN SENT",   	"ESTAB",     "rcv SYN, ACK"),
    			edge("SYN SENT",   	"CLOSED",    "close"),
    			edge("ESTAB", 		"FINWAIT-1", "close"),
    			edge("ESTAB", 		"CLOSE WAIT", "rcv FIN"),
    			edge("FINWAIT-1",  	"FINWAIT-2",  "rcv ACK of FIN"),
    			edge("FINWAIT-1",  	"CLOSING",    "rcv FIN"),
    			edge("CLOSE WAIT", 	"LAST-ACK",  "close"),
    			edge("FINWAIT-2",  	"TIME WAIT",  "rcv FIN"),
    			edge("CLOSING",    	"TIME WAIT",  "rcv ACK of FIN"),
    			edge("LAST-ACK",   	"CLOSED",     "rcv ACK of FIN"),
    			edge("TIME WAIT",  	"CLOSED",     "timeout=2MSL")
  			];
  			
  			 render("graph4", graph(nodes=states, edges=edges, size=<900,900>,gap=<40,40>,fill="white"));
}

//void ex72(){
//	ex("ex72", hcat(figs=[graph(nodes1, edges1, size(400,400)),
//					 scatterplot(fill="blue", size=<400,300>, dataset=DATA2),
//					 texteditor(size=<200,200>),
//					 barchart(fill="black", size=<400,300>, dataset=DATA1)
//					], gap(50,50)));
//}
//
void text1(){
	ex("text1", text("Hello", fontFamily="sans-serif", fill="black", fontWeight="bold", fontStyle="italic", fontSize=20));
}

void text2(){
	ex("text2", box(fig=text("Hello", fill="black", fontSize=20), fill="white"));
}

void text3(){
	ex("text3", hcat(figs=[ box(fig=text("Hello", fill="black"), fill="white"),
					  text("World")
					], fontSize=20));
}

/************** Interaction *****************/

data COUNTER = COUNTER(int counter);

void counter1(){
	
	render("counter1",  #COUNTER, COUNTER(666), Figure (COUNTER m) {
			return
				vcat(figs=[ box(fig=text("Click me", event=on("click", bind(m.counter, m.counter + 1)), fontSize=20, gap=<2,2>), fill="whitesmoke"),
					   text(m.counter, size=<150,50>,fontSize=30)
				     ]);
			});
}

void counter2(){
	
	render("counter2",  #COUNTER, COUNTER(666),  Figure (COUNTER m) {
			return
				vcat(figs=[ box(fig=text("Click me 1", event=on("click", bind(m.counter, m.counter + 1)), fontSize=20, gap=<2,2>), fill="whitesmoke"),
					   text(m.counter, size=<150,50>,fontSize=30),
					   box(fig=text("Click me 2", event=on("click", bind(m.counter, m.counter + 1)), fontSize=20, gap=<2,2>), fill="whitesmoke"),
					   text(m.counter, size=<150,50>, fontSize=50),
					   text(m.counter, size=<150,50>, fontSize=80)
				     ]);
			});
}

void counter3(){
	
	render("counter3",  #COUNTER, COUNTER(666), Figure (COUNTER m) {
			return
				vcat(figs=[ buttonInput(trueText="Click me", falseText="Click me", event=on("click", bind(m.counter, m.counter + 1)), size=<80,40>),
					   text(m.counter, size=<150,50>,fontSize=30)
				     ]);
			});
}

void counter4(){
	
	render("counter4",  #COUNTER, COUNTER(666), Figure (COUNTER m) {
			return
				vcat(figs=[ buttonInput( trueText="Click me", falseText="Click me", event=on("click", bind(m.counter, m.counter + 1)), size=<80,40>),
					   text(m.counter, size=<150,50>,fontSize=30),
					   box(size=<50,50>, stroke="white"),
					   buttonInput( trueText="Click me", falseText="Click me", event=on("click", bind(m.counter, m.counter + 1)), size=<100,40>),
					   text(m.counter, size=<150,50>, fontSize=50),
					   text(m.counter, size=<150,50>, fontSize=80)
				     ]);
			});
}

data ECHO = ECHO(str TXT);

void echo1(){
	render("echo1", #ECHO, ECHO("abc"), Figure (ECHO m) {
			return
				hcat(figs=[ 		
	                   strInput(event=on("submit", bind(m.TXT)), size=<100,25>), 
	                   text(m.TXT, size=<150,50>, fontSize=50),
	                   text(m.TXT, size=<150,50>, fontSize=80)
	                  
				   ], gap=<20,20>);
			});
}

data BORDER = BORDER(str C);

void border1(){
	render("border1", #BORDER, BORDER("red"), Figure (BORDER m) {
			return
				hcat(figs=[ text("Enter:", size=<150,50>, fontSize=18), 
				
	                   strInput(event=on("submit", bind(m.C)), size=<100,25>), 
	                   
	                   box(stroke=m.C, strokeWidth=10, size=<100,100>),
	                   
	                   box(stroke=m.C, strokeWidth=10, size=<100,100>)
				   ], gap=<20,20>);
			  });
}

void border2(){
	render("border2", #BORDER, BORDER("red"), Figure (BORDER m) {
			return
				hcat(figs=[ text("Enter:", size=<150,50>, fontSize=18), 
				
	                   colorInput(event=on("change", bind(m.C)), size=<100,25>), 
	                   
	                   box(stroke=m.C, strokeWidth=10, size=<100,100>),
	                   
	                   box(stroke=m.C, strokeWidth=10, size=<100,100>)
				   ], gap=<20,20>);
			  });
}

data CONTROL = CONTROL(str FC, int LW, int WIDTH , int HEIGHT);

void control1(){
	render("control1", #CONTROL, CONTROL("red",1,100,100), Figure (CONTROL m) {
			return
				vcat(figs=[
					hcat(figs=[ text("  fill:", size=<150,50>, fontSize=20), colorInput(event=on("submit", bind(m.FC)), size=<100,25>),
				
					       text("strokeWidth:", size=<150,50>, fontSize=20), numInput(event=on("submit", bind(m.LW)), size=<80,25>),
					
					       text("     width:", size=<150,50>, fontSize=20), numInput(event=on("submit", bind(m.WIDTH)), size=<100,25>),
					
					       text("    height:", size=<150,50>, fontSize=20), numInput(event=on("submit", bind(m.HEIGHT)), size=<100,25>)
					     ]),
					
					box(size=<100,100>, strokeWidth=0),
					
	                box(fill=m.FC, strokeWidth=m.LW, width=m.WIDTH, height=m.HEIGHT)
	                   
				   ], gap=<30,30>);
			  });
}

data CHOICE = CHOICE(int SEL);

void choice1(){
	render("choice1", #CHOICE, CHOICE(0),  Figure (CHOICE m) {
			return
			hcat(figs=[ text("Enter:", size=<150,50>, fontSize=18), 
			
	               numInput(event=on("change", bind(m.SEL)), size=<100,25>),
	               
				   choice(selection=m.SEL, 
				   		   figs = [ box(fill="red", size=<100,100>),
								      box(fill="white", size=<100,100>),
								      box(fill="blue", size=<100,100>)
								    ])],
					gap=<30,30>);
					});
}

void choice2(){
	render("choice2", #CHOICE, CHOICE(0),  Figure (CHOICE m) {
			return
			hcat(figs=[ text("Enter:", size=<150,50>, fontSize=18), 
			
	               choiceInput(choices=["red", "white", "blue"], event=on("change", bind(m.SEL)), size=<100,25>),
	               
				   choice(selection=m.SEL, 
				   		  figs = [ box(fill="red", size=<100,100>),
								     box(fill="white", size=<100,100>),
								     box(fill="blue", size=<100,100>)
								   ])],
					gap=<30,30>);
					});
}

data SLIDER = SLIDER(int SLIDER);

void slider1(){
	render("slider1", #SLIDER, SLIDER(50), Figure (SLIDER m) {
			return
			vcat(figs=[ hcat(figs=[text("0"), rangeInput(low=0,high=100,step=5, event=on("change", bind(m.SLIDER)), size=<150,50>), text("100")]),
			
				   text(m.SLIDER, size=<150,50>,fontSize=30)
	             ],			  
				 gap=<10,20>);
				 });
}

data DIM = DIM(int WIDTH, int HEIGHT);

void slider2(){

	render("slider2", #DIM, DIM(50,50), Figure (DIM m) {
			return vcat(figs=[ hcat(figs=[text("WIDTH"), text("0"), rangeInput(low=0,high=100,step=5, event=on("change", bind(m.WIDTH)), size=<150,50>), text("100")]),
			       hcat(figs=[text("HEIGHT"), text("0"), rangeInput(low=0,high=100,step=5, event=on("change", bind(m.HEIGHT)), size=<150,50>), text("100")]),
			
				   box(width=m.WIDTH, height=m.HEIGHT, fill="pink")
	             ],			  
				 gap=<10,20>);
		});
}

void slider3(){
	render("slider3", #SLIDER, SLIDER(25), Figure (SLIDER m) {
			return 
			vcat(figs=[ rangeInput(low=0, high=50, step=5, event=on("change", bind(m.SLIDER)), size=<200,50>),
				   box(size=<50,50>, strokeWidth=0),
				   box(strokeWidth=m.SLIDER, size=<150,50>, fill="red")
	             ], align=<left(), top()>,		  
				 gap=<80,80>);
				 });
}

// Resize scatterplot

data SIZE = SIZE(int SIZE);

void slider3(){

	render("slider3", #SIZE, SIZE(300), Figure (SIZE m) {
			low = 100;
			high = 500;
			return vcat(figs=[ hcat(figs=[text("SIZE"), text(low), rangeInput(low=low,high=high,step=5, event=on("change", bind(m.SIZE)), size=<500,50>), text(high) ]),
				   
				    scatterplot(fill="black", width=m.SIZE, height=m.SIZE, dataset=DATA2)
	             ],			  
				 gap=<10,20>);
		});
}


data VISABLE = VISABLE(bool VISABLE);

void visible1(){
	render("visible1", #VISABLE, VISABLE(true), Figure (VISABLE m) {
			return 
			vcat(figs=[ buttonInput( trueText="hide", falseText="show", event=on("click", bind(m.VISABLE)),size=<50,50>),
				   
				   visible(condition=m.VISABLE,  fig=box(size=<150,50>, fill="red"))
	             ], align=<left(), top()>,		  
				 gap=<30,30>);
				 });
}

void visible2(){
	render("visible2", #VISABLE, VISABLE(true), Figure (VISABLE m) {
			return 
			vcat(figs=[ checkboxInput(event=on("click", bind(m.VISABLE)), size=<50,50>),
				   
				   visible(condition=m.VISABLE,  fig=box(size=<150,50>, fill="red"))
	             ], align=<left(), top()>,		  
				 gap=<30,30>);
				 });
}

// Tooltip

data EMPTY = EMPTY();

void tooltip1(){

	render("tooltip1", #EMPTY, EMPTY(), Figure (EMPTY m) {
			low = 100;
			high = 500;
			
			Event tooltip(str txt) = on("mouseover", box(fig=text(txt, fontSize=12, stroke="black"), fill="yellow"));
			
			return 
				vcat(figs=[ box(size=<200,50>, stroke="white"),
					   hcat(figs=[ box(fill="red", width=100, height=100, event=tooltip("I am a red box")),
					   			   box(fill="white", width=100, height=100),
						           box(fill="blue", width=100, height=100, event=tooltip("I am a blue box"))
	                        ])
	                  ],		  
				 gap=<10,20>);
		});
}


data COLOR1 = COLOR1(str C);

void boxcolor1(){
          
	render("boxcolor1", #COLOR1, COLOR1("white"), Figure (COLOR1 m) {
			return box(fig=colorInput(event=on("change", bind(m.C)), size=<50,20>, fill=m.C, rounded=<10,10>, gap=<20,20>,strokeDashArray=[1,1,1,1,1,1]));
		});
}

data COLOR2 = COLOR2(str C1, str C2);

void boxcolor2(){
          
	render("boxcolor2", #COLOR2, COLOR2("white", "blue"), Figure (COLOR2 m) {
			return hcat(figs=[ box(fig=colorInput(event=on("change", bind(m.C1)), size=<50,20>, fill=m.C1, rounded=<10,10>, gap=<20,20>,strokeDashArray=[1,1,1,1,1,1])),
						       box(fig=colorInput(event=on("change", bind(m.C2)), size=<50,20>, fill=m.C2, rounded=<10,10>, gap=<20,20>,strokeDashArray=[1,1,1,1,1,1]))
						     ], gap=<20,30>);
		});
}

//data M219 = m219(str C);
//
//void ex219(){
//          
//	render("ex219", #M219, m219("blue"), Figure (M219 m) {
//			Figures nodes1 = [  /* 0 */	hcat(figs=[box(fill="green", size=<50,50>),box(fill="yellow", size=<50,50>), box(fill="gray"), size=<50,50>)]),
//          		       			/* 1 */	box(fill="red", fillOpacity=0.4, size=<100,100>),
//     	    	      			/* 2 */	box(colorInput(event=on("change", bind(m.C)), size=<50,50>), fill=m.C, rounded=<10,10>,strokeDashArray=[1,1,1,1,1,1], size=<150,150>)
//     	  					];
//    		Edges edges1 = [edge(0,1, stroke="orange"), edge(1,2, strokeWidth=3, gap=<20,20>, strokeOpacity=0.3), edge(2,0, strokeDashArray=[4,24,2])];  
//	
//			return graph(nodes1, edges1, size=<500,500>);
//		});
//}










