module experiments::VL::Examples

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

// Blue outer box of 20x20 with white inner text

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
public void comb1(){
	render(combine([lineWidth(2), size(100,200)],
	               [
					 box([ fillColor("mediumblue") ]),
	                 box([ fillColor(rgb(0, 0, 205)) ]),
	                 box([ fillColor(rgb(0, 0, 205, 0.5)) ]),
	                 box([ fillColor(color("mediumblue", 0.5)) ])
	                ]));
}

// Horizontal combination of boxes of 100x200 with named colors and opacity
public void comb2(){
	render(combine([size(100,200)],
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
public void comb3(){
	render(combine([size(100,200)],
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
public void comb4(){
	render(combine([fillColor("yellow"), gap(10),bottom()],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ size(150,100)]),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}
// Vertical combination of boxes 
public void comb5(){
	render(combine([vertical(),left(), gap(2)],
	              [box([ size(100,200), fillColor("red") ]),
			       box([ size(150, 100), fillColor("blue") ]),
			       box([ size(200,50), fillColor("green") ])
			      ]
		));
}

public void txt1(){ // default fill color is white!
	render(text("Een label"));
}

public void txt2(){
	render(text([fontSize(20), fillColor("black")], "Een label"));
}

public void txt3(){
	render(box([gap(1)], text([fontSize(20), fillColor("black")], "Een label")));
}


public void txt4(){
	render(combine([ text([fontSize(20), fillColor("black")], "Label een"),
	 				 text([fontSize(40), fillColor("blue")], "Label twee")
	 			   ]));
}

public void txt5(){
	render(text([ fontSize(20), fillColor("black"), textAngle(-90)], "Een label"));
}

public void txt6(){
	render(combine([vertical(), center(), gap (10)],
	               [
	                text([fontSize(20), fillColor("black"), textAngle(-90)], "Een label"),
	                box([ width(100), height(200), fillColor("red")])
	                ]));
}

public void txt7(){
	render(overlay([bottom()],
	              [box([ width(100), height(200), fillColor("red") ]),
			       text([fontSize(20), fillColor("black")], "Een label")
			      ]
		));
}



public void bar1(){
    dt1 = [10, 12, 17, 0, 15, 7, 20, 40, 60];  
    colors = colorScale(dt1, color("blue"), color("red"));  
	b = combine([
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

public void bar2(){
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [ combine([gap(5), top()], 
                     [ box([fillColor("green"), height((d1[i] ? 0) * 8)]),
                      box([fillColor("red"), height((d2[i] ? 0) * 8)])
                    ])
           | int i <- [0 .. m]
           ];
    
	b = combine([
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

public void bar2v(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [combine([vertical(), gap(0)],
                   [box([fillColor("green"), height((d1[i] ? 0) * 8)]),
                    box([fillColor("red"), height((d2[i] ? 0) * 8)])
                   ])
           | int i <- [0 .. m]
           ];
           
	b = combine([
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
	render(ellipse([fillColor("mediumblue"), gap(10)], box([size(40), fillColor("white")])));
}

// Unsized blue ellipse with sized white inner text
public void e3(){
	render(ellipse([fillColor("mediumblue"), gap(10)], text("een label")));
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

// Shape

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

// Shape

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

// Two overlayed shapes

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

// Grid of boxes

public void grid1(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(grid([width(120), gap(10), top()], boxes));
}

// Packed boxes

public void pack1(){
  boxes = [box([width(50), height(50),fillColor("red")]), box([width(30), height(30),fillColor("yellow")]), 
           box([width(30), height(30),fillColor("green")]), box([width(70), height(50),fillColor("blue")]),
           box([width(70), height(70),fillColor("black")]), box([width(10), height(20),fillColor("orange")]),
           box([width(80), height(10),fillColor("grey")]), box([width(10), height(150),fillColor("white")]),
           box([width(10), height(10),fillColor("lightblue")]),
           box([width(10), height(10),fillColor("lightblue")]),
           box([width(10), height(10),fillColor("lightblue")]),
           box([width(10), height(20),fillColor("orange")]) ,
           box([width(10), height(20),fillColor("orange")]),
           box([width(10), height(20),fillColor("orange")]) ,
           box([width(10), height(20),fillColor("orange")])                  
           ];

  render(pack([width(200), height(170), lineWidth(0), gap(10), top()], boxes));
}

// Pie chart

public void pie1(){
     elems =
     	[ box([size(10,100), fillColor("green")])  ,
     	  box([size(20,150), fillColor("red")]),
     	  box([size(30,200), fillColor("blue")])
     	];
    	    
    render(pie([fromAngle(0), toAngle(360), innerRadius(0), gap(0), lineWidth(5), lineColor(0)], elems));
}

// Two overlayed Pie charts

public void pie2(){
     elems =
     	[ box([width(10), height(100), fillColor("green")])  ,
     	  box([width(20), height(150), fillColor("red")]),
     	  box([width(30), height(200), fillColor("blue")])
     	];
    	    
    p1 = pie([fromAngle(0), toAngle(320), innerRadius(50), gap(0), lineWidth(5), lineColor(0)], elems);
    p2 = pie([fromAngle(0), toAngle(270), innerRadius(0), gap(0), lineWidth(5), lineColor(0)], elems);
    render(overlay([center()], [p1, p2]));
}

// Graph

public void graph1(){
     nodes =
     	[ box([id("A"), width(10), height(20), fillColor("green")]),
     	  box([id("B"), width(20), height(30), fillColor("red")]),
     	  box([id("C"), width(30), height(20), fillColor("blue")]),
     	  box([id("D"), width(20), height(20), fillColor("purple")]),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")]),
          box([id("F"), width(20), height(20), fillColor("orange")]),
     	  box([id("G"), width(20), height(20), fillColor("brown")]),
     	  box([id("H"), width(20), height(20), fillColor("black")]),
     	  box([id("I"), width(20), height(20), fillColor("grey")]),
     	  box([id("J"), width(20), height(20), fillColor("white")]),
     	  box([id("K"), width(20), height(20), fillColor("deeppink")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "C", "D"),
    	  edge([lineWidth(1)], "D", "E"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "F", "G"),
    	  edge([lineWidth(1)], "G", "H"),
    	  edge([lineWidth(1)], "H", "I"),
    	  edge([lineWidth(1)], "I", "J"),
    	  edge([lineWidth(1)], "J", "K"),
    	  
    	   edge([lineWidth(1)], "K", "A")
    	  
    	  //edge([lineWidth(1)], "A", "D")
    	 // edge([lineWidth(1)], "G", "K")
    	  
    	   
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

// Tree

public void tree1(){

   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")])
     	];
     	
    edges = 
    	[    	  
    	];
    	  
    render(tree([gap(10), top()], nodes, edges));
}

// Tree

public void tree2(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")])
     	];
     	
    edges = 
    	[ edge("A", "B")
    	  
    	];
    	  
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges));
}

// Tree
public void tree3(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")]),
     	  box([id("C"), fillColor("blue")])
     	];
     	
    edges = 
    	[ edge("A", "B"),
          edge("A", "C")
    	];
    	  
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges));
}

// Tree

public void tree4(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")]),
     	  box([id("C"), fillColor("blue")]),
     	  box([id("D"), fillColor("purple")])
     	];
     	
    edges = 
    	[ edge("A", "B"),
          edge("A", "C"),
          edge("A", "D")
    	];
    	  
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges));
}

// Tree

public void ltree1(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")]),
     	  box([id("C"), fillColor("blue")]),
     	  box([id("D"), fillColor("purple")]), 	
     	  box([id("E"), fillColor("lightblue")]),
          box([id("F"), fillColor("orange")]),
     	  box([id("G"), fillColor("brown")]),
     	  box([id("H"), fillColor("black")]),
     	  box([id("I"), fillColor("grey")]),
     	  box([id("J"), fillColor("white")]),
     	  box([id("K"), fillColor("deeppink")])
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("A", "C"),
    	  edge("A", "K"),
    	  edge("A", "D"),
    	  edge("B", "E"),
    	  edge("B", "F"),
    	  edge("B", "G"),
    	  edge("D", "H"),
    	  edge("D", "I"),
    	  edge("D", "J")
    	];
    	    
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges));
}

// Tree

public void ltree2(){

   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")]),
     	  box([id("B"), width(20), height(20), fillColor("red")]),
     	  box([id("C"), width(20), height(20), fillColor("blue")]),
     	  box([id("D"), width(20), height(20), fillColor("purple")]),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")]),
     	  box([id("F"), width(20), height(20), fillColor("orange")]),
     	  box([id("G"), width(20), height(20), fillColor("brown")]),
     	  box([id("H"), width(20), height(20), fillColor("black")]),
     	  box([id("I"), width(20), height(20), fillColor("grey")]),
     	  box([id("J"), width(20), height(20), fillColor("white")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "B", "D"),
    	  edge([lineWidth(1)], "A", "E"),
    	  edge([lineWidth(1)], "A", "H"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "E", "G"),
    	  edge([lineWidth(1)], "E", "I"),
    	  edge([lineWidth(1)], "E", "J")
    	  
    	];
    	    
    render(tree([gap(10), top()], nodes, edges));
}

// Tree with text popups.

public VELEM popup(str s){
	return box([width(0), height(0), gap(1), fillColor("yellow")], text(s));
}

public void oltree2(){

   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")], popup("Text For Box A")),
     	  box([id("B"), width(20), height(20), fillColor("red")], popup("Text For Box B")),
     	  box([id("C"), width(20), height(20), fillColor("blue")], popup("Text For Box C")),
     	  box([id("D"), width(20), height(20), fillColor("purple")], popup("Text For Box D")),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")], popup("Text For Box E")),
     	  box([id("F"), width(20), height(20), fillColor("orange")], popup("Text For Box F")),
     	  box([id("G"), width(20), height(20), fillColor("brown")], popup("Text For Box G")),
     	  box([id("H"), width(20), height(20), fillColor("black")], popup("Text For Box H")),
     	  box([id("I"), width(20), height(20), fillColor("grey")], popup("Text For Box I")),
     	  box([id("J"), width(20), height(20), fillColor("white")], popup("Text For Box J"))
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "B", "D"),
    	  edge([lineWidth(1)], "A", "E"),
    	  edge([lineWidth(1)], "A", "H"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "E", "G"),
    	  edge([lineWidth(1)], "E", "I"),
    	  edge([lineWidth(1)], "E", "J")
    	  
    	];
    	    
    render(tree([gap(10), top(), lineWidth(1), fillColor("black")], nodes, edges));
}

// Tree

public void ltree3(){

   nodes =
     	[ box([id("A"), width(10), height(20), fillColor("green")]),
     	  box([id("B"), width(20), height(60), fillColor("red")]),
     	  box([id("C"), width(60), height(20), fillColor("blue")]),
     	  box([id("D"), width(10), height(20), fillColor("purple")]),
     	  box([id("E"), width(30), height(20), fillColor("lightblue")]),
     	  box([id("F"), width(30), height(30), fillColor("orange")]),
     	  box([id("G"), width(30), height(50), fillColor("brown")])
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("B", "D"),
    	  edge("A", "E"),
    	  edge("E", "F"),
    	  edge("E", "G")
    	];
    	    
    render(tree([gap(10), top(), lineWidth(1)], nodes, edges));
}


alias CI = tuple[str name, int noa, int nom, int sloc];

set[CI] classes =
{ <"A", 1, 2, 100>,
  <"B", 5, 5, 10000>,
  <"C", 7, 7, 500>,
  <"D", 2, 10, 5000>,
  <"E", 10, 2, 1000>,
  <"F", 3, 3, 200>,
  <"G", 10, 10, 1000>
};

rel[str,str] inherits =
{ <"B", "A">,
  <"C", "B">,
  <"D", "C">,
  <"E", "B">,
  <"G", "F">
};

public void class1() {
   cscale = colorScale(toList(classes.sloc), color("green"), color("red"));
   render(combine([top()], [ box([width(c.noa*5), height(c.nom*5), fillColor(cscale(c.sloc))]) | CI c <- classes]));
}

public void class2() {
   cscale = colorScale(toList(classes.sloc), color("green"), color("red"));
   nodes = [ box([id(c.name), width(c.noa*5), height(c.nom*5), fillColor(cscale(c.sloc))]) | CI c <- classes];
   edges = [ edge([], from,to) | <str from, str to> <- inherits ];
   
   render(graph([width(400), height(400)], nodes, edges));      
}

// Mouse Over experiments

public void mo0(){
	render(box([ width(20), height(20), fillColor("green")], text("A very long label")));
}

public void mo1(){
	render(box([ width(100), height(200), fillColor("green"), mouseOver([fillColor("red")]) ]));
}

public void mo2(){
	render(box([ width(100), height(200), fillColor("green"), mouseOver([fillColor("red")], box([width(50), height(50)])) ]));
}

public void mo3(){
	render(combine( [ size(100,200), mouseOver([lineColor("red")]) , bottom()],
				    [
	                 box([ fillColor(color("mediumblue", 0.05)) ], text("A very wide label A")),
	                 box([ fillColor(color("mediumblue", 0.2)) ],  text("A very wide label B")),
	                 box([ fillColor(color("mediumblue", 0.4)) ], text("A very wide label C")),
	                 box([ fillColor(color("mediumblue", 0.6)) ], text("A very wide text D")),
	                 box([ fillColor(color("mediumblue", 0.8)) ], text("A very wide text E")),
	                 box([ fillColor(color("mediumblue", 1.0)) ], text("A very wide label F"))
	                ]));
}
