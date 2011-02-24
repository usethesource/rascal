module vis::examples::Compose

import vis::Figure;
import vis::Render;

import Number;
import List;
import Set;
import IO;

// ===== hcat =====

// Horizontal combination of boxes of 100x200 with rgb and (same) named colors
public void hor1(){
	render(hcat(   [
					 box(fillColor("mediumblue")),
	                 box(fillColor(rgb(0, 0, 205))),
	                 box(fillColor(rgb(0, 0, 205, 0.5))),
	                 box(fillColor(color("mediumblue", 0.5)))
	                ],
	                lineWidth(2), size(100,200)
	                ));
}

// Horizontal combination of boxes of 100x200 with named colors and opacity
public void hor2(){
	render(hcat(   [
	                 box(fillColor(color("mediumblue", 0.05))),
	                 box(fillColor(color("mediumblue", 0.2))),
	                 box(fillColor(color("mediumblue", 0.4))),
	                 box(fillColor(color("mediumblue", 0.6))),
	                 box(fillColor(color("mediumblue", 0.8))),
	                 box(fillColor(color("mediumblue", 1.0)))
	                ],
	                size(100,200)
	                ));
}
// Horizontal combination of boxes of 100x200 with grey color and different opacities
public void hor3(){
	render(hcat(   [
	                 box(fillColor(gray(125, 0.05))),
	                 box(fillColor(gray(125, 0.2))),
	                 box(fillColor(gray(125, 0.4))),
	                 box(fillColor(gray(125, 0.6))),
	                 box(fillColor(gray(125, 0.8))),
	                 box(fillColor(gray(125, 1.0))) 
	                ],
	                size(100,200)
	                ));
}

// Horizontal combination of top-aligned boxes with some inherited colors
public void hor4(){
	render(hcat(  [ box(size(100,200), fillColor("red")),
			        box(size(150,100)),
			        box(size(200,50), fillColor("green"))
			      ],
			      fillColor("yellow"), gap(10),top()
		));
}
// Horizontal combination of bottom-aligned boxes with some inherited colors
public void hor5(){
	render(hcat(  [ box(size(100,200), fillColor("red")),
			        box(size(150,100)),
			        box(size(200,50), fillColor("green"))
			      ],
			      fillColor("yellow"), gap(10),bottom()
		));
}

// Horizontal combination of centered boxes with some inherited colors
public void hor6(){
	render(hcat(  [ box(size(100,200), fillColor("red")),
			        box(size(150,100)),
			        box(size(200,50), fillColor("green"))
			      ],
			      fillColor("yellow"), gap(10),vcenter()
		));
}

// Horizontal, bottom aligned with on exception
public void hor7(){
	render(hcat(  [ box(size(100,200), fillColor("red")),
			        box(anchor(0.5,0.8), size(150,100)),
			        box(size(200,50), fillColor("green")),
			        box(size(50,100), fillColor("yellow"))
			      ],
			      gap(10),alignAnchors(true), bottom()
		));
}

// Horizontal, nested
public void hor8(){
	render(hcat(  [ box(size(100,200), fillColor("red")),
			        box(anchor(0.5,1.0), size(150,100)),
			        hcat([box(size(200,50), fillColor("green")),
			              box(size(50,100), fillColor("yellow"))
			             ], bottom()
			            )
			      ],
			      gap(10),top()
		));
}

// Horizontal, nested
public void hor9(){
	render(hcat(  [ box(size(100,200), fillColor("red")),
			        box(anchor(0.5,1.0), size(150,100)),
			        hcat([box(size(200,50), fillColor("green")),
			              box(size(50,100), fillColor("yellow"))
			             ], bottom()
			            )
			      ],
			      gap(10),alignAnchors(true)
		));
}

// Horizontal, top aligned at 0.1, 0.2, 0.3, 0.4 of top
public void hor10(){
	render(hcat(  [ box(anchor(0.0, 0.1), size(100,100), fillColor("red")),
			        box(anchor(0.0, 0.2), size(100,100)),
			        box(anchor(0.0, 0.3), size(100,100), fillColor("green")),
			        box(anchor(0.0, 0.4), size(100,100), fillColor("yellow"))
			      ],
			      gap(10), alignAnchors(true)
		));
}

// ===== vcat ======


// Vertical combination of boxes, left-aligned 
public void vert1(){
	render(vcat( [ box(size(100,200), fillColor("red")),
			       box(size(150, 100), fillColor("blue")),
			       box(size(200,50), fillColor("green"))
			     ],
			     left(), gap(2)
		));
}

// Vertical combination of boxes, centered 
public void vert2(){
	render(vcat(  [ box(size(100,200), fillColor("red")),
			        box(size(150, 100), fillColor("blue")),
			        box(size(200,50), fillColor("green"))
			      ],
			      gap(2), hcenter()
		));
}

