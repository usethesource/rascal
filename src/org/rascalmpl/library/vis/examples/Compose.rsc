module vis::examples::Compose

import vis::Figure;
import vis::Render;

import Number;
import List;
import Set;
import IO;


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



public void rlab2(){
	render(overlay([bottom()],
	              [box([ width(100), height(200), fillColor("red") ]),
			       label([ text("Een label"), fontSize(20), fillColor("black")])
			      ]
		));
}

public void r3(){
	render(hcat([fillColor("yellow"), gap(10),bottom()],
	              [box([ width(100), height(200), fillColor("red") ]),
			       box([ width(150), height(100)]),
			       box([ width(200), height(50), fillColor("green") ])
			      ]
		));
}

public void r4(){
	render(vcat([left(), gap(2)],
	              [box([ width(100), height(200), fillColor("red") ]),
			       box([ width(150), height(100), fillColor("blue") ]),
			       box([ width(200), height(50), fillColor("green") ])
			      ]
		));
}

// Simple bar charts

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

// Barchart: Horizontal composition of vertically stacked boxes
public void bar4(){ 
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