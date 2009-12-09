module experiments::VL::Examples

import experiments::VL::VLCore;
import experiments::VL::VLRender;

import Integer;
import List;
import Set;
import IO;

public void box1(){
	render(box([ width(100), height(200) ]));
}

public void box2(){
	render(box([ width(100), height(200), fillColor("green") ]));
}

public void box3(){
	render(box([ width(100), height(200), fillColor("green"), lineColor("red")]));
}

public void box4(){
	render(combine([lineWidth(2)],
	               [
					 box([ width(100), height(200), fillColor("mediumblue") ]),
	                 box([ width(100), height(200), fillColor(rgb(0, 0, 205)) ]),
	                 box([ width(100), height(200), fillColor(rgb(0, 0, 205, 0.5)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.5)) ])
	                ]));
}

public void box5(){
	render(combine([
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.05)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.2)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.4)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.6)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.8)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 1.0)) ])
	                ]));
}

public void box6(){
	render(combine([
	                 box([ width(100), height(200), fillColor(gray(125, 0.05)) ]),
	                 box([ width(100), height(200), fillColor(gray(125, 0.2)) ]),
	                 box([ width(100), height(200), fillColor(gray(125, 0.4)) ]),
	                 box([ width(100), height(200), fillColor(gray(125, 0.6)) ]),
	                 box([ width(100), height(200), fillColor(gray(125, 0.8)) ]),
	                 box([ width(100), height(200), fillColor(gray(125, 1.0)) ]) 
	                ]));
}

public void box7(){
	render(box([fillColor("mediumblue"), gap(10)], box([width(40), height(40), fillColor("white")])));
}

public void box8(){
	render(box([fillColor("mediumblue"), gap(10)], ellipse([width(40), height(40), fillColor("white")])));
}

public void box9(){
	render(box([fillColor("mediumblue"), gap(10)], label([text("een label")])));
}

public void lab1(){
	render(label([ text("Een label"), fontSize(20), fillColor("black")]));
}

public void lab2(){
	render(combine([ label([ text("Label een"), fontSize(20), fillColor("black")]),
	 				 label([ text("Label twee"), fontSize(40), fillColor("blue")])
	 			   ]));
}

public void lab3(){
	render(label([ text("Een label"), fontSize(20), fillColor("black"), textAngle(-90)]));
}

public void rlab1(){
	render(combine([vertical(), center(), gap (10)],
	               [
	                
	                label([ text("Een label"), fontSize(20), fillColor("black"), textAngle(-90) ]),
	                box([ width(100), height(200), fillColor("red")])
	                
	                ]));
}

public void rlab2(){
	render(overlay([bottom()],
	              [box([ width(100), height(200), fillColor("red") ]),
			       label([ text("Een label"), fontSize(20), fillColor("black")])
			      ]
		));
}

public void r3(){
	render(combine([fillColor("yellow"), gap(10),bottom()],
	              [box([ width(100), height(200), fillColor("red") ]),
			       box([ width(150), height(100)]),
			       box([ width(200), height(50), fillColor("green") ])
			      ]
		));
}

