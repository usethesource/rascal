@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::examples::Trafo

import vis::Figure;
import vis::Render;

import Number;
import List;
import Set;
import IO;

/*
 * Rotation
 */
 
 /*
public void rot1a(){
  render(rotate(0.0, box(align(0.5, 0.5), size(50,70))));
}

public void rot1b(){
  render(rotate(45.0, box(align(0.5, 0.5), size(50,70))));
}

public void rot1c(){
  render(rotate(90.0, box(align(0.5, 0.5), size(50,70))));
}

public void rot2(){
  render(rotate(45.0, box(align(1.0, 1.0), size(50,70))));
}

public void rot3a(){
  render(rotate(90.0, box(align(0.0, 0.0), size(50,70))));
}

public void rot3b(){
  render(rotate(90.0, box(align(0.0, 1.0), size(50,70))));
}

public void rot3c(){
  render(rotate(90.0, box(align(1.0, 0.0), size(50,70))));
}

public void rot3d(){
  render(rotate(90.0, box([align(1.0, 1.0), size(50,70)])));
}

public void rot4a(){
  render(rotate(45.0, box(align(0.0, 0.0), size(50,70))));
}

public void rot4b(){
  render(rotate(45.0, box(align(0.0, 1.0), size(50,70))));
}

public void rot4c(){
  render(rotate(45.0, box(align(1.0, 0.0), size(50,70))));
}

public void rot4d(){
  render(rotate(45.0, box(align(1.0, 1.0), size(50,70))));
}

public void rot5a(){
  render(rotate(135.0, box(align(0.0, 0.0), size(50,70))));
}

public void rot5b(){
  render(rotate(135.0, box(align(0.0, 1.0), size(50,70))));
}

public void rot5c(){
  render(rotate(135.0, box(align(1.0, 0.0), size(50,70))));
}

public void rot5d(){
  render(rotate(135.0, box(align(1.0, 1.0), size(50,70))));
}

public void rot6(){
  b1 = rotate(0.0, box(align(0.5, 0.5), fillColor("green"), size(50,70)));
  b2 = rotate(45.0, box(align(0.5, 0.5), fillColor("yellow"), size(50,70)));
  b3 = rotate(90.0, box(align(0.5, 0.5), fillColor("red"), size(50,70)));
  b4 = rotate(145.0, box(align(0.5, 0.5), fillColor("blue"), size(50,70)));
  render(overlay([b1, b2, b3, b4]));
}

public void rot7(){
  b1 = rotate(0.0, box(align(0.0, 0.0), fillColor("green"), size(50,70)));
  b2 = rotate(45.0, box(align(0.0, 0.0), fillColor("yellow"), size(50,70)));
  b3 = rotate(90.0, box(align(0.0, 0.0), fillColor("red"), size(50,70)));
  b4 = rotate(145.0, box(align(0.0, 0.0), fillColor("blue"), size(50,70)));
  render(overlay([b1, b2, b3, b4]));
}

public void rot8(){
  b1 = rotate(0.0, box(align(0.0, 1.0), fillColor("green"), size(50,70)));
  b2 = rotate(45.0, box(align(0.0, 1.0), fillColor("yellow"), size(50,70)));
  b3 = rotate(90.0, box(align(0.0, 1.0), fillColor("red"), size(50,70)));
  b4 = rotate(145.0, box(align(0.0, 1.0), fillColor("blue"), size(50,70)));
  render(overlay([b1, b2, b3, b4]));
}

public void rot9(){
  b1 = rotate(0.0, box(align(1.0, 0.0), fillColor("green"), size(50,70)));
  b2 = rotate(45.0, box(align(1.0, 0.0), fillColor("yellow"), size(50,70)));
  b3 = rotate(90.0, box(align(1.0, 0.0), fillColor("red"), size(50,70)));
  b4 = rotate(145.0, box(align(1.0, 0.0), fillColor("blue"), size(50,70)));
  render(overlay([b1, b2, b3, b4]));
}

public void rot10(){
  b1 = rotate(0.0, box(align(1.0, 1.0), fillColor("green"), size(50,70)));
  b2 = rotate(45.0, box(align(1.0, 1.0), fillColor("yellow"), size(50,70)));
  b3 = rotate(90.0, box(align(1.0, 1.0), fillColor("red"), size(50,70)));
  b4 = rotate(145.0, box(align(1.0, 1.0), fillColor("blue"), size(50,70)));
  render(overlay([b1, b2, b3, b4]));
}

/*
 * Scaling
 */
 
 public void sc1(){
   render(scale(1.0, 1.0, box(size(50,70))));
 }
 
 public void sc2(){
   render(scale(2.0, 2.0, box(size(50,70))));
 }
 
 public void sc3(){
   render(scale(2.0, box(size(50,70))));
 }
 
  public void sc4(){
   render(scale(0.5, box(size(50,70))));
 }
 
 public void sc5(){
   render(scale(0.5, 2.0, box(size(50,70))));
 }
 
 */
 
