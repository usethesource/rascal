module viz::Figure::Examples1

import viz::Figure::Core;
import viz::Figure::Render;
import Integer;

import List;
import Set;
import IO;

// Unfilled box of 100x200
public void box1(){
	render(box([ width(100), height(200) ]));
}

// Unfilled box of 100x200
public void box2(){
	render(box([ size(100,200) ]));
}

// Green box of 100x200
public void box3(){
	render(box([ size(100,200), fillColor("green") ]));
}

// Green box of 100x200 with red border
public void box4(){
	render(box([ size(100,200), fillColor("green"), lineColor("red")]));
}

// Unsized blue outer box with white inner box of 40x40
public void box5(){
	render(box([fillColor("mediumblue"), gap(10)], box([size(40), fillColor("white")])));
}

// Unsized blue outer box with white inner ellipse of 40x60

public void box6(){
	render(box([fillColor("mediumblue"), gap(10)], ellipse([size(40,60), fillColor("white")])));
}

// Unsized blue outer box with black inner text

public void box7(){
	render(box([fillColor("mediumblue"), gap(10)], text("een label")));
}

// Unsized blue outer box with white inner text

public void box8(){
	render(box([fillColor("mediumblue"), gap(10)], text([fontColor("white")], "een label")));
}

// Blue outer box of 20x20 with black large inner text (only visible on mouse over)

public void box9(){
	render(box([width(20), height(20), fillColor("mediumblue"), gap(10)], text("een label")));
}

// Blue outer box of 20x20 with yellow larger inner box (only visible on mouse over)
//Note: left/top borders of innerbox is not visible
public void box10(){
	render(box([width(20), height(20), fillColor("mediumblue"), gap(10)], box([size(30,30), fillColor("yellow")])));
}

// Unsized outer box, with centered inner box of 100x200