public void r4(){
	render(combine([vertical(),left(), gap(2)],
	              [box([ width(100), height(200), fillColor("red") ]),
			       box([ width(150), height(100), fillColor("blue") ]),
			       box([ width(200), height(50), fillColor("green") ])
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

public void e1(){
	render(ellipse([width(50), height(100)]));
}


public void e2(){
	render(ellipse([fillColor("mediumblue"), gap(10)], box([width(40), height(40), fillColor("white")])));
}

public void e3(){
	render(ellipse([fillColor("mediumblue"), gap(10)], label([text("een label")])));
}

public void o1(){

render(overlay([center()],
               [ box([width(100), height(300), fillColor("green")]), 
                 box([width(200), height(200), fillColor("red")])
               ])
      );
}

public void o2(){
render(overlay([bottom()],
               [box([width(100), height(100)]),
                ellipse([width(50), height(50)])
              ]));
}

public void s1(){
    dt1 = [10, 20, 10, 30];
	b = shape([
                lineColor("blue"),
                lineWidth(2),
	            fillColor("lightgreen")
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([width(10),height(10), lineWidth(0), fillColor("lightblue")])) | int i <- [0 .. size(dt1) -1]]
               );
    render(b);
}

public void s2(){
    dt1 = [10, 20, 10, 30];
	b = shape([
                lineColor("blue"),
                lineWidth(2),
	            fillColor("lightgreen"),
	            curved()
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([width(10),height(10), lineWidth(0), fillColor("lightblue")])) | int i <- [0 .. size(dt1) -1]]
               );
    render(b);
}

public void s3(){
    dt1 = [10, 20, 10, 30];
    dt2 = [15, 10, 25, 20];
	sh1 = shape([
                lineColor("blue"),
                lineWidth(2),
	            fillColor(color("lightblue", 0.5)),
	            curved(), closed()
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([width(10),height(10), lineWidth(0), fillColor("white")])) | int i <- [0 .. size(dt1) -1]]
               );
    sh2 = shape([
                lineColor("green"),
                lineWidth(2),
	            fillColor(color("lightgreen", 0.5)),
	            curved(), closed()
               ],
               [ vertex(i * 50, 10 * dt2[i], ellipse([width(10),height(10), lineWidth(0), fillColor("black")])) | int i <- [0 .. size(dt2) -1]]
               );
    render(overlay([bottom(), left()], [sh1, sh2]));
}



public void grid1(){
  boxes = [box([width(50), height(50),fillColor("red")]), box([width(30), height(30),fillColor("yellow")]), 
           box([width(30), height(30),fillColor("green")]), box([width(70), height(50),fillColor("blue")]),
           box([width(70), height(70),fillColor("black")])
           ];

  render(grid([width(120), gap(10), top()], boxes));
}

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

public void pie1(){
     elems =
     	[ box([width(10), height(100), fillColor("green")])  ,
     	  box([width(20), height(150), fillColor("red")]),
     	  box([width(30), height(200), fillColor("blue")])
     	];
    	    
    render(pie([fromAngle(0), toAngle(360), innerRadius(0), gap(0), lineWidth(5), lineColor(0)], elems));
}

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

public void graph1(){
     nodes =
     	[ box([id("A"), width(10), height(20), fillColor("green")]),
     	  box([id("B"), width(20), height(30), fillColor("red")]),
     	  box([id("C"), width(30), height(20), fillColor("blue")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "C", "A")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

public void tree1(){

   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")])
     	];
     	
    edges = 
    	[    	  
    	];
    	  
    render(tree([gap(10), top()], nodes, edges));
}

public void tree2(){

   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")]),
     	  box([id("B"), width(20), height(20), fillColor("red")])
     	];
     	
    edges = 
    	[ edge([lineWidth(1)], "A", "B")
    	  
    	];
    	  
    render(tree([gap(10), top()], nodes, edges));
}
public void tree3(){

   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")]),
     	  box([id("B"), width(20), height(20), fillColor("red")]),
     	  box([id("C"), width(20), height(20), fillColor("blue")])
     	];
     	
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
          edge([lineWidth(1)], "A", "C")
    	  
    	];
    	  
    render(tree([gap(10), top()], nodes, edges));
}

public void tree4(){

   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")]),
     	  box([id("B"), width(20), height(20), fillColor("red")]),
     	  box([id("C"), width(20), height(20), fillColor("blue")]),
     	  box([id("D"), width(20), height(20), fillColor("purple")])
     	];
     	
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
          edge([lineWidth(1)], "A", "C"),
          edge([lineWidth(1)], "A", "D")
    	  
    	];
    	  
    render(tree([gap(10), top()], nodes, edges));
}


public void ltree1(){

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
     	  box([id("J"), width(20), height(20), fillColor("white")]),
     	  box([id("K"), width(20), height(20), fillColor("deeppink")])
     	  
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "A", "C"),
    	   edge([lineWidth(1)], "A", "K"),
    	  edge([lineWidth(1)], "A", "D"),
    	 
    	  edge([lineWidth(1)], "B", "E"),
    	  edge([lineWidth(1)], "B", "F"),
    	  edge([lineWidth(1)], "B", "G"),
    	  edge([lineWidth(1)], "D", "H"),
    	  edge([lineWidth(1)], "D", "I"),
    	  edge([lineWidth(1)], "D", "J")
    	  
    	];
    	    
    render(tree([gap(10), top()], nodes, edges));
}

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
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "B", "D"),
    	  edge([lineWidth(1)], "A", "E"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "E", "G")
    	  
    	];
    	    
    render(tree([gap(10), top()], nodes, edges));
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

public void mo1(){
	render(box([ width(100), height(200), fillColor("green"), mouseOver([fillColor("red")]) ]));
}

public void mo2(){
	render(combine( [ mouseOver([lineColor("red")]) ],
				    [
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.05)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.2)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.4)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.6)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 0.8)) ]),
	                 box([ width(100), height(200), fillColor(color("mediumblue", 1.0)) ])
	                ]));
}