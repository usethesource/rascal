@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::examples::Layout

import vis::Figure;
import vis::Render; 
import Set;
/*
// =====  hvcat =====

list[Figure] hvboxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

public void hvcattl(){  render(hvcat(hvboxes, width(125), gap(10), child(top(), left()))); }
public void hvcattc(){ render(hvcat(hvboxes, width(125), gap(10), child(top(), hcenter()))); }
public void hvcattr(){ render(hvcat(hvboxes, width(125), gap(10), child(top(), right()))); }
public void hvcatcl(){ render(hvcat(hvboxes, width(125), gap(10), child(vcenter(), left()))); }
public void hvcatcc(){ render(hvcat(hvboxes, width(125), gap(10), child(center()))); }
public void hvcatcr(){ render(hvcat(hvboxes, width(125), gap(10), child(vcenter(), right()))); }
public void hvcatbl(){ render(hvcat(hvboxes, width(125), gap(10), child(bottom(), left()))); }
public void hvcatbc(){ render(hvcat(hvboxes, width(125), gap(10), child(bottom(), hcenter()))); }
public void hvcatbr(){ render(hvcat(hvboxes, width(125), gap(10), child(bottom(), right()))); }

list[Figure] hvboxes2 = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(align(1.0,1.0), size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];
// TODO: check           
public void hvcatAA(){ render(hvcat(hvboxes2, width(125), gap(10))); }
           

// ===== grid =====

list[Figure] gboxes = [box(size(40,40),fillColor("lightgreen")), box(size(40,40),fillColor("lightblue")),
           box(size(40,40),fillColor("grey")),
           box(size(40,40),fillColor("red")), box(size(20,20),fillColor("yellow")), 
           box(size(40,40),fillColor("green")), box(size(40,40),fillColor("blue")),
           box(size(40,40),fillColor("black")), box(size(40,40),fillColor("purple"))
           ];

public void gridcc1(){ render(grid(gboxes, width(140), gap(60))); }
public void gridcl1(){ render(grid(gboxes, width(140), gap(60), child(left()))); }
public void gridcr1(){ render(grid(gboxes, width(140), gap(60), child(right()))); }
public void gridtc1(){ render(grid(gboxes, width(140), gap(60), child(top()))); }
public void gridbc1(){ render(grid(gboxes, width(140), gap(60), child(bottom()))); }
public void gridtl1(){ render(grid(gboxes, width(140), gap(60), child(top(), left()))); }
public void gridtr1(){ render(grid(gboxes, width(140), gap(60), child(top(), right()))); }
public void gridbl1(){ render(grid(gboxes, width(140), gap(60), child(bottom(), left()))); }
public void gridbr1(){ render(grid(gboxes, width(140), gap(60), child(bottom(), right()))); }

list[Figure] gboxes2 = [box(size(40,40),fillColor("lightgreen")), box(size(40,40),fillColor("lightblue")),
           box(size(40,40),fillColor("grey")),
           box(size(40,40),fillColor("red")), box(size(20,20),fillColor("yellow")), 
           box(size(40,40),fillColor("green")), box(size(20,40),fillColor("blue")),
           box(size(40,20),fillColor("black")), box(size(60,60),fillColor("purple"))
           ];
           
public void gridcc2(){ render(grid(gboxes2, width(140), gap(60))); }
public void gridcl2(){ render(grid(gboxes2, width(140), gap(60), child(left()))); }
public void gridcr2(){ render(grid(gboxes2, width(140), gap(60), child(right()))); }
public void gridtc2(){ render(grid(gboxes2, width(140), gap(60), child(top()))); }
public void gridbc2(){ render(grid(gboxes2, width(140), gap(60), child(bottom()))); }
public void gridtl2(){ render(grid(gboxes2, width(140), gap(60), child(top(), left()))); }
public void gridtr2(){ render(grid(gboxes2, width(140), gap(60), child(top(), right()))); }
public void gridbl2(){ render(grid(gboxes2, width(140), gap(60), child(bottom(), left()))); }
public void gridbr2(){ render(grid(gboxes2, width(140), gap(60), child(bottom(), right()))); }
    
    
list[Figure] gboxes3 = [box(size(40,40),fillColor("lightgreen")), box(size(40,40),fillColor("lightblue")),
           box(size(40,40),fillColor("grey")),
           box(size(40,40),fillColor("red")), box(align(0.0,0.0), size(20,20),fillColor("yellow")), 
           box(size(40,40),fillColor("green")), box(align(1.0,1.0), size(20,40),fillColor("blue")),
           box(size(40,20),fillColor("black")), box(size(60,60),fillColor("purple"))
           ];  
           
public void gridAA(){ render(grid(gboxes3, width(140), gap(60))); }


// ===== pack =====

public void pack0(){
  boxes = [box(width(50), height(50),fillColor("red"))];
   render(pack(boxes, width(200), height(170), lineWidth(0), gap(5,10), top()));
}

public void pack1(){
  boxes = [box(size(50),fillColor("red")), box(size(30),fillColor("yellow")), 
           box(size(30) ,fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70),fillColor("black")), box(size(10,20),fillColor("orange")),
           box(size(80,10),fillColor("grey")), box(size(10,150),fillColor("black")),
           box(size(10),fillColor("lightblue")),
           box(size(10),fillColor("lightblue")),
           box(size(10),fillColor("lightblue")),
           box(size(10,20),fillColor("orange")) ,
           box(size(10,20),fillColor("orange")),
           box(size(10,20),fillColor("orange")) ,
           box(size(10,20),fillColor("orange"))                  
           ];

  render(pack(boxes, width(200), height(170), lineWidth(0), gap(5,10), top()));
}

public void pack2(){
  boxes = [box(size(50),fillColor("red")), box(size(30),fillColor("yellow")), 
           box(size(30) ,fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70),fillColor("black")), box(size(10,20),fillColor("orange")),
           box(size(80,10),fillColor("grey")), box(size(10,150),fillColor("black")),
           box(size(10),fillColor("lightblue")),
           box(size(10),fillColor("lightblue")),
           box(size(10),fillColor("lightblue")),
           box(size(10,20),fillColor("orange")) ,
           box(size(10,20),fillColor("orange")),
           box(size(10,20),fillColor("orange")) ,
           box(size(10,20),fillColor("orange"))                  
           ];

  render(pack(boxes, width(200), height(170), lineWidth(0), gap(5,5), top()));
}
*/
