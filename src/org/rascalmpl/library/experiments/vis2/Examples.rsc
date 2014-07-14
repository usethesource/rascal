module experiments::vis2::Examples

import experiments::vis2::Figure;
import experiments::vis2::FigureServer; 

import String;
import List;
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
	ex("box1", box(fillColor="red", size=<100,100>));
}  

void box2(){
	ex("box2", box(fillColor="red", size=<100,100>, lineWidth=10));
} 

void box3(){
	ex("box3", box(fillColor="red", lineColor="blue", lineWidth=10, lineDashing= [10,20,10,10], size=<100,100>));
}

// Nested box
Figure WB = box(fillColor="white", size=<50,100>);

Figure RB = box(fillColor="red", size=<20,20>);

void box4(){
	ex("box4", box(fig=WB, fillColor="blue", size=<200,200>));
} 

void box5(){
	ex("box5", box(fillColor="blue", size=<200,200>, align=topLeft,
				   fig=WB));
} 

void box6(){
	ex("box6", box(fillColor="blue", size=<200,200>, align=topRight,
				   fig=WB));
} 

void box7(){
	ex("box7", box(fillColor="blue", size=<200,200>, align=bottomRight,
				   fig=WB));
} 

void box8(){
	ex("box8", box(fillColor="blue", size=<200,200>, align=bottomLeft,
				   fig=WB));
} 


void box9(){
	ex("box9", box(fillColor="blue", size=<200,200>, gap=<10,10>, align=topLeft,
				   fig=WB));
} 

void box10(){
	ex("box10", box(fig=WB, fillColor="blue", size=<200,200>, gap=<10,10>, align=topRight));
} 

void box11(){
	ex("box11", box(fig=WB, fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomRight));
} 

void box12(){
	ex("box12", box(fig=WB, fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft));
} 

void box13(){
	ex("box13", box(fig=box(fig=RB, fillColor="white", size=<50,100>), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft));
} 

void box14(){
	ex("box14", box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=topLeft), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft));
}

void box15(){
	ex("box15", box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=topRight), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft));
}

void box16(){
	ex("box16", box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=bottomRight), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft));
}

void box17(){
	ex("box17", box(fig=RB, fillColor="blue", grow=3));
}

void box18(){
	ex("box18", box(fig=text("Hello", fontSize=20), grow=2));
}

// hcat  

Figures rgbFigs = [box(fillColor="red",size=<50,100>), box(fillColor="green", size=<200,200>), box(fillColor="blue", size=<10,10>)];
        
void hcat1(){
	ex("hcat1", hcat(figs=rgbFigs, align=top));
}

void hcat2(){
	ex("hcat2", hcat(figs=rgbFigs, align=center));
}

void hcat3(){
	ex("hcat3", hcat(figs=rgbFigs, align=bottom));
}

void hcat4(){
	ex("hcat4", hcat(figs=rgbFigs, align=bottom, gap=<10,10>));
}



// hcat in box

Figure tlFigs = hcat(align=topLeft, gap=<10,10>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);
				             
Figure ctFigs = hcat(align=center, gap=<10,10>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);

void box_hcat1(){
	ex("box_hcat1", box(fig=tlFigs, fillColor="grey"));
}

void box_hcat2(){
	ex("box_hcat2", box(fig=tlFigs, fillColor="grey", size=<400,400>, align=topLeft));
}

void box_hcat3(){
	ex("box_hcat3", box(fig=tlFigs, fillColor="grey", size=<400,400>, align=topRight));
}

void box_hcat4(){
	ex("box_hcat4", box(fig=tlFigs,fillColor="grey", size=<400,400>, align=bottomRight));
}

void box_hcat5(){
	ex("box_hcat5", box(fig=tlFigs, fillColor="grey", size=<400,400>, align=bottomLeft));
}

void box_hcat6(){
	ex("box_hcat6", box(fig=tlFigs, fillColor="grey", size=<400,400>, align=topLeft));
}

void box_hcat7(){
	ex("box_hcat7", box(fig=ctFigs, fillColor="grey", size=<400,400>, align=topRight));
}

void box_hcat8(){
	ex("box_hcat8", box(fig=ctFigs, fillColor="grey", size=<400,400>, align=bottomRight));
}

void box_hcat9(){
	ex("box_hcat9", box(fig=ctFigs, fillColor="grey", size=<400,400>, align=bottomLeft));
}

// hcat flex

void hflex1(){
	ex("hflex1", hcat(size = <200,200>,
					  figs = [ box(fillColor="red"), 
				               box(fillColor="green", size=<100,100>), 
				               box(fillColor="blue", size=<50,50>)
				             ]));
}

