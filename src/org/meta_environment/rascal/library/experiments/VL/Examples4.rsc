module experiments::VL::Examples4

import experiments::VL::VLCore;
import experiments::VL::VLRender; 
import Integer;
// import viz::VLRender;

import Integer;
import List;
import Set;
import IO;

public void w1a(){
  render(wedge([fromAngle(10), toAngle(80), fillColor("blue"), height(100), innerRadius(20)]));
}

public void w1b(){
  render(wedge([fromAngle(10), toAngle(170), fillColor("blue"), height(100), innerRadius(20)]));
}

public void w1c(){
  render(wedge([fromAngle(10), toAngle(260), fillColor("blue"), height(100), innerRadius(20)]));
}

public void w1d(){
  render(wedge([fromAngle(10), toAngle(350), fillColor("blue"), height(100), innerRadius(20)]));
}

public void w2a(){
  render(wedge([fromAngle(100), toAngle(170), fillColor("green"),height(100),  innerRadius(20)]));
}

public void w2b(){
  render(wedge([fromAngle(100), toAngle(260), fillColor("green"),height(100),  innerRadius(20)]));
}

public void w2c(){
  render(wedge([fromAngle(100), toAngle(350), fillColor("green"), height(100), innerRadius(20)]));
}
//TODO
public void w2d(){
  render(wedge([fromAngle(100), toAngle(45), fillColor("green"), height(100), innerRadius(20)]));
}

public void w3a(){
  render(wedge([fromAngle(190), toAngle(260), fillColor("red"), height(100), innerRadius(20)]));
}

public void w3b(){
  render(wedge([fromAngle(190), toAngle(350), fillColor("red"), height(100), innerRadius(20)]));
}
//TODO
public void w3c(){
  render(wedge([fromAngle(190), toAngle(45), fillColor("red"), height(100), innerRadius(20)]));
}

public void w4a(){
  render(wedge([fromAngle(280), toAngle(350), fillColor("yellow"),height(100),  innerRadius(20)]));
}

public void w4b(){
  render(wedge([fromAngle(280), toAngle(80), fillColor("yellow"), height(100), innerRadius(20)]));
}

public void w4c(){
  render(wedge([fromAngle(280), toAngle(180), fillColor("yellow"), height(100), innerRadius(20)]));
}

public void w4d(){
  render(wedge([fromAngle(280), toAngle(260), fillColor("yellow"), height(100), innerRadius(20)]));
}

public void w5(){
  render(overlay([height(100)],
 	 [wedge([fromAngle(0), toAngle(90), fillColor("blue"), innerRadius(20)]),
      wedge([fromAngle(90), toAngle(180), fillColor("green"), innerRadius(20)]),
      wedge([fromAngle(180), toAngle(270), fillColor("red"), innerRadius(20)]),
      wedge([fromAngle(270), toAngle(360), fillColor("yellow"), innerRadius(20)])
    ]));
}

public void w6(){
  render(overlay([height(100)],
 	 [wedge([fromAngle(0), toAngle(90), fillColor("blue"), innerRadius(20)]),
      wedge([fromAngle(90), toAngle(180), fillColor("green"), innerRadius(30)]),
      wedge([fromAngle(180), toAngle(270), fillColor("red"), innerRadius(40)]),
      wedge([fromAngle(270), toAngle(360), fillColor("yellow"), innerRadius(50)])
    ]));
}

public void w7(){
  render(overlay([lineWidth(10), height(100) ],
 	 [wedge([fromAngle(0), toAngle(90), fillColor("blue"), innerRadius(20)]),
      wedge([fromAngle(90), toAngle(180), fillColor("green"), innerRadius(30)]),
      wedge([fromAngle(180), toAngle(270), fillColor("red"), innerRadius(40)]),
      wedge([fromAngle(270), toAngle(360), fillColor("yellow"), innerRadius(50)])
    ]));
}

public void w8(){
  render(overlay([ height(100) ],
 	 [wedge([fromAngle(0), toAngle(180), fillColor("blue"), innerRadius(20)]),
      wedge([fromAngle(180), toAngle(360), fillColor("green"), innerRadius(20)])
    ]));
}


public void w9(){
  render(overlay([ height(100) ],
 	 [wedge([fromAngle(0), toAngle(270), fillColor("blue"), innerRadius(20)]),
      wedge([fromAngle(270), toAngle(360), fillColor("green"), innerRadius(20)])
    ]));
}

public void w10(){
  render(overlay([ height(100) ],
 	 [wedge([fromAngle(270), toAngle(90), fillColor("blue"), innerRadius(20)]),
      wedge([fromAngle(90), toAngle(270), fillColor("green"), innerRadius(20)])
    ]));
}

public void wn(int delta){
   render(overlay([ height(100) ],
 		[ wedge([fromAngle(a), toAngle(a+delta), fillColor("yellow"), innerRadius(50)]) | a <- [0, delta .. 360 - delta]]
 		));
}
