module vis::examples::Layout

import vis::Figure;
import vis::Render; 
import Set;

// HVCat of boxes

public void align1(){
  boxes = [box(size(50,50),fillColor("red"))];

  render(hvcat(boxes, width(125), gap(10), top(), left()));
}

public void hvcattl(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(hvcat(boxes, width(125), gap(10), top(), left()));
}

public void hvcattc(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(hvcat(boxes, width(125), gap(10), top(), hcenter()));
}

public void hvcattr(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(hvcat(boxes, width(125), gap(10), top(), right()));
}

public void hvcatcl(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(hvcat(boxes, width(125), gap(10), vcenter(), left()));
}

public void hvcatcc(){
  boxes =  [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(hvcat(boxes, width(125), gap(10), center()));
}

public void hvcatcr(){
  boxes =  [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(hvcat(boxes, width(125), gap(10), vcenter(), right()));
}

public void hvcatbl(){
  boxes =  [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(hvcat(boxes, width(125), gap(10), bottom(), left()));
}

public void hvcatbc(){
  boxes =  [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(hvcat(boxes, width(125), gap(10), bottom(), hcenter()));
}

public void hvcatbr(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(hvcat(boxes, width(125), gap(10), bottom(), right()));
}

// Grid alignment

public void gridcc(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(grid(boxes, width(90), gap(50)));
}

public void gridcl(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(grid(boxes, width(90), gap(50), left()));
}

public void gridcr(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(grid(boxes, width(90), gap(50), right()));
}

public void gridtc(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(grid(boxes, width(90), gap(50), top()));
}

public void gridtl(){
  boxes =[box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(grid(boxes, width(90), gap(50), top(), left()));
}

public void gridtr(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(grid(boxes, width(90), gap(50), top(), right()));
}

public void gridbc(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(grid(boxes, width(90), gap(50), bottom()));
}

public void gridbl(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(grid(boxes, width(90), gap(50), bottom(), left()));
}

public void gridbr(){
  boxes = [box(size(50,50),fillColor("red")), box(size(30,30),fillColor("yellow")), 
           box(size(30,30),fillColor("green")), box(size(70,50),fillColor("blue")),
           box(size(70,70),fillColor("black"))
           ];

  render(grid(boxes, width(90), gap(50), bottom(), right()));
}

// one point grid

public void grid0(){
  boxes = [box(bottom(), right(), size(20,20),fillColor("red")), 
           box(bottom(), left(), size(30,30),fillColor("yellow")),
           box(top(), right(), size(20,20),fillColor("green")),
           box(top(), left(), size(20,20),fillColor("blue")),
           box(hcenter(), vcenter(), size(10,10),fillColor("black"))
           ];

  render(grid(boxes, gap(0)));
}


public void grid1(){
  boxes = [box(width(50), height(50),fillColor("red")), box(width(30), height(30),fillColor("yellow")), 
           box(width(30), height(30),fillColor("green")), box(width(70), height(50),fillColor("blue")),
           box(width(70), height(70),fillColor("black"))
           ];

  render(grid(boxes, width(120), gap(10), top()));
}

public void grid11(){

 boxes = [box(width(50), height(50),fillColor("red")), box(width(30), height(30),fillColor("yellow")), 
           box(width(30), height(30),fillColor("green")), box(width(70), height(50),fillColor("blue")),
           box(width(70), height(70),fillColor("black"))
           ];
           
 g1 = grid(boxes, width(120), gap(10), top());
 g2 = grid(boxes, width(80), gap(10), top());
 
 render(hcat([g1, g2], gap(50)));
}

// Overlay with same effect as grid0

public void ov1(){
  boxes = [box(bottom(), right(), size(20,20),fillColor("red")), 
           box(bottom(), left(), size(30,30),fillColor("yellow")),
           box(top(), right(), size(20,20),fillColor("green")),
           box(top(), left(), size(20,20),fillColor("blue")),
           box(hcenter(), vcenter(), size(10,10),fillColor("black"))
           ];

  render(overlay(boxes));
}


// Packed boxes

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