void hflex2(){
	ex("hflex2", hcat(size = <200,200>,
					  figs = [ box(fillColor="red", height=10), 
				               box(fillColor="green", size=<100,100>), 
				               box(fillColor="blue", size=<50,50>)
				             ]));
}

void hflex3(){
	ex("hflex3", hcat(size = <200,200>,
					  figs = [ box(fillColor="red", width=10), 
				               box(fillColor="green", size=<100,100>), 
				               box(fillColor="blue", size=<50,50>)
				             ]));
}	

void hflex4(){
	ex("hflex4", hcat(size = <200,200>,
					  figs = [ box(fillColor="red"), 
				               box(fillColor="green", size=<100,100>), 
				               box(fillColor="blue")
				             ]));
}
void hflex5(){
	ex("hflex5", hcat(size = <400,400>,
					  figs = [ box(fillColor="red"), 
				               box(fillColor="green", size=<100,100>), 
				               box(fillColor="blue")
				             ]));
}						               
				               

// vcat

void vcat1(){
	ex("vcat1", vcat(figs=rgbFigs, align=topLeft));
}

void vcat2(){
	ex("vcat2", vcat(figs=rgbFigs, align=top));
}

void vcat3(){
	ex("vcat3", vcat(figs=rgbFigs, align=topRight));
}

void vcat4(){
	ex("vcat4", vcat(figs=rgbFigs, align=topRight, gap=<10,10>));
}

// vcat flex

void vflex1(){
	ex("vflex1", vcat(size = <200,200>,
					  figs = [ box(fillColor="red"), 
				               box(fillColor="green", size=<100,100>), 
				               box(fillColor="blue", size=<50,50>)
				             ]));
}

void vflex2(){
	ex("vflex2", vcat(size = <200,200>,
					  figs = [ box(fillColor="red", height=10), 
				               box(fillColor="green", size=<100,100>), 
				               box(fillColor="blue", size=<50,50>)
				             ]));
}

void vflex3(){
	ex("vflex3", vcat(size = <200,200>,
					  figs = [ box(fillColor="red", width=10), 
				               box(fillColor="green", size=<100,100>), 
				               box(fillColor="blue", size=<50,50>)
				             ]));
}	

void vflex4(){
	ex("vflex4", vcat(size = <200,200>,
					  figs = [ box(fillColor="red"), 
				               box(fillColor="green", size=<100,100>), 
				               box(fillColor="blue")
				             ]));
}
void vflex5(){
	ex("vflex5", vcat(size = <400,400>,
					  figs = [ box(fillColor="red"), 
				               box(fillColor="green", size=<100,100>), 
				               box(fillColor="blue")
				             ]));
}	


// vcat in box

Figure vtlFigs = vcat(align=topLeft, gap=<10,10>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);
				             
Figure vctFigs = vcat(align=center, gap=<10,10>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);

void box_vcat1(){
	ex("box_vcat1", box(fig=vtlFigs, fillColor="grey"));
}

void box_vcat2(){
	ex("box_vcat2", box(fig=vtlFigs, size=<400,400>, align=topLeft, fillColor="grey"));
}

void box_vcat3(){
	ex("box_vcat3", box(fig=vtlFigs, size=<400,400>, align=topRight, fillColor="grey"));
}

void box_vcat4(){
	ex("box_vcat4", box(fig=vtlFigs, size=<400,400>, align=bottomRight, fillColor="grey"));
}

void box_vcat5(){
	ex("box_vcat5", box(fig=vtlFigs, size=<400,400>, align=bottomLeft, fillColor="grey"));
}

void box_vcat6(){
	ex("box_vcat6", box(fig=vctFigs, size=<400,400>, align=topLeft, fillColor="grey"));
}

void box_vcat7(){
	ex("box_vcat7", box(fig=vctFigs, size=<400,400>, align=topRight, fillColor="grey"));
}

void box_vcat8(){
	ex("box_vcat8", box(fig=vctFigs, size=<400,400>, align=bottomRight, fillColor="grey"));
}

void box_vcat9(){
	ex("box_vcat9", box(fig=vctFigs, size=<400,400>, align=bottomLeft, fillColor="grey"));
}

// hvcat flex

void hvflex1(){
	ex("hvflex1", hcat(size=<600,600>,
					   figs= [ vcat(width=200, height=300,
	                                figs= [ 
	                                       box(fillColor="red"), 
				                           box(fillColor="green", width=50), 
				                           box(fillColor="blue")
				                          ]),
				               vcat(//size = <400,400>,
					               figs = [ box(fillColor="yellow", height=50), 
				                            box(fillColor="purple"), 
				                            box(fillColor="orange")
				                          ])
				            ]));
}	

/********************** grid ******************************/

Figure RedBox = box(fillColor="red", size=<50,50>);
Figure BlueBox = box(fillColor="blue", size=<100,30>);
Figure GreenBox = box(fillColor="green", size=<40,60>);


