module viz::Figure::ExampleLayout

import viz::Figure::Core;
import viz::Figure::Render; 
import Set;

// HVCat of boxes

public void align1(){
  boxes = [box([size(50,50),fillColor("red")])];

  render(hvcat([width(125), gap(10), top(), left()], boxes));
}

public void hvcattl(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(hvcat([width(125), gap(10), top(), left()], boxes));
}

public void hvcattc(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(hvcat([width(125), gap(10), top(), hcenter()], boxes));
}

public void hvcattr(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(hvcat([width(125), gap(10), top(), right()], boxes));
}

public void hvcatcl(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(hvcat([width(125), gap(10), vcenter(), left()], boxes));
}

public void hvcatcc(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(hvcat([width(125), gap(10), center()], boxes));
}

public void hvcatcr(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(hvcat([width(125), gap(10), vcenter(), right()], boxes));
}

public void hvcatbl(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(hvcat([width(125), gap(10), bottom(), left()], boxes));
}

public void hvcatbc(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(hvcat([width(125), gap(10), bottom(), hcenter()], boxes));
}

public void hvcatbr(){
  boxes = [box([size(50,50),fillColor("red")]), box([size(30,30),fillColor("yellow")]), 
           box([size(30,30),fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70,70),fillColor("black")])
           ];

  render(hvcat([width(125), gap(10), bottom(), right()], boxes));
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


public void grid1(){
  boxes = [box([width(50), height(50),fillColor("red")]), box([width(30), height(30),fillColor("yellow")]), 
           box([width(30), height(30),fillColor("green")]), box([width(70), height(50),fillColor("blue")]),
           box([width(70), height(70),fillColor("black")])
           ];

  render(grid([width(120), gap(10), top()], boxes));
}

public void grid11(){

 boxes = [box([width(50), height(50),fillColor("red")]), box([width(30), height(30),fillColor("yellow")]), 
           box([width(30), height(30),fillColor("green")]), box([width(70), height(50),fillColor("blue")]),
           box([width(70), height(70),fillColor("black")])
           ];
           
 g1 = grid([width(120), gap(10), top()], boxes);
 g2 = grid([width(80), gap(10), top()], boxes);
 
 render(hcat([gap(50)], [g1, g2]));
 
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

public void pack0(){
  boxes = [box([width(50), height(50),fillColor("red")])];
   render(pack([width(200), height(170), lineWidth(0), gap(5,10), top()], boxes));
}

public void pack1(){
  boxes = [box([size(50),fillColor("red")]), box([size(30),fillColor("yellow")]), 
           box([size(30) ,fillColor("green")]), box([size(70,50),fillColor("blue")]),
           box([size(70),fillColor("black")]), box([size(10,20),fillColor("orange")]),
           box([size(80,10),fillColor("grey")]), box([size(10,150),fillColor("black")]),
           box([size(10),fillColor("lightblue")]),
           box([size(10),fillColor("lightblue")]),
           box([size(10),fillColor("lightblue")]),
           box([size(10,20),fillColor("orange")]) ,
           box([size(10,20),fillColor("orange")]),
           box([size(10,20),fillColor("orange")]) ,
           box([size(10,20),fillColor("orange")])                  
           ];

  render(pack([width(200), height(170), lineWidth(0), gap(5,10), top()], boxes));
}

public void pack2(){
  boxes = [box([width(50), height(10),fillColor("red")]), box([width(30), height(20),fillColor("yellow")]), 
           box([width(30), height(150),fillColor("green")]), box([width(70), height(30),fillColor("blue")]),
           box([width(70), height(10),fillColor("black")]), box([width(10), height(20),fillColor("orange")]),
           box([width(80), height(10),fillColor("grey")]), box([width(10), height(20),fillColor("white")]),
           box([width(10), height(10),fillColor("lightblue")]),
           box([width(10), height(10),fillColor("lightblue")]),
           box([width(10), height(10),fillColor("lightblue")]),
           box([width(10), height(20),fillColor("orange")]) ,
           box([width(10), height(20),fillColor("orange")]),
           box([width(10), height(20),fillColor("orange")]) ,
           box([width(10), height(20),fillColor("orange")])                  
           ];

  render(pack([width(200), height(170), lineWidth(0), gap(5,5), top()], boxes));
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

