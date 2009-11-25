module experiments::VL::Examples

import experiments::VL::VLCore;
import experiments::VL::VLRender;

import Integer;
import List;
import Set;
import IO;

public void rect1(){
	render(rect([ width(100), height(200) ]));
}

public void rect2(){
	render(rect([ width(100), height(200), fillColor("green") ]));
}

public void rect3(){
	render(rect([ width(100), height(200), fillColor("green"), lineColor("red")]));
}

public void rect4(){
	render(rect([ width(100), height(200), fillColor(rgb(0, 0, 205)) ]));  
}

public void rect5(){
	render(rect([ width(100), height(200), fillColor(rgb(0, 0, 205, 0.2)) ]));  // TODO alpha does not work
}

public void lab1(){
	render(label([ text("Een label"), fontSize(20), fillColor("black")]));
}

public void lab2(){
	render(label([ text("Een label"), fontSize(20), fillColor("black"), textAngle(-90)]));
}

public void rlab1(){
	render(combine([vertical(), center(), gap (10)],
	               [
	                
	                label([ text("Een label"), fontSize(20), fillColor("black"), textAngle(-90) ]),
	                rect([ width(100), height(200), fillColor("red")])
	                
	                ]));
}

public void rlab2(){
	render(overlay([bottom()],
	              [rect([ width(100), height(200), fillColor("red") ]),
			       label([ text("Een label"), fontSize(20), fillColor("black")])
			      ]
		));
}

public void r3(){
	render(combine([fillColor("yellow"), gap(10),bottom()],
	              [rect([ width(100), height(200), fillColor("red") ]),
			       rect([ width(150), height(100)]),
			       rect([ width(200), height(50), fillColor("green") ])
			      ]
		));
}

public void r4(){
	render(combine([vertical(),left(), gap(0)],
	              [rect([ width(100), height(200), fillColor("red") ]),
			       rect([ width(150), height(100), fillColor("blue") ]),
			       rect([ width(200), height(50), fillColor("green") ])
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
	            width(10)
               ],
               [ rect([height(d * 8), fillColor(colors(d))]) | d <- dt1 ]
               );
    render(b);
}

public void bar2(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [ combine([gap(5), bottom()], 
                     [ rect([fillColor("green"), height((d1[i] ? 0) * 8)]),
                      rect([fillColor("red"), height((d2[i] ? 0) * 8)])
                    ])
           | int i <- [0 .. m]
           ];
    
	b = combine([
                lineColor(0),
                lineWidth(1),
	          	width(10),
	          	top(),
	          	gap(10),
	          	bottom()
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
                   [rect([fillColor("green"), height((d1[i] ? 0) * 8)]),
                    rect([fillColor("red"), height((d2[i] ? 0) * 8)])
                   ])
           | int i <- [0 .. m]
           ];
           
	b = combine([
                lineColor(0),
                lineWidth(1),
	          	width(10),
	          	gap(5)
               ],
               bars
               );
    render(b);
}

public void dot1(){
render(overlay([bottom()],
               [rect([width(100), height(100)]),
                dot([width(50), height(100), size(20)])
              ]));
}


public void dot2(){     
    dt1 = [10, 12, 17, 15, 7];      
	b = combine([
                lineColor("blue"),
                lineWidth(1),
	            fillColor("lightblue"),
	            width(10),
	            size(5)
               ],
               [ dot([height(d * 8)]) | d <- dt1]
               );
    render(b);
}

public void l1(){
    dt1 = [10, 12, 17, 15, 7];      
	b = combine([
                lineColor("blue"),
                lineWidth(2),
	            fillColor(125),
	            width(20)
               ],
               [ line([height(dt1[i] * 8), height2(dt1[i+1] * 8)]) | i <- [0 .. size(dt1) - 2]]
               );
    render(b);
}

public void a1(){  //TODO
    dt1 = [10, 12, 17, 15, 7];      
	b = combine([bottom(),
                lineColor(0),
                lineWidth(1),
	            fillColor(125),
	            width(50),
	            size(5)
               ],
               [ area([height(dt1[i] * 8), height2(dt1[i+1] * 8)]) | i <- [0 .. size(dt1) - 2]]
               );
    render(b);
}

public void dl1(){      //TODO
    dt1 = [10, 12, 17, 15, 7];      
	b = 
	            overlay([
                lineColor("blue"),
                lineWidth(0),
	            fillColor("lightblue"),
	            width(50),
	            size(10)
               ],
               [
            	   combine([ line([height(dt1[i] * 8), height2(dt1[i+1] * 8)]) | i <- [0 .. size(dt1) - 2]]),
            	   combine([ dot([height(d * 8)]) | d <- dt1])
               ]
               );
    render(b);
}

public void o1(){

render(overlay([center()],
               [ rect([width(100), height(300), fillColor("green")]), 
                 rect([width(200), height(200), fillColor("red")])
               ])
      );
}

public void grid1(){
  rects = [rect([width(50), height(50),fillColor("red")]), rect([width(30), height(30),fillColor("yellow")]), 
           rect([width(30), height(30),fillColor("green")]), rect([width(70), height(50),fillColor("blue")]),
           rect([width(70), height(70),fillColor("black")])
           ];

  render(grid([width(120), gap(10), top()], rects));
}

public void graph1(){
     nodes =
     	[ rect([name("A"), width(10), height(20), fillColor("green")]),
     	  rect([name("B"), width(20), height(30), fillColor("red")]),
     	  rect([name("C"), width(30), height(20), fillColor("blue")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "C", "A")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
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
   render(combine([top()], [ rect([width(c.noa*5), height(c.nom*5), fillColor(cscale(c.sloc))]) | CI c <- classes]));
}

public void class2() {
   cscale = colorScale(toList(classes.sloc), color("green"), color("red"));
   nodes = [ rect([name(c.name), width(c.noa*5), height(c.nom*5), fillColor(cscale(c.sloc))]) | CI c <- classes];
   edges = [ edge([], from,to) | <str from, str to> <- inherits ];
   
   render(graph([width(400), height(400)], nodes, edges));      
}