void grid1(){
	ex("grid1", grid(figArray=[ [RedBox],
							    [BlueBox]
							  ], gap=<10,10>));
}

void grid2(){
	ex("grid2", grid(figArray=[ [RedBox, GreenBox],
							    [BlueBox, RedBox, RedBox]
							  ], gap=<10,10>));
}

void grid3(){
	ex("grid3", grid(figArray=[ [box(fillColor="red", size=<50,50>, align=topLeft), GreenBox],
							    [BlueBox, RedBox, RedBox]
							  ], gap=<10,10>));
}
void grid4(){
	ex("grid4", grid(figArray=[ [box(fillColor="red", size=<50,50>, align=bottomRight), GreenBox],
							    [BlueBox, RedBox, RedBox]
							  ], gap=<10,10>));
}

// grid flex

void gflex1(){
	ex("gflex1", grid(size=<600,600>,
					  figArray= [ [box(fillColor="red"),               box(fillColor="green", width=50), box(fillColor="blue")],
				                  [box(fillColor="yellow", height=50), box(fillColor="purple"),          box(fillColor="mediumspringgreen") ]
				                ]));
}

void gflex2(){
	ex("gflex2", grid(size=<600,600>,
					  figArray= [ [box(fillColor="red"),               box(fillColor="green", size=<50,50>), box(fillColor="blue")],
				                  [box(fillColor="yellow", height=50), box(fillColor="purple"),          box(fillColor="mediumspringgreen") ]
				                ]));
}

void gflex3(){
	ex("gflex3", grid(size=<600,600>,
					  figArray= [ [box(fillColor="red"),               box(fillColor="green", size=<50,50>, align=topRight), box(fillColor="blue")],
				                  [box(fillColor="yellow", size=<50,50>, align=bottomLeft), box(fillColor="purple"),          box(fillColor="mediumspringgreen") ]
				                ]));
}

/********************** overlay ******************************/

void overlay1(){
	ex("overlay1", overlay(align=center, figs= [box(fillColor="red", size=<50,50>), box(fillColor="green", size=<100,20>)]));
}

void overlay2(){
	ex("overlay2", overlay(align=topLeft, figs= [box(fillColor="red", size=<50,50>), box(fillColor="green", size=<100,20>)]));
}

void overlay3(){
	ex("overlay3", overlay(align=bottomRight, figs= [box(fillColor="red", size=<50,50>), box(fillColor="green", size=<100,20>)]));
}

//void overlay4(){
//	ex("overlay4", overlay(figs= [box(fillColor="red", size=<50,50>, align=topLeft), box(fillColor="green", size=<100,20>, align=bottomRight)]));
//}

void overlay5(){
	ex("overlay5", box(fig=overlay(align=topLeft, figs=[MOVE(x, y, box(size=<10,10>)) | <x, y> <- [<0,0>, <100,100>, <200,200>]])));
}

void overlay6(){
	ex("overlay6", box(fig=overlay(align=topLeft, figs= MOVE(0,100, box(size = <100,1>)) + [MOVE(toInt(x * 10), toInt(100 + 100 * sin(x)), box(size=<2,2>))| real x <- [0.0, 0.1 .. 10.0]])));
} 


/********************** move ******************************/

void at1(){
	ex("at1", at(100, 100, box(fillColor="red", size=<50,50>)));
}

void at2(){
	ex("at2", overlay(align=topLeft,
						figs= [at(100,100, box(fillColor="red", size=<50,50>)),
							   at(200,200, box(fillColor="green", size=<50,50>))
							  ]));
}

void at3(){
	ex("at3", box(fig=overlay(align=topLeft,
	                          figs= [at(100,100, box(fillColor="red", size=<50,50>)),
								     at(200,200, box(fillColor="green", size=<50,50>)),
								     at(0,0, box(fillColor="blue", size=<50,50>))
								])));
}

void at4(){

	ex("at4", hcat(figs= [
						box(fillColor="red", size=<50,50>),
								
						box(fig=overlay(align=topLeft,
						                figs= [at(200,200, box(fillColor="yellow", size=<50,50>)),
								               at(20, 0, box(fillColor="gray", size=<50,50>))
								]))
						]));
}

void at5(){

	ex("at5", hcat(figs= [
						overlay(align=topLeft,
						        figs= [at(100, 100, box(fillColor="red", size=<50,50>)),
							           at(200, 200, box(fillColor="green", size=<50,50>))
								]),
								
						box(fig=overlay(align=topLeft,
						                figs= [at(100, 100, box(fillColor="purple", size=<50,50>)),
								               at(200, 200, box(fillColor="yellow", size=<50,50>)),
								               at(0, 0, box(fillColor="gray", size=<50,50>))
								]))
						]));
}

