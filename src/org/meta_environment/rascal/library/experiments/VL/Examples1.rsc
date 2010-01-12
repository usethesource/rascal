module experiments::VL::Examples1

import experiments::VL::VLCore;
import experiments::VL::VLRender; 
// import viz::VLRender;

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

// Unsized blue outer box with white inner ellipse of 40x40

public void box6(){
	render(box([fillColor("mediumblue"), gap(10)], ellipse([size(40), fillColor("white")])));
}

// Unsized blue outer box with white inner text

public void box7(){
	render(box([fillColor("mediumblue"), gap(10)], text("een label")));
}

// Blue outer box of 20x20 with white large inner text

public void box8(){
	render(box([width(20), height(20), fillColor("mediumblue"), gap(10)], text("een label")));
}

// Unsized outer box, with centered inner box of 100x200

public void bbc(){
	render(box([gap(5, 30)], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Unsized outer box, with left-aligned inner box of 100x200

public void bbl(){
	render(box([width(150), height(250), gap(5, 30), left()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Unsized outer box, with top-aligned inner box of 100x200

public void bblt(){
	render(box([width(150), height(250), gap(10), left(), top()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Unsized outer box, with bottom-aligned inner box of 100x200
public void bblb(){
	render(box([width(150), height(250), gap(10), left(), bottom()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Unsized outer box, with right-aligned inner box of 100x200
public void bbr(){
	render(box([width(150), height(250), gap(10), right()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Unsized outer box, with top-aligned and right-aligned inner box of 100x200
public void bbrt(){
	render(box([width(150), height(250), gap(10), right(), top()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Unsized outer box, with bottom-aligned and right-aligned inner box of 100x200
public void bbrb(){
	render(box([width(150), height(250), gap(10), right(), bottom()], box([size(100,200), fillColor("green"), lineColor("red")])));
}

// Horizontal combination of boxes of 100x200 with rgb and named colors
public void hor1(){
	render(horizontal([lineWidth(2), size(100,200)],
	               [
					 box([ fillColor("mediumblue") ]),
	                 box([ fillColor(rgb(0, 0, 205)) ]),
	                 box([ fillColor(rgb(0, 0, 205, 0.5)) ]),
	                 box([ fillColor(color("mediumblue", 0.5)) ])
	                ]));
}

// Horizontal combination of boxes of 100x200 with named colors and opacity
public void hor2(){
	render(horizontal([size(100,200)],
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
	render(horizontal([size(100,200)],
	               [
	                 box([ fillColor(gray(125, 0.05)) ]),
	                 box([ fillColor(gray(125, 0.2)) ]),
	                 box([ fillColor(gray(125, 0.4)) ]),
	                 box([ fillColor(gray(125, 0.6)) ]),
	                 box([ fillColor(gray(125, 0.8)) ]),
	                 box([ fillColor(gray(125, 1.0)) ]) 
	                ]));
}

// Horizontal combination of boxes of with some inherited colors
public void hor4(){
	render(horizontal([fillColor("yellow"), gap(10),bottom()],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ size(150,100)]),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}

// Horizontal, bottom aligned with on exception
public void hor5(){
	render(horizontal([gap(10),bottom()],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ vcenter(), size(150,100)]),
			       box([ size(200,50), fillColor("green") ]),
			       box([ size(50,100), fillColor("yellow") ])
			      ]
		));
}


// Vertical combination of boxes 
public void vert1(){
	render(vertical([left(), gap(2)],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ size(150, 100), fillColor("blue") ]),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}

// Vertical, left aligned with on exception
public void vert2(){
	render(vertical([gap(10),left()],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ right(), size(150,100)]),
			       box([ size(200,50), fillColor("green") ]),
			       box([ size(50,100), fillColor("yellow") ])
			      ]
		));
}

// TODO: how do we get the inner vertical left aligned?
public void vert3(){
	render(vertical([gap(10),left()],
	              [box([ size(100,200), fillColor("red") ]),
			       use([left()], vertical([right()], [ box([size(150,100)]),
			                             box([size(50,50)]),  
			                             box([size(30,30)])
			                           ])),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}


public void hv1(){
	render(horizontal([bottom(), gap(10)],
	              [ box([ size(100,250), fillColor("red") ]),
	                vertical([right(), gap(30)],
			                 [ horizontal([vcenter()],
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
// TODO baselines not ok

public void txt4(){
	render(box([gap(1)], 
	           horizontal([bottom()],
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
	render(vertical([center(), gap(10)],
	               [
	                box([ width(100), height(200), fillColor("red")]),
	                text([fontSize(20), textAngle(-90)], "Een label")
	                ]));
}

// Overlay of box and text
public void txt7(){
	render(overlay([bottom()],
	              [box([ width(100), height(200), fillColor("red") ]),
			       text([fontSize(20)], "Een label")
			      ]
		));
}

// Word cloud
// TODO: a mess regarding alignment
public void txt8(){

    d = ("aap" : 10, "noot" :5, "mies" : 7, 
         "wim" : 5, "zus": 10, "jet": 40, 
         "teun" : 10, "vuur" : 20, "gijs" : 5,
         "lam" : 50, "kees" : 30, "bok" : 20,
         "weide" : 20,  "does" : 25, "hok" : 15,
         "duif" : 30, "schapen" : 35);
    
     words = [text([ fontSize(2*d[name])], "<name>") | name <- d];
     
     render(grid([width(400), fillColor("black"), gap(10), bottom()], words));
}

// Barchart: Horizontal composition of boxes

public void bar1(){
    dt1 = [10, 12, 17, 0, 15, 7, 20, 40, 60];  
    colors = colorScale(dt1, color("blue"), color("red"));  
	b = horizontal([
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
    bars = [ horizontal([gap(5), top()], 
                     [ box([fillColor("green"), height((d1[i] ? 0) * 8)]),
                      box([fillColor("red"), height((d2[i] ? 0) * 8)])
                    ])
           | int i <- [0 .. m]
           ];
    
	b = horizontal([
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
public void bar2v(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [vertical([gap(0)],
                   [box([fillColor("green"), height((d1[i] ? 0) * 8)]),
                    box([fillColor("red"), height((d2[i] ? 0) * 8)])
                   ])
           | int i <- [0 .. m]
           ];
           
	b = horizontal([
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
	render(ellipse([fillColor("mediumblue"), gap(10)], text("een label")));
}

// TODO should explicit size overrule size of an inner object?
public void e4(){
	render(ellipse([width(40), height(20), fillColor("mediumblue"), gap(10)], text("een label")));
}

// Centered Overlay of two boxes
public void o1(){

render(overlay([center()],
               [ box([size(100,300), fillColor("green")]), 
                 box([size(200,200), fillColor("red")])
               ])
      );
}

// Bottom-aligned overlay of box and ellipse
public void o2(){
render(overlay([bottom()],
               [box([size(100)]),
                ellipse([size(50)])
              ]));
}

// Shape: line graph with circle on each point

public void s1(){
    dt1 = [10, 20, 10, 30];
	b = shape([
                lineColor("blue"),
                lineWidth(2),
	            fillColor("lightgreen")
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([size(10), lineWidth(0), fillColor("lightblue")])) | int i <- [0 .. size(dt1) -1]]
               );
    render(b);
}

// Shape: curved (fitted) graph with circle on each point

public void s2(){
    dt1 = [10, 20, 10, 30];
	b = shape([
                lineColor("blue"),
                lineWidth(2),
	            fillColor("lightgreen"),
	            curved()
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([size(10), lineWidth(0), fillColor("lightblue")])) | int i <- [0 .. size(dt1) -1]]
               );
    render(b);
}

// Two overlayed shapes with closed and curved graphs

public void s3(){
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
    render(overlay([bottom(), left()], [sh1, sh2]));
}

