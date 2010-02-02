module experiments::VL::Examples2

import experiments::VL::VLCore;
import experiments::VL::VLRender; 
import Set;

// Alignment of boxes

public void aligntl(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(align([width(125), gap(10), top(), left()], boxes));
}

public void aligntc(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(align([width(125), gap(10), top(), hcenter()], boxes));
}

public void aligntr(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(align([width(125), gap(10), top(), right()], boxes));
}

public void aligncl(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(align([width(125), gap(10), vcenter(), left()], boxes));
}

public void aligncc(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(align([width(125), gap(10), center()], boxes));
}

public void aligncr(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(align([width(125), gap(10), vcenter(), right()], boxes));
}

public void alignbl(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(align([width(125), gap(10), bottom(), left()], boxes));
}

public void alignbc(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(align([width(125), gap(10), bottom(), hcenter()], boxes));
}

public void alignbr(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(align([width(125), gap(10), bottom(), right()], boxes));
}

// Grid alignment

public void gridcc(){
  boxes = [box([size(20,20),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(20,20),fillColor("green")]), box([size(20,20),fillColor("blue")]),
           box([size(40,40),fillColor("black")])
           ];

  render(grid([width(90), gap(50)], boxes));
}

public void gridcl(){
  boxes = [box([size(20,20),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(20,20),fillColor("green")]), box([size(20,20),fillColor("blue")]),
           box([size(40,40),fillColor("black")])
           ];

  render(grid([width(90), gap(50), left()], boxes));
}

public void gridcr(){
  boxes = [box([size(20,20),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(20,20),fillColor("green")]), box([size(20,20),fillColor("blue")]),
           box([size(40,40),fillColor("black")])
           ];

  render(grid([width(90), gap(50), right()], boxes));
}

public void gridtc(){
  boxes = [box([size(20,20),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(20,20),fillColor("green")]), box([size(20,20),fillColor("blue")]),
           box([size(40,40),fillColor("black")])
           ];

  render(grid([width(90), gap(50), top()], boxes));
}

public void gridtl(){
  boxes = [box([size(20,20),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(20,20),fillColor("green")]), box([size(20,20),fillColor("blue")]),
           box([size(40,40),fillColor("black")])
           ];

  render(grid([width(90), gap(50), top(), left()], boxes));
}

public void gridtr(){
  boxes = [box([size(20,20),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(20,20),fillColor("green")]), box([size(20,20),fillColor("blue")]),
           box([size(40,40),fillColor("black")])
           ];

  render(grid([width(90), gap(50), top(), right()], boxes));
}

public void gridbc(){
  boxes = [box([size(20,20),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(20,20),fillColor("green")]), box([size(20,20),fillColor("blue")]),
           box([size(40,40),fillColor("black")])
           ];

  render(grid([width(90), gap(50), bottom()], boxes));
}

public void gridbl(){
  boxes = [box([size(20,20),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(20,20),fillColor("green")]), box([size(20,20),fillColor("blue")]),
           box([size(40,40),fillColor("black")])
           ];

  render(grid([width(90), gap(50), bottom(), left()], boxes));
}

public void gridbr(){
  boxes = [box([size(20,20),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(20,20),fillColor("green")]), box([size(20,20),fillColor("blue")]),
           box([size(40,40),fillColor("black")])
           ];

  render(grid([width(90), gap(50), bottom(), right()], boxes));
}

// one point grid

public void grid0(){
  boxes = [box([bottom(), right(), size(20,20),fillColor("red")]), 
           box([bottom(), left(), size(30,30),fillColor("yellow")]),
           box([top(), right(), size(20,20),fillColor("green")]),
           box([top(), left(), size(20,20),fillColor("blue")]),
           box([hcenter(), vcenter(), size(10,10),fillColor("black")])
           ];

  render(grid([gap(0)], boxes));
}

// Overlay with same effect as grid0

public void ov1(){
  boxes = [box([bottom(), right(), size(20,20),fillColor("red")]), 
           box([bottom(), left(), size(30,30),fillColor("yellow")]),
           box([top(), right(), size(20,20),fillColor("green")]),
           box([top(), left(), size(20,20),fillColor("blue")]),
           box([hcenter(), vcenter(), size(10,10),fillColor("black")])
           ];

  render(overlay(boxes));
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

// Tree: 1 node

public void tree1(){

   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")])
     	];
     	
    edges = 
    	[    	  
    	];
    	  
    render(tree([gap(10), top()], nodes, edges, "A"));
}