void at6(){
	ex("at6", box(fig=overlay(align=topLeft,
	                          figs= [at(100, 100, box(fillColor="red", size=<50,50>)),
								       at(100, -50, box(fillColor="green", size=<50,50>)),
								       at(0, 0, box(fillColor="blue", size=<50,50>))
								])));
}

/********************** scale ******************************/

void scale1(){
	ex("scale1", SCALE(0.5, box(size=<200,300>)));
}

void scale2(){
	ex("scale2", SCALE(1, box(size=<200,300>)));
}

void scale3(){
	ex("scale3", SCALE(2, box(size=<200,300>)));
}

/********************** rotate ******************************/

void rotate1(){
	ex("rotate1", rotate(45, box(size=<200,300>)));
}

void rotate2(){
	ex("rotate2", box(fig=rotate(0, box(size=<200,300>))));
}
void rotate3(){
	ex("rotate3", box(fig=rotate(45, box(size=<200,300>))));
}

void rotate4(){
	ex("rotate4", box(fig=rotate(90, box(size=<200,300>))));
}

void rotate5(){
	ex("rotate5", box(fig=rotate(180, box(size=<200,300>))));
}

void rotate6(){
	ex("rotate6", box(fig=rotate(225, box(size=<200,300>))));
}
void rotate7(){
	ex("rotate7", box(fig=rotate(270, box(size=<200,300>))));
}

void rotate8(){
	ex("rotate8", box(fig=rotate(360, box(size=<200,300>))));
}

/********************** image ******************************/

void image1(){
	ex("image1", image(url=|file:///lib/favicon.ico|));
}

void image2(){
	ex("image2", image(url=|file:///lib/favicon.ico|, size=<80,80>));
}

void image3(){
	ex("image3", hcat(figs = [ image(url=|file:///lib/favicon.ico|, size=<50,50>),
							   image(url=|file:///lib/favicon.ico|, size=<100,100>)
							 ]));
}

void image4(){
	ex("image4", rotate(45, image(url=|file:///lib/favicon.ico|, size=<50,50>)));
}


/********************** polygon ******************************/

void polygon1(){
	ex("polygon1", polygon([line(100,100), line(100,200), line(200,200)]));
}

void polygon2(){
	ex("polygon2", polygon([line(100,100), line(100,200), line(200,200)], fillColor="red", lineWidth=4, lineDashing=[1,1,1,1,1,1]));
}

/********************** shape ******************************/

void shape1(){
	ex("shape1", shape([line(100,100), line(100,200), line(200,200)], shapeClosed=true));
}

void shape2(){
	ex("shape2", shape([line(30,100), line(100, 100), line(200,80)], shapeClosed=true));
}

void shape3(){
	ex("shape3", hcat(figs=[ shape([line(100,100), line(100, 200), line(200,200)], shapeClosed=true, fillColor="red"),
	
							 shape([line(100,100), line(100, 200), line(200,200)], shapeClosed=true, fillColor="blue")
							 ]));
}

void shape4(){
	ex("shape4", shape([line(0,0), line(50, 50), line(80,50), line(100,0) ], shapeClosed = true,  fillColor = "yellow"));
}

void shape5(){
	ex("shape5", shape([line(0,0), line(50, 50), line(80,50), line(100,0) ], shapeCurved=true, shapeClosed = true, fillColor = "yellow"));
}

// SVG Essentials, p95.

void fillRule1(){
	ex("fillRule1", grid(fillColor="yellow",
						figArray=[ [ shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(45, 15), line(45,45), line(15,45)],  // clockwise/clockwise
					                      shapeClosed=true, fillRule="nonzero", fillColor = "grey"),
					           
					                 shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(15,45), line(45,45), line(45, 15)], 	// clockwise/counter clockwise
					                       shapeClosed=true, fillRule="nonzero", fillColor = "grey")
					               ],
					               
					               [ shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(45, 15), line(45,45), line(15,45)],  // clockwise/clockwise
					                      shapeClosed=true, fillRule="evenodd", fillColor = "grey"),
					           
					                 shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(15,45), line(45,45), line(45, 15)], 	// clockwise/counter clockwise
					                       shapeClosed=true, fillRule="evenodd", fillColor = "grey")
					               ] ]));			           
					           
}


// http://www.soc.napier.ac.uk/~cs66/hilbert.html

Vertices hilbert(num x0, num y0, num xis, num xjs, num yis, num yjs, int n){
	/* x0 and y0 are the coordinates of the bottom left corner */
	/* xis & xjs are the i & j components of the unit x vector this frame */
	/* similarly yis and yjs */
	if (n<= 0){
   	return [line(x0+(xis+yis)/2, y0+(xjs+yjs)/2)];
	} else {
		return [ *hilbert(x0,             y0,             yis/2,  yjs/2,  xis/2,  xjs/2,  n-1),
   				 *hilbert(x0+xis/2,       y0+xjs/2,       xis/2,  xjs/2,  yis/2,  yjs/2,  n-1),
  				 *hilbert(x0+xis/2+yis/2, y0+xjs/2+yjs/2, xis/2,  xjs/2,  yis/2,  yjs/2,  n-1),
   				 *hilbert(x0+xis/2+yis,   y0+xjs/2+yjs,   -yis/2, -yjs/2, -xis/2, -xjs/2, n-1) ];
   	}
}

void hilbert1(){
	ex("hilbert1", shape(hilbert(0, 0, 300, 0, 0, 300, 5)));
}

void hilbert2(){
	ex("hilbert2", shape(hilbert(0, 0, 300, 0, 0, 300, 5), 
								startMarker=box(size=<10,10>,fillColor="red"),
								midMarker=box(size=<3,3>,fillColor="blue"),
								endMarker=box(size=<10,10>,fillColor="green")
					   ));
}

void hilbert3(){
	ex("hilbert3", hcat(,
						figs = [ box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 1))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 2))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 3))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 4))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 5)))
							 ]));
}