// Vertical combination of boxes, right-aligned 
public void vert3(){
	render(vcat(  [ box([ size(100,200), fillColor("red") ]),
			        box([ size(150, 100), fillColor("blue") ]),
			        box([ size(200,50), fillColor("green") ])
			      ],
			      right(), gap(2)
		));
}

// Vertical, left aligned with on exception
public void vert4(){
	render(vcat(  [ box(size(100,200), fillColor("red")),
			        box(anchor(1.0, 1.0), size(150,100)),
			        box(size(200,50), fillColor("green")),
			        box(size(50,100), fillColor("yellow"))
			      ],
			      gap(10),alignAnchors(true)
		));
}

// Nested vertical composition with left/right alignment
public void vert5(){
	render(vcat( [ box(size(100,200), fillColor("red")),
			       vcat([ box(size(150,100)),
			              box(size(50,50)),  
			              box(size(30,30))
			            ],
			            right()),
			       box(size(200,50), fillColor("green"))
			      ],
			      gap(10),left()
		));
}

// Nested vertical composition with left/left alignment
public void vert6(){
	render(vcat( [ box(size(100,200), fillColor("red")),
			       vcat([ box(size(150,100)),
			              box(size(50,50)),  
			              box(size(30,30))
			            ],
			            left()), 
			       box(size(200,50), fillColor("green"))
			      ],
			      gap(10),left()
		));
}

// Vertical, left aligned at 0.1, 0.2, 0.3, 0.4 of left side
public void vert7(){
	render(vcat(  [ box(anchor(0.1, 0.0), size(100,100), fillColor("red")),
			        box(anchor(0.2, 0.0), size(100,100)),
			        box(anchor(0.3, 0.0), size(100,100), fillColor("green")),
			        box(anchor(0.4, 0.0), size(100,100), fillColor("yellow"))
			      ],
			      gap(10), alignAnchors(true)
		));
}

// ===== hv =====

public void hv1(){
	render(hcat([ box(size(100,250), fillColor("red")),
	              vcat([ hcat( [ box(size(50, 100), fillColor("blue")),
			                     box(size(100, 50), fillColor("blue"))
			                   ],
			                   vcenter()),
			             box(size(250,50), fillColor("green"))
			           ],
			           right(), gap(30)),
			      box(size(100,100), fillColor("yellow"))
			    ],
			    bottom(), gap(10)));
}

public void r3(){
	render(hcat(  [ box(width(100), height(200), fillColor("red")),
			        box(width(150), height(100)),
			        box(width(200), height(50), fillColor("green"))
			      ],
			      fillColor("yellow"), gap(10),bottom()
		));
}

public void r4(){
	render(vcat(  [ box(width(100), height(200), fillColor("red")),
			        box(width(150), height(100), fillColor("blue")),
			        box(width(200), height(50), fillColor("green"))
			      ],
			      left(), gap(2)
		));
}

// ===== overlay =====

// Left Overlay of two boxes
public void olc(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               left(), vcenter()
               )
      );
}

// Centered Overlay of two boxes
public void occ(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               center()
               )
      );
}

// Right Overlay of two boxes
public void orc(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               right(), vcenter()
               )
      );
}

// Left, top Overlay of two boxes
public void olt(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               left(), top()
               )
      );
}

// Center, top Overlay of two boxes
public void oct(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               hcenter(), top()
               )
      );
}

// Right, top Overlay of two boxes
public void ort(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               right(), top()
               )
      );
}

// Left, bottom Overlay of two boxes
public void olb(){
render(overlay(
               [ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               left(), bottom()
               )
      );
}

// Center, bottom Overlay of two boxes
public void ocb(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               hcenter(), bottom()
               )
      );
}

// Right, bottom Overlay of two boxes
public void orb(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               right(), bottom()
               )
      );
}

// Bottom-aligned overlay of box and ellipse
public void obe(){
render(overlay([ box(size(100)),
                ellipse(size(50))
               ],
               bottom()
               ));
}

// TODO: ok?

// Nested vertical composition with left/left alignment
public void onest1(){
	render(overlay( [ box(size(100,250), fillColor("red")),
			          vcat([ box(size(150,100)),
			                 box(size(50,50)),  
			                 box(size(30,30))
			               ],
			               right()), 
			         box(size(150,5), fillColor("green"))
			        ],
			        gap(10),left()
		));
}

// Vertical, left aligned at 0.1, 0.2, 0.3, 0.4 of left side
public void onest2(){
	render(overlay(  [ box(anchor(0.1, 0.0), size(100,100), fillColor("red")),
			           box(anchor(0.2, 0.0), size(100,100)),
			           box(anchor(0.3, 0.0), size(100,100), fillColor("green")),
			           box(anchor(0.4, 0.0), size(100,100), fillColor("yellow"))
			      ],
			      gap(10), alignAnchors(true)
		));
}