// Tree: 2 nodes

public void tree2(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")])
     	];
     	
    edges = 
    	[ edge("A", "B")
    	  
    	];
    	  
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges, "A"));
}

// Tree: 3 nodes
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
    	  
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges, "A"));
}

// Tree: 4 nodes

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
    	  
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges, "A"));
}

// Tree

public void tree5(){

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
    	    
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges, "A"));
}

// Tree

public void tree6(){

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
    	    
    render(tree([gap(10), top()], nodes, edges, "A"));
}

// Tree with text popups.

public VELEM popup(str s){
	return box([width(0), height(0), gap(1), fillColor("yellow")], text(s));
}

public void tree7(){
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
    render(tree([gap(10), top(), lineWidth(1), fillColor("black")], nodes, edges, "A"));
}

public void tree8(){
   nodes =
     	[ ellipse([id("A"), width(20), height(20), fillColor("green")], popup("Text For ellipse A")),
     	  ellipse([id("B"), width(20), height(20), fillColor("red")], popup("Text For ellipse B")),
     	  ellipse([id("C"), width(20), height(20), fillColor("blue")], popup("Text For ellipse C")),
     	  ellipse([id("D"), width(20), height(20), fillColor("purple")], popup("Text For ellipse D")),
     	  ellipse([id("E"), width(20), height(20), fillColor("lightblue")], popup("Text For ellipse E")),
     	  ellipse([id("F"), width(20), height(20), fillColor("orange")], popup("Text For ellipse F")),
     	  ellipse([id("G"), width(20), height(20), fillColor("brown")], popup("Text For ellipse G")),
     	  ellipse([id("H"), width(20), height(20), fillColor("black")], popup("Text For ellipse H")),
     	  ellipse([id("I"), width(20), height(20), fillColor("grey")], popup("Text For ellipse I")),
     	  ellipse([id("J"), width(20), height(20), fillColor("white")], popup("Text For ellipse J"))
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
    render(tree([gap(10), top(), lineWidth(1), fillColor("black")], nodes, edges, "A"));
}

// Tree

public void tree9(){

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
    	    
    render(tree([gap(10), top(), lineWidth(1)], nodes, edges, "A"));
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
   render(hcat([top()], [ box([width(c.noa*5), height(c.nom*5), fillColor(cscale(c.sloc))]) | CI c <- classes]));
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
	render(hcat( [ size(100,200), mouseOver([lineColor("red")]) , bottom()],
				 [
	               box([ fillColor(color("mediumblue", 0.05)) ], text("A very wide label A")),
	               box([ fillColor(color("mediumblue", 0.2)) ],  text("A very wide label B")),
	               box([ fillColor(color("mediumblue", 0.4)) ], text("A very wide label C")),
	               box([ fillColor(color("mediumblue", 0.6)) ], text("A very wide label D")),
	               box([ fillColor(color("mediumblue", 0.8)) ], text("A very wide label E")),
	               box([ fillColor(color("mediumblue", 1.0)) ], text("A very wide label F"))
	             ]));
}


/*
 * Rotation
 */
public void rot1a(){
  render(rotate(0.0, box([anchor(0.5, 0.5), size(50,70)])));
}

public void rot1b(){
  render(rotate(45.0, box([anchor(0.5, 0.5), size(50,70)])));
}

public void rot1c(){
  render(rotate(90.0, box([anchor(0.5, 0.5), size(50,70)])));
}

public void rot2(){
  render(rotate(45.0, box([anchor(1.0, 1.0), size(50,70)])));
}

public void rot3a(){
  render(rotate(90.0, box([anchor(0.0, 0.0), size(50,70)])));
}

public void rot3b(){
  render(rotate(90.0, box([anchor(0.0, 1.0), size(50,70)])));
}

public void rot3c(){
  render(rotate(90.0, box([anchor(1.0, 0.0), size(50,70)])));
}

public void rot3d(){
  render(rotate(90.0, box([anchor(1.0, 1.0), size(50,70)])));
}

public void rot4a(){
  render(rotate(45.0, box([anchor(0.0, 0.0), size(50,70)])));
}

public void rot4b(){
  render(rotate(45.0, box([anchor(0.0, 1.0), size(50,70)])));
}

public void rot4c(){
  render(rotate(45.0, box([anchor(1.0, 0.0), size(50,70)])));
}

public void rot4d(){
  render(rotate(45.0, box([anchor(1.0, 1.0), size(50,70)])));
}


public void rot5a(){
  render(rotate(135.0, box([anchor(0.0, 0.0), size(50,70)])));
}

public void rot5b(){
  render(rotate(135.0, box([anchor(0.0, 1.0), size(50,70)])));
}

public void rot5c(){
  render(rotate(135.0, box([anchor(1.0, 0.0), size(50,70)])));
}

public void rot5d(){
  render(rotate(135.0, box([anchor(1.0, 1.0), size(50,70)])));
}

public void rot6(){
  b1 = rotate(0.0, box([anchor(0.5, 0.5), fillColor("green"), size(50,70)]));
  b2 = rotate(45.0, box([anchor(0.5, 0.5), fillColor("yellow"), size(50,70)]));
  b3 = rotate(90.0, box([anchor(0.5, 0.5), fillColor("red"), size(50,70)]));
  b4 = rotate(145.0, box([anchor(0.5, 0.5), fillColor("blue"), size(50,70)]));
  render(overlay([b1, b2, b3, b4]));
}

public void rot7(){
  b1 = rotate(0.0, box([anchor(0.0, 0.0), fillColor("green"), size(50,70)]));
  b2 = rotate(45.0, box([anchor(0.0, 0.0), fillColor("yellow"), size(50,70)]));
  b3 = rotate(90.0, box([anchor(0.0, 0.0), fillColor("red"), size(50,70)]));
  b4 = rotate(145.0, box([anchor(0.0, 0.0), fillColor("blue"), size(50,70)]));
  render(overlay([b1, b2, b3, b4]));
}

public void rot8(){
  b1 = rotate(0.0, box([anchor(0.0, 1.0), fillColor("green"), size(50,70)]));
  b2 = rotate(45.0, box([anchor(0.0, 1.0), fillColor("yellow"), size(50,70)]));
  b3 = rotate(90.0, box([anchor(0.0, 1.0), fillColor("red"), size(50,70)]));
  b4 = rotate(145.0, box([anchor(0.0, 1.0), fillColor("blue"), size(50,70)]));
  render(overlay([b1, b2, b3, b4]));
}

public void rot9(){
  b1 = rotate(0.0, box([anchor(1.0, 0.0), fillColor("green"), size(50,70)]));
  b2 = rotate(45.0, box([anchor(1.0, 0.0), fillColor("yellow"), size(50,70)]));
  b3 = rotate(90.0, box([anchor(1.0, 0.0), fillColor("red"), size(50,70)]));
  b4 = rotate(145.0, box([anchor(1.0, 0.0), fillColor("blue"), size(50,70)]));
  render(overlay([b1, b2, b3, b4]));
}

public void rot10(){
  b1 = rotate(0.0, box([anchor(1.0, 1.0), fillColor("green"), size(50,70)]));
  b2 = rotate(45.0, box([anchor(1.0, 1.0), fillColor("yellow"), size(50,70)]));
  b3 = rotate(90.0, box([anchor(1.0, 1.0), fillColor("red"), size(50,70)]));
  b4 = rotate(145.0, box([anchor(1.0, 1.0), fillColor("blue"), size(50,70)]));
  render(overlay([b1, b2, b3, b4]));
}

/*
 * Scaling
 */
 
 public void sc1(){
   render(scale(1.0, 1.0, box([size(50,70)])));
 }
 
 public void sc2(){
   render(scale(2.0, 2.0, box([size(50,70)])));
 }
 
 public void sc3(){
   render(scale(2.0, box([size(50,70)])));
 }
 
  public void sc4(){
   render(scale(0.5, box([size(50,70)])));
 }
 
 public void sc5(){
   render(scale(0.5, 2.0, box([size(50,70)])));
 }
 
 