/********************** shape with markers ******************************/

void marker1(){
	ex("marker1", box(size=<300,300>, align=topLeft, fig=shape([move(100,100), line(200,200)], startMarker=box(size=<10,10>,fillColor="red"))));
}

void marker2(){
	ex("marker2", box(size=<300,300>, align=topLeft, fig=shape([move(100,100), line(100,200), line(200,200)], shapeClosed=true, startMarker=box(size=<10,10>,fillColor="red"))));
}

void marker3(){
	ex("marker3", box(size=<300,300>, align=topLeft, fig=shape([move(100,100), line(100,200), line(200,200)], 
												shapeClosed=true,
												startMarker=box(size=<10,10>,fillColor="red"),
												midMarker=box(size=<20,20>,fillColor="blue")
												)));
}

void marker4(){
	ex("marker4", box(size=<300,300>,  align=topLeft, fig=shape([move(100,100), line(150,30), line(200,100), line(150,150)],
												shapeClosed=true, shapeCurved=true,
												startMarker=box(size=<10,10>,fillColor="red"),
												midMarker=box(size=<20,20>,fillColor="blue")
												//endMarker=box(size=<20,20>,fillColor="yellow")
												)));
}

Figure arrow(int side, str color, bool rightDir=true) =
	rightDir ? shape([line(0,0), line(side,side), line(0, 2*side)], shapeClosed=true, fillColor=color)
			 : shape([line(side,0), line(0,side), line(side, 2*side)], shapeClosed=true, fillColor=color);

void arrow1(){
	ex("arrow1", box(size=<300,300>, align=topLeft, fig= shape([move(100,100), line(200,200)], endMarker=arrow(10, "red"))));
}

void arrow2(){
	ex("arrow2", box(size=<300,300>, align=topLeft, fig= shape([move(100,100), line(200,200)], startMarker = arrow(10, "green",rightDir=false), endMarker=arrow(10, "red"))));
}

void arrow3(){
	ex("arrow3", box(size=<300,300>, align=topLeft, fillColor="silver", fig= shape([line(100,100), line(200,150), line(100,200), line(250,250)], 
	                                            shapeCurved=true, fillColor="silver",startMarker = arrow(10, "green",rightDir=false), endMarker=arrow(10, "red"))));
}


/********************* barChart ******************************/

Dataset[LabeledData] exampleBarData() =
	("Cumulative Return": [	<"A Label" , -29.765957771107>,
          					<"B Label" , 0>,
       						<"C Label" , 32.807804682612>,
          					<"D Label" , 196.45946739256>,
     						<"E Label" , 0.19434030906893>,
     						<"F Label" , -98.079782601442>,
      						<"G Label" , -13.925743130903>,
      						<"H Label" , -5.1387322875705>
      					  ]);

void barChart1(){
	ex("barChart1", barChart(dataset=exampleBarData()));

}

void barChart2(){
	ex("barChart2", barChart(dataset=exampleBarData(), size=<600,600>));
}

void barChart3(){
	ex("barChart3", hcat(figs=[  box(fillColor="red",size=<100,100>), barChart(size=<400,300>, dataset=exampleBarData())]));
}

void vegaBarChart1(){
	ex("vegaBarChart1", barChart(size=<400,400>, dataset=exampleBarData(), flavor="vegaBarChart"));
}

void vegaBarChart2(){
	ex("vegaBarChart2", hcat(figs=[  box(fillColor="red",size=<100,100>), barChart(size=<400,300>, dataset=exampleBarData(), flavor="vegaBarChart")]));
}

