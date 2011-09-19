@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
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
	render(hcat([box(fillColor("mediumblue")),box(fillColor(rgb(0, 0, 205))),box(fillColor(rgb(0, 0, 205, 0.5))),box(fillColor(color("mediumblue", 0.5)))],std(lineWidth(2))));
}

// Horizontal combination of boxeswith named colors and opacity
public void hor2(){
	render(hcat(   [
	                 box(fillColor(color("mediumblue", 0.05))),
	                 box(fillColor(color("mediumblue", 0.2))),
	                 box(fillColor(color("mediumblue", 0.4))),
	                 box(fillColor(color("mediumblue", 0.6))),
	                 box(fillColor(color("mediumblue", 0.8))),
	                 box(fillColor(color("mediumblue", 1.0)))
	                ]
	               
	                ));
}

// Horizontal combination of boxes  with grey color and different opacities
public void hor3(){
	render(hcat(   [
	                 box(fillColor(gray(125, 0.05))),
	                 box(fillColor(gray(125, 0.2))),
	                 box(fillColor(gray(125, 0.4))),
	                 box(fillColor(gray(125, 0.6))),
	                 box(fillColor(gray(125, 0.8))),
	                 box(fillColor(gray(125, 1.0))) 
	                ]
	                ));
}

// Horizontal combination of top-aligned boxes with some inherited colors
public void hor4(){
	render(hcat(  [ box(vshrink(0.2),hshrink(0.5), fillColor("red"),top()),
			        box(vshrink(0.8),top()),
			        box(shrink(0.1,0.5), fillColor("green"),top())
			      ],
			      std(fillColor("yellow")), hgrow(1.1))
		);
}
// Horizontal combination of bottom-aligned boxes with some inherited colors
public void hor5(){
	render(hcat(  [ box(vshrink(0.2),hshrink(0.5), fillColor("red"),bottom()),
			        box(vshrink(0.8),bottom()),
			        box(shrink(0.1,0.5), fillColor("green"),bottom())
			      ],
			      std(fillColor("yellow")), hgrow(1.1))
		);
}