public void bbc(){
	render(box([gap(5, 30)], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with left-aligned inner box of 100x200

public void bbl(){
	render(box([width(150), height(300), gap(5, 30), left()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with top-aligned inner box of 100x200

public void bblt(){
	render(box([width(150), height(300), gap(5,30), left(), top()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with bottom-aligned inner box of 100x200
public void bblb(){
	render(box([width(150), height(300), gap(5,30), left(), bottom()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with right-aligned inner box of 100x200
public void bbr(){
	render(box([width(150), height(300), gap(5,30), right()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with top-aligned and right-aligned inner box of 100x200
public void bbrt(){
	render(box([width(150), height(300), gap(5,30), right(), top()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Sized outer box, with bottom-aligned and right-aligned inner box of 100x200
public void bbrb(){
	render(box([width(150), height(300), gap(5,30), right(), bottom()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Horizontal combination of boxes of 100x200 with rgb and (same) named colors
public void hor1(){
	render(hcat([lineWidth(2), size(100,200)],
	               [
					 box([ fillColor("mediumblue") ]),
	                 box([ fillColor(rgb(0, 0, 205)) ]),
	                 box([ fillColor(rgb(0, 0, 205, 0.5)) ]),
	                 box([ fillColor(color("mediumblue", 0.5)) ])
	                ]));
}

// Horizontal combination of boxes of 100x200 with named colors and opacity
public void hor2(){
	render(hcat([size(100,200)],
	               [
	                 box([ fillColor(color("mediumblue", 0.05)) ]),
	                 box([ fillColor(color("mediumblue", 0.2)) ]),
	                 box([ fillColor(color("mediumblue", 0.4)) ]),
	                 box([ fillColor(color("mediumblue", 0.6)) ]),
	                 box([ fillColor(color("mediumblue", 0.8)) ]),
	                 box([ fillColor(color("mediumblue", 1.0)) ])
	                ]));
}
// Horizontal combination of boxes of 100x200 with grey color and different opacities
public void hor3(){
	render(hcat([size(100,200)],
	               [
	                 box([ fillColor(gray(125, 0.05)) ]),
	                 box([ fillColor(gray(125, 0.2)) ]),
	                 box([ fillColor(gray(125, 0.4)) ]),
	                 box([ fillColor(gray(125, 0.6)) ]),
	                 box([ fillColor(gray(125, 0.8)) ]),
	                 box([ fillColor(gray(125, 1.0)) ]) 
	                ]));
}

// Horizontal combination of top-aligned boxes with some inherited colors
public void hor4(){
	render(hcat([fillColor("yellow"), gap(10),top()],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ size(150,100)]),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}
// Horizontal combination of bottom-aligned boxes with some inherited colors
public void hor5(){
	render(hcat([fillColor("yellow"), gap(10),bottom()],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ size(150,100)]),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}

// Horizontal combination of centered boxes with some inherited colors
public void hor6(){
	render(hcat([fillColor("yellow"), gap(10),vcenter()],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ size(150,100)]),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}

// Horizontal, bottom aligned with on exception
public void hor7(){
	render(hcat([gap(10),bottom()],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ vcenter(), size(150,100)]),
			       box([ size(200,50), fillColor("green") ]),
			       box([ size(50,100), fillColor("yellow") ])
			      ]
		));
}

// Horizontal, top aligned at 0.1, 0.2, 0.3, 0.4 of top
public void hor8(){
	render(hcat([gap(10)],
	              [box([ anchor(0.0, 0.1), size(100,100), fillColor("red") ]),
			       box([ anchor(0.0, 0.2), size(100,100)]),
			       box([ anchor(0.0, 0.3), size(100,100), fillColor("green") ]),
			       box([ anchor(0.0, 0.4), size(100,100), fillColor("yellow") ])
			      ]
		));
}


// Vertical combination of boxes, left-aligned 
public void vert1(){
	render(vcat([left(), gap(2)],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ size(150, 100), fillColor("blue") ]),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}

// Vertical combination of boxes, centered 
public void vert2(){
	render(vcat([left(), gap(2), hcenter()],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ size(150, 100), fillColor("blue") ]),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}

// Vertical combination of boxes, right-aligned 
public void vert3(){
	render(vcat([right(), gap(2)],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ size(150, 100), fillColor("blue") ]),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}

// Vertical, left aligned with on exception
public void vert4(){
	render(vcat([gap(10),left()],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ right(), size(150,100)]),
			       box([ size(200,50), fillColor("green") ]),
			       box([ size(50,100), fillColor("yellow") ])
			      ]
		));
}

// Nested vertical composition with left/right alignment
public void vert5(){
	render(vcat([gap(10),left()],
	              [box([ size(100,200), fillColor("red") ]),
			       use([right()], vcat([left()], [ box([size(150,100)]),
			                                      box([size(50,50)]),  
			                                      box([size(30,30)])
			                                    ])),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}

// Nested vertical composition with left/left alignment
public void vert6(){
	render(vcat([gap(10),left()],
	              [box([ size(100,200), fillColor("red") ]),
			       use([left()], vcat([left()], [ box([size(150,100)]),
			                                      box([size(50,50)]),  
			                                      box([size(30,30)])
			                                    ])),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}

// Vertical, left aligned at 0.1, 0.2, 0.3, 0.4 of left side
public void vert7(){
	render(vcat([gap(10)],
	              [box([ anchor(0.1, 0.0), size(100,100), fillColor("red") ]),
			       box([ anchor(0.2, 0.0), size(100,100)]),
			       box([ anchor(0.3, 0.0), size(100,100), fillColor("green") ]),
			       box([ anchor(0.4, 0.0), size(100,100), fillColor("yellow") ])
			      ]
		));
}

public void hv1(){
	render(hcat([bottom(), gap(10)],
	              [ box([ size(100,250), fillColor("red") ]),
	                vcat([right(), gap(30)],
			                 [ hcat([vcenter()],
			                              [ box([ size(50, 100), fillColor("blue") ]),
			                                box([ size(100, 50), fillColor("blue") ])
			                              ]),
			                   box([ size(250,50), fillColor("green") ])
			                 ]),
			        box([ size(100,100), fillColor("yellow") ])
			      ]));
}


// Text
public void txt1(){
	render(text("Een label"));
}

// Blue text of size 20
public void txt2(){
	render(text([fontSize(20), fontColor("blue")], "A giant label"));
}

// Unsized box with inner text
public void txt3(){
	render(box([gap(1)], text([fontSize(20), fontColor("black")], "A giant label")));
}

// Horizontal bottom-aligned composition of text of different size

public void txt4(){
	render(box([gap(10)], 
	           hcat([bottom()],
	               [ text([fontSize(20), fontColor("black")], "Giant xyz 1"),
	 				 text([fontSize(40), fontColor("blue")], "Giant xyz 2"),
	 				 text([fontSize(60), fontColor("red")], "Giant xyz 3")
	 			   ])));
}

// Text rotated -90 degrees (counter clockwise)
public void txt5(){
	render(text([ fontSize(20), textAngle(-90)], "Een label"));
}

// Vertical; composition of rotated text and a box

public void txt6(){
	render(vcat([center(), gap(10)],
	               [
	                box([ width(100), height(200), fillColor("red")]),
	                text([fontSize(20), textAngle(-90)], "Een label")
	                ]));
}

// Overlay of box and text
//Note: the result is not pretty since the baseline of the text and the bottom of the box are aligned.
public void txt7(){
	render(overlay([bottom(), right()],
	              [box([ width(150), height(200), fillColor("red") ]),
			       text([fontSize(20)], "Een label")
			      ]
		));
}

// Vertical stack of text of various font sizes

public void txt8(){
   render(box([gap(1)], 
	           vcat([bottom()],
	               [ text([fontSize(20), fontColor("black")], "A"),
	 				 text([fontSize(40), fontColor("blue")], "BB"),
	 				 text([fontSize(60), fontColor("red")], "CC")
	 			   ])));
}

public void txt9(){
   words = [ text("aappp"), text("noot"), text("mies"), text("wim"), text("zus") ];
   
   render(grid([width(100), fillColor("black"), gap(40), bottom(), left()], words));
 }
 //TODO
 public void txt10(){
   words = [ text("aappp"), text("noot"), text("mies"), text("wim"), text("zus") ];
   
   render(grid([width(100), fillColor("black"), gap(40), bottom(), left(), textAngle(-90)], words));
 }

private map[str, int] leesplank = 
 ("aap" : 10, "noot" :5, "mies" : 7,
        "wim" : 5, "zus": 10, "jet": 40, 
        "teun" : 10, "vuur" : 20, "gijs" : 5,
        "lam" : 50, "kees" : 30, "bok" : 20,
        "weide" : 20,  "does" : 25, "hok" : 15,
        "duif" : 30, "schapen" : 35
         );
         
// Word cloud using align
public void txt11(){
     words = [text([ fontSize(2*leesplank[name])], "<name>") | name <- leesplank];
     render(align([width(400), fillColor("black"), gap(10), bottom(), left()], words));
}

// Word cloud using pack
public void txt12(){
     words = [text([ fontSize(2*leesplank[name])], "<name>") | name <- leesplank];
     render(pack([width(400), fillColor("black"), gap(10), bottom(), left()], words));
}

// Word cloud using pack with rotated words
public void txt13(){
     words = [text([ fontSize(2*leesplank[name]), (arbInt(3) == 2) ? textAngle(-90) : textAngle(0)], "<name>") | name <- leesplank];
     render(pack([width(400), fillColor("black"), gap(10), bottom(), left()], words));
}

// Barchart: Horizontal composition of boxes

public void bar1(){
    dt1 = [10, 12, 17, 0, 15, 7, 20, 40, 60];  
    colors = colorScale(dt1, color("blue"), color("red"));  
	b = hcat([
                lineColor(0),
                lineWidth(1),
	            fillColor(125),
	            width(10),
	            bottom()
               ],
               [ box([height(d * 8), fillColor(colors(d))]) | d <- dt1 ]
               );
    render(b);
}

// Barchart: Horizontal composition of two sets of boxes (top aligned)
public void bar2(){
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [ hcat([gap(5), top()], 
                     [ box([fillColor("green"), height((d1[i] ? 0) * 8)]),
                      box([fillColor("red"), height((d2[i] ? 0) * 8)])
                    ])
           | int i <- [0 .. m]
           ];
    
	b = hcat([
                lineColor(0),
                lineWidth(1),
	          	width(10),
	          	top(),
	          	gap(10)
               ],
               bars
               );
    render(b);
}
// Barchart: Horizontal composition of vertically stacked boxes
public void bar3(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [vcat([gap(0)],
                   [box([fillColor("green"), height((d1[i] ? 0) * 8)]),
                    box([fillColor("red"), height((d2[i] ? 0) * 8)])
                   ])
           | int i <- [0 .. m]
           ];
           
	b = hcat([
                lineColor(0),
                lineWidth(1),
	          	width(10),
	          	gap(5),
	          	bottom()
               ],
               bars
               );
    render(b);
}

// Ellipse of 50x100

public void e1(){
	render(ellipse([width(50), height(100)]));
}

// Unsized blue ellipse with sized white inner box
public void e2(){
	render(ellipse([fillColor("mediumblue"), gap(20)], box([size(40), fillColor("white")])));
}

// Unsized blue ellipse with sized white inner text
public void e3(){
	render(ellipse([fillColor("mediumblue"), gap(10)], text([fontColor("white")], "een label")));
}

// Sized ellipse with inner text that appears on mouseOver.
public void e4(){
	render(ellipse([width(40), height(20), fillColor("mediumblue"), gap(10)], text([fontColor("white")], "een label")));
}

// Left Overlay of two boxes
public void olc(){
render(overlay([left(), vcenter()],
               [ box([size(100,300), fillColor("green")]), 
                 box([size(200,200), fillColor("red")])
               ])
      );
}

// Centered Overlay of two boxes
public void occ(){
render(overlay([center()],
               [ box([size(100,300), fillColor("green")]), 
                 box([size(200,200), fillColor("red")])
               ])
      );
}

// Right Overlay of two boxes
public void orc(){
render(overlay([right(), vcenter()],
               [ box([size(100,300), fillColor("green")]), 
                 box([size(200,200), fillColor("red")])
               ])
      );
}

// Left, top Overlay of two boxes
public void olt(){
render(overlay([left(), top()],
               [ box([size(100,300), fillColor("green")]), 
                 box([size(200,200), fillColor("red")])
               ])
      );
}

// Center, top Overlay of two boxes
public void oct(){
render(overlay([hcenter(), top()],
               [ box([size(100,300), fillColor("green")]), 
                 box([size(200,200), fillColor("red")])
               ])
      );
}

// Right, top Overlay of two boxes
public void ort(){
render(overlay([right(), top()],
               [ box([size(100,300), fillColor("green")]), 
                 box([size(200,200), fillColor("red")])
               ])
      );
}

// Left, bottom Overlay of two boxes
public void olb(){
render(overlay([left(), bottom()],
               [ box([size(100,300), fillColor("green")]), 
                 box([size(200,200), fillColor("red")])
               ])
      );
}

// Center, bottom Overlay of two boxes
public void ocb(){
render(overlay([hcenter(), bottom()],
               [ box([size(100,300), fillColor("green")]), 
                 box([size(200,200), fillColor("red")])
               ])
      );
}

// Right, bottom Overlay of two boxes
public void orb(){
render(overlay([right(), bottom()],
               [ box([size(100,300), fillColor("green")]), 
                 box([size(200,200), fillColor("red")])
               ])
      );
}

// Bottom-aligned overlay of box and ellipse
public void obe(){
render(overlay([bottom()],
               [box([size(100)]),
                ellipse([size(50)])
              ]));
}

// Shape: line graph with circle on each point

public void s1(int s){
    dt1 = [10, 20, 10, 30];
	b = shape([
                lineColor("blue"),
                lineWidth(2),
                connected(),
	            fillColor("lightgreen")
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([size(s), lineWidth(0), fillColor("red")])) | int i <- [0 .. size(dt1) -1]]
               );
    render(b);
}

public void s2(int s){
    dt1 = [10, 20, 0, 30];
	b = shape([
                lineColor("blue"),
                lineWidth(2),
                connected(), closed(),
	            fillColor("lightgreen")
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([size(s), lineWidth(0), fillColor("red")])) | int i <- [0 .. size(dt1) -1]]
               );
    render(overlay([b, box([left(), bottom(), size(20)])]));
}



// Shape: curved (fitted) graph with circle on each point

public void s3(int s){
    dt1 = [10, 20, 10, 30];
	b = shape([
                lineColor("blue"),
                lineWidth(2),
	            fillColor("lightgreen"),
	            connected(),
	            curved()
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([size(s), lineWidth(0), fillColor("lightblue")])) | int i <- [0 .. size(dt1) -1]]
               );
    render(b);
}

// Two overlayed shapes with closed and curved graphs
public void s4(){
    dt1 = [10, 20, 10, 30];
    dt2 = [15, 10, 25, 20];
	sh1 = shape([
                lineColor("blue"),
                lineWidth(2),
	            fillColor(color("lightblue", 0.5)),
	            curved(), closed()
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([size(10), lineWidth(0), fillColor("white")])) | int i <- [0 .. size(dt1) -1]]
               );
    sh2 = shape([
                lineColor("green"),
                lineWidth(2),
	            fillColor(color("lightgreen", 0.5)),
	            curved(), closed()
               ],
               [ vertex(i * 50, 10 * dt2[i], ellipse([size(10), lineWidth(0), fillColor("black")])) | int i <- [0 .. size(dt2) -1]]
               );
    render(overlay([sh1, sh2]));
}