/********************* lineChart ******************************/

Dataset[XYData] sinAndCos() =
	("Sine Wave":         xyData([<x, round(sin(x/10),0.01)>               | x <- [0.0, 1.0 .. 100.0]], color= "#ff7f0e"),
	 "Cosine Wave":       xyData([<x, round(0.5 * cos(x/10), 0.01)>        | x <- [0.0, 1.0 .. 100.0]], color= "#2ca02c"),
	 "Another sine wave": xyData([<x, round(0.25 * sin(x/10) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 100.0]], color= "#7777ff", area=true)
	);

void lineChart1(){
	ex("lineChart1", lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   yAxis=axis(label="Voltage (v)", tick=".02f"),	
							   dataset= sinAndCos()));
}

void lineChart2(){
	ex("lineChart2", lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   yAxis=axis(label="Voltage (v)", tick=".02f"),	
							   dataset= sinAndCos(), size=<600,600>));
}

void lineChart3(){
	ex("lineChart3", hcat(figs=[box(fillColor="yellow", size=<200,100>),
								lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   			  yAxis=axis(label="Voltage (v)", tick=".02f"),	
							   			  dataset= sinAndCos(), 
							   			  size=<400,400>)
	]));
}

void lineChart4(){
	ex("lineChart4", box(fillColor="whitesmoke", lineWidth=4, lineColor="blue",
					     fig=hcat(figs=[barChart(size=<400,300>, dataset=exampleBarData()),
								lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   			  yAxis=axis(label="Voltage (v)", tick=".02f"),	
							   			  dataset= sinAndCos(), 
							   			  size=<400,400>)
	])));
}

void lineChart5(){
	ex("lineChart5", lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   yAxis=axis(label="Voltage (v)", tick=".02f"),	
							   dataset= sinAndCos(), 
							   flavor="nvLineWithFocusChart",
							   size=<400,400>));
}

/********************* graph ******************************/

map[str,Figure] nodes1 = 
			     ( "N0" :	box(fillColor="yellow", rounded=<1,1>, lineWidth=3, size=<10,10>),
          		   "N1" :   box(fillColor="red", lineDashing=[1,1,1,1,1,1], size=<20,20>),
     	    	   "N2" :	box(fillColor="lightblue", rounded=<15,15>, size=<30,30>)
     	  		);
list[Figure] edges1 = [ edge("N0","N1", "N0-N1", lineColor="orange", lineWidth=4), 
						edge("N1","N2", "N1-N2", lineWidth=3, lineOpacity=0.3), 
						edge("N2","N0", "N2-N0", lineDashing=[4,2,4,2]),
						edge("N0","N2", "N0-N2", lineDashing=[4,2,4,2])
					  ];        

void graph1(){
	ex("graph1", graph(nodes=nodes1, edges=edges1));
}

void graph2(){
	ex("graph2", graph(nodes=nodes1, edges=edges1, flavor="springGraph", size=<200,200>));
}

void graph3(){
	ex("graph3", hcat(figs= [ graph(nodes=nodes1, edges=edges1),
							  graph(nodes=nodes1, edges=edges1, flavor="springGraph", size=<200,200>)
							 ]));
}

void graph4(){
	ex("graph4", hcat(figs=[ barChart(size=<400,300>, dataset=exampleBarData()),
						     graph(nodes=nodes1, edges=edges1),
					         lineChart(xAxis=axis(label="Time (s)",    tick=",r"), 
							   		   yAxis=axis(label="Voltage (v)", tick=".02f"),	
							   		   dataset= sinAndCos(), 
							   		   size=<400,400>)
					], gap=<50,50>));
}

map[str,Figure] nodes2 =
        ("A": box(size=<20,20>, fillColor="green"),
     	 "B": box(size=<20,20>, fillColor="red"),
     	 "C": box(size=<20,20>, fillColor="blue"),
     	 "D": box( size=<20,20>, fillColor="purple"),
     	 "E": box(size=<20,20>, fillColor="lightblue"),
     	 "F": box(size=<20,20>, fillColor="orange")
     	);
     	
list[Figure] edges2 = 
    	[ edge("A", "B", ""),
    	  edge("B", "C", ""),
    	  edge("C", "D", ""),
    	  edge("D", "E", ""),
    	  edge("E", "F", ""),
    	  edge("F", "A", "")
    	];
    	
public void graph5(){ 
    render("graph5", graph(nodes=nodes2, edges=edges2));
}

public void graph6(){ 
    render("graph6", graph(nodes=nodes2, edges=edges2,flavor="springGraph", size=<300,300>));
}

public void graph7(){ 
    render("graph7", hcat(figs=[graph(nodes=nodes2, edges=edges2),
    					   graph(nodes=nodes2, edges=edges2,flavor="springGraph", size=<300,300>)
    					  ]));
}