// Horizontal combination of centered boxes with some inherited colors
public void hor6(){
	render(hcat(  [ box(vshrink(0.2),hshrink(0.5), fillColor("red"),vcenter()),
			        box(vshrink(0.8),vcenter()),
			        box(shrink(0.1,0.5), fillColor("green"),vcenter())
			      ],
			      std(fillColor("yellow")), hgrow(1.1))
		);
}
/*
// Horizontal, nested
public void hor8(){
	render(hcat(  [ box(size(100,200), fillColor("red")),
			        box(align(0.5,1.0), size(150,100)),
			        hcat([box(size(200,50), fillColor("green")),
			              box(size(50,100), fillColor("yellow"))
			             ], child(bottom())
			            )
			      ],
			      gap(10),child(top())
		));
}

// Horizontal, nested
public void hor9(){
	render(hcat(  [ box(size(100,200), fillColor("red")),
			        box(align(0.5,1.0), size(150,100)),
			        hcat([box(size(200,50), fillColor("green")),
			              box(size(50,100), fillColor("yellow"))
			             ], child(bottom())
			            )
			      ],
			      gap(10)
		));
}

// Horizontal, top aligned at 0.1, 0.2, 0.3, 0.4 of top
public void hor10(){
	render(hcat(  [ box(align(0.0, 0.1), size(100,100), fillColor("red")),
			        box(align(0.0, 0.2), size(100,100)),
			        box(align(0.0, 0.3), size(100,100), fillColor("green")),
			        box(align(0.0, 0.4), size(100,100), fillColor("yellow"))
			      ],
			      gap(10)
		));
}

// ===== vcat ======


// Vertical combination of boxes, left-aligned 
public void vert1(){
	render(vcat( [ box(size(100,200), fillColor("red")),
			       box(size(150, 100), fillColor("blue")),
			       box(size(200,50), fillColor("green"))
			     ],
			     child(left()), gap(2)
		));
}

// Vertical combination of boxes, centered 
public void vert2(){
	render(vcat(  [ box(size(100,200), fillColor("red")),
			        box(size(150, 100), fillColor("blue")),
			        box(size(200,50), fillColor("green"))
			      ],
			      gap(2), child(hcenter())
		));
}

// Vertical combination of boxes, right-aligned 
public void vert3(){
	render(vcat(  [ box([ size(100,200), fillColor("red") ]),
			        box([ size(150, 100), fillColor("blue") ]),
			        box([ size(200,50), fillColor("green") ])
			      ],
			      child(right()), gap(2)
		));
}

// Vertical, left aligned with on exception
public void vert4(){
	render(vcat(  [ box(size(100,200), fillColor("red")),
			        box(align(1.0, 1.0), size(150,100)),
			        box(size(200,50), fillColor("green")),
			        box(size(50,100), fillColor("yellow"))
			      ],
			      gap(10)
		));
}

// Nested vertical composition with left/right alignment
public void vert5(){
	render(vcat( [ box(size(100,200), fillColor("red")),
			       vcat([ box(size(150,100)),
			              box(size(50,50)),  
			              box(size(30,30))
			            ],
			            child(right())),
			       box(size(200,50), fillColor("green"))
			      ],
			      gap(10),child(left())
		));
}

// Nested vertical composition with left/left alignment
public void vert6(){
	render(vcat( [ box(size(100,200), fillColor("red")),
			       vcat([ box(size(150,100)),
			              box(size(50,50)),  
			              box(size(30,30))
			            ],
			            child(left())), 
			       box(size(200,50), fillColor("green"))
			      ],
			      gap(10),child(left())
		));
}

// Vertical, left aligned at 0.1, 0.2, 0.3, 0.4 of left side
public void vert7(){
	render(vcat(  [ box(align(0.1, 0.0), size(100,100), fillColor("red")),
			        box(align(0.2, 0.0), size(100,100)),
			        box(align(0.3, 0.0), size(100,100), fillColor("green")),
			        box(align(0.4, 0.0), size(100,100), fillColor("yellow"))
			      ],
			      gap(10)
		));
}

// ===== hv =====

public void hv1(){
	render(hcat([ box(size(100,250), fillColor("red")),
	              vcat([ hcat( [ box(size(50, 100), fillColor("blue")),
			                     box(size(100, 50), fillColor("blue"))
			                   ],
			                   child(vcenter())),
			             box(size(250,50), fillColor("green"))
			           ],
			           child(right()), gap(30)),
			      box(size(100,100), fillColor("yellow"))
			    ],
			    child(bottom()), gap(10)));
}

public void r3(){
	render(hcat(  [ box(width(100), height(200), fillColor("red")),
			        box(width(150), height(100)),
			        box(width(200), height(50), fillColor("green"))
			      ],
			      fillColor("yellow"), gap(10),child(bottom())
		));
}

public void r4(){
	render(vcat(  [ box(width(100), height(200), fillColor("red")),
			        box(width(150), height(100), fillColor("blue")),
			        box(width(200), height(50), fillColor("green"))
			      ],
			      child(left()), gap(2)
		));
}

// ===== overlay =====

// Left Overlay of two boxes
public void olc(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               child(left(), vcenter())
               )
      );
}

// Centered Overlay of two boxes
public void occ(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               child(center())
               )
      );
}

// Right Overlay of two boxes
public void orc(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               child(right(), vcenter())
               )
      );
}

// Left, top Overlay of two boxes
public void olt(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               child(left(), top())
               )
      );
}

// Center, top Overlay of two boxes
public void oct(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               child(hcenter(), top())
               )
      );
}

// Right, top Overlay of two boxes
public void ort(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               child(right(), top())
               )
      );
}

// Left, bottom Overlay of two boxes
public void olb(){
render(overlay(
               [ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               child(left(), bottom())
               )
      );
}

// Center, bottom Overlay of two boxes
public void ocb(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               child(hcenter(), bottom())
               )
      );
}

// Right, bottom Overlay of two boxes
public void orb(){
render(overlay([ box(size(100,300), fillColor("green")), 
                 box(size(200,200), fillColor("red"))
               ],
               child(right(), bottom())
               )
      );
}

// Bottom-aligned overlay of box and ellipse
public void obe(){
render(overlay([ box(size(100)),
                ellipse(size(50))
               ],
               child(bottom())
               ));
}


// Nested vertical composition with left/right alignment
public void onest1(){
	render(overlay( [ box(size(100,250), fillColor("red")),
			          vcat([ box(size(150,100)),
			                 box(size(50,50)),  
			                 box(size(30,30))
			               ],
			               child(right())), 
			         box(size(150,5), fillColor("green"))
			        ],
			        gap(10),child(left())
		));
}

// Vertical, left aligned at 0.1, 0.2, 0.3, 0.4 of left side
public void onest2(){
	render(overlay(  [ box(align(0.1, 0.0), size(100,100), fillColor("red")),
			           box(align(0.2, 0.0), size(100,100)),
			           box(align(0.3, 0.0), size(100,100), fillColor("green")),
			           box(align(0.4, 0.0), size(100,100), fillColor("yellow"))
			      ],
			      gap(10)
		));
}

public void exwidth() {
	render(hcat([box(),box(),box()],width(350),std(fillColor("red"))));
}


public void exwidthspace() {
	render(hcat([box(),box(),box()],width(350),std(fillColor("red")),gapFactor(0.2)));
}


public void exwidthspaceex() {
	render(hcat([box(),box(width(150)),box()],width(350),std(fillColor("red")),gapFactor(0.2)));
}

public void alignandautoheight() {
	render(hcat([box(bottom()),box(top()),box(vcenter())],width(300),height(200)));
}

public void alignandautoheight2() {
	render(hcat([box(bottom(),height(100)),box(top(),height(150)),box(vcenter())],width(300),height(200)));
}

public void alignandautoheight2() {
	render(hcat([box(bottom()),box(top(),height(150)),box(vcenter())],width(300),height(200)));
}
*/