public void graph8(){

	b = box(fillColor="whitesmoke");
    states = ( 	"CLOSED": box(fillColor="#f77"), 
    			"LISTEN": b,
    			"SYN RCVD" : b,
				"SYN SENT": b,
                "ESTAB":	 box(fillColor="#7f7"),
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
  			
  	render("graph8", graph(nodes=states, edges=edges));
}

/************** text *****************/
void text1(){
	ex("text1", text("Hello", fontSize=20));
}

void text2(){
	ex("text2", text("Hello", fontStyle="italic", fontSize=20));
}

void text3(){
	ex("text3", text("Hello", fontWeight="bold", fontSize=20));
}

void text4(){
	ex("text4", text("Hello", fontWeight="bold", fontStyle="italic", fontSize=20));
}

void text5(){
	ex("text5", box(fig=text("Hello", fillColor="black", fontWeight="bold", fontStyle="italic", fillColor="black", fontSize=20), fillColor="yellow"));
}

void text6(){
	ex("text6", hcat(figs=[ box(fig=text("Hello", fillColor="black"), fillColor="white"),
					  text("World")
					], fontSize=20));
}

/************** Interaction *****************/

data COUNTER = COUNTER(int counter);

void counter1(){
	
	render("counter1",  #COUNTER, COUNTER(666), Figure (COUNTER m) {
			return
				vcat(figs=[ box(fig=text("Click me", event=on("click", bind(m.counter, m.counter + 1)), fontSize=20, gap=<2,2>), fillColor="whitesmoke"),
					   text(m.counter, size=<150,50>,fontSize=30)
				     ]);
			});
}

void counter2(){
	
	render("counter2",  #COUNTER, COUNTER(666),  Figure (COUNTER m) {
			return
				vcat(figs=[ box(fig=text("Click me 1", event=on("click", bind(m.counter, m.counter + 1)), fontSize=20, gap=<2,2>), fillColor="whitesmoke"),
					   text(m.counter, size=<150,50>,fontSize=30),
					   box(fig=text("Click me 2", event=on("click", bind(m.counter, m.counter + 1)), fontSize=20, gap=<2,2>), fillColor="whitesmoke"),
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
				hcat(figs=[ strInput(event=on("submit", bind(m.TXT))), 
	                        text(m.TXT, size=<150,50>, fontSize=50),
	                        text(m.TXT, size=<150,50>, fontSize=80)
				          ], gap=<20,20>);
			});
}

data ECHO2 = ECHO2(num NUM);

void echo2(){
	render("echo2", #ECHO2, ECHO2(0), Figure (ECHO2 m) {
			return
				hcat(figs=[ text("Enter number:", fontSize=18),
							numInput(event=on("input", bind(m.NUM))), 
	                        text(m.NUM, size=<150,50>, fontSize=50),
	                        text(m.NUM, size=<150,50>, fontSize=80)
				          ], gap=<20,20>);
			});
}

data BORDER = BORDER(str C);

void border1(){
	render("border1", #BORDER, BORDER("red"), Figure (BORDER m) {
			return
				hcat(figs=[ text("Enter:", fontSize=18), 
				
	                   strInput(event=on("submit", bind(m.C))), 
	                   
	                   box(lineColor=m.C, lineWidth=10, size=<100,100>),
	                   
	                   box(lineColor=m.C, lineWidth=10, size=<100,100>)
				   ], gap=<20,20>);
			  });
}

void border2(){
	render("border2", #BORDER, BORDER("red"), Figure (BORDER m) {
			return
				hcat(figs=[ text("Enter:", fontSize=18), 
				
	                   colorInput(event=on("change", bind(m.C)), size=<100,25>), 
	                   
	                   box(lineColor=m.C, lineWidth=10, size=<100,100>),
	                   
	                   box(lineColor=m.C, lineWidth=10, size=<100,100>)
				   ], gap=<20,20>);
			  });
}

data CONTROL = CONTROL(str FC, int LW, int WIDTH , int HEIGHT);

void control1(){
	render("control1", #CONTROL, CONTROL("red",1,100,100), Figure (CONTROL m) {
			return
				vcat(figs=[
					hcat(figs=[ text("  fillColor:", size=<150,50>, fontSize=20), colorInput(event=on("submit", bind(m.FC)), size=<100,25>),
				
					       text("lineWidth:", size=<150,50>, fontSize=20), numInput(event=on("input", bind(m.LW)), size=<80,25>),
					
					       text("     width:", size=<150,50>, fontSize=20), numInput(event=on("input", bind(m.WIDTH)), size=<100,25>),
					
					       text("    height:", size=<150,50>, fontSize=20), numInput(event=on("input", bind(m.HEIGHT)), size=<100,25>)
					     ]),
					
					box(size=<100,100>, lineWidth=0),
					
	                box(fillColor=m.FC, lineWidth=m.LW, width=m.WIDTH, height=m.HEIGHT)
	                   
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
				   		   figs = [ box(fillColor="red", size=<100,100>),
								      box(fillColor="white", size=<100,100>),
								      box(fillColor="blue", size=<100,100>)
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
				   		  figs = [ box(fillColor="red", size=<100,100>),
								     box(fillColor="white", size=<100,100>),
								     box(fillColor="blue", size=<100,100>)
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
			
				   box(width=m.WIDTH, height=m.HEIGHT, fillColor="pink")
	             ],			  
				 gap=<10,20>);
		});
}

void slider3(){
	render("slider3", #SLIDER, SLIDER(25), Figure (SLIDER m) {
			return 
			vcat(figs=[ rangeInput(low=0, high=50, step=5, event=on("change", bind(m.SLIDER)), size=<200,50>),
				   box(size=<50,50>, lineWidth=0),
				   box(lineWidth=m.SLIDER, size=<150,50>, fillColor="red")
	             ], align=topLeft,		  
				 gap=<80,80>);
				 });
}

// Resize barChart

data SIZE = SIZE(int SIZE);

void slider4(){

	render("slider4", #SIZE, SIZE(300), Figure (SIZE m) {
			low = 100;
			high = 500;
			return vcat(figs=[ hcat(figs=[text("SIZE"), text(low), rangeInput(low=low,high=high,step=5, event=on("change", bind(m.SIZE)), size=<500,50>), text(high) ]),
				               barChart(width=m.SIZE, height=m.SIZE, dataset=exampleBarData())
	             ],			  
				 gap=<10,20>);
		});
}


data VISABLE = VISABLE(bool VISABLE);

void visible1(){
	render("visible1", #VISABLE, VISABLE(true), Figure (VISABLE m) {
			return 
			vcat(figs=[ buttonInput( trueText="hide", falseText="show", event=on("click", bind(m.VISABLE)),size=<50,50>),
				   
				   visible(condition=m.VISABLE,  fig=box(size=<150,50>, fillColor="red"))
	             ], align=topLeft,		  
				 gap=<30,30>);
				 });
}

void visible2(){
	render("visible2", #VISABLE, VISABLE(true), Figure (VISABLE m) {
			return 
			vcat(figs=[ checkboxInput(event=on("click", bind(m.VISABLE)), size=<50,50>),
				   
				   visible(condition=m.VISABLE,  fig=box(size=<150,50>, fillColor="red"))
	             ], align=topLeft,		  
				 gap=<30,30>);
				 });
}

// Tooltip

data EMPTY = EMPTY();

Event tooltip(str txt) = on("mouseover", box(size=<50,50>,fig=text(txt, fontSize=12, lineColor="black"), fillColor="yellow"));

void tooltip1(){
	ex("tooltip1", box(fillColor="red", width=100, height=100, event=tooltip("I am a red box")));
}

void tooltip2(){
	render("tooltip2", #EMPTY, EMPTY(), Figure (EMPTY m) {
			return 
				vcat(figs=[ box(size=<200,50>, lineColor="white"),
					   hcat(figs=[ box(fillColor="red", width=100, height=100, event=tooltip("I am a red box")),
					   			   box(fillColor="white", width=100, height=100),
						           box(fillColor="blue", width=100, height=100, event=tooltip("I am a blue box"))
	                        ])
	                  ],		  
				 gap=<10,20>);
		});
}


data COLOR1 = COLOR1(str C);

void boxcolor1(){
          
	render("boxcolor1", #COLOR1, COLOR1("white"), Figure (COLOR1 m) {
			return box(size=<100,100>, fig=colorInput(event=on("change", bind(m.C)), size=<50,20>, fillColor=m.C, rounded=<10,10>, gap=<20,20>,lineDashing=[1,1,1,1,1,1]));
		});
}

data COLOR2 = COLOR2(str C1, str C2);

void boxcolor2(){
          
	render("boxcolor2", #COLOR2, COLOR2("white", "blue"), Figure (COLOR2 m) {
			return hcat(figs=[ box(size=<100,100>, fig=colorInput(event=on("change", bind(m.C1)), size=<50,20>, fillColor=m.C1, rounded=<10,10>, gap=<20,20>,lineDashing=[1,1,1,1,1,1])),
						       box(size=<100,100>, fig=colorInput(event=on("change", bind(m.C2)), size=<50,20>, fillColor=m.C2, rounded=<10,10>, gap=<20,20>,lineDashing=[1,1,1,1,1,1]))
						     ], gap=<20,30>);
		});
}

