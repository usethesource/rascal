@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

module vis::examples::Box




import vis::Figure;
import vis::Render;
import Number;

import List;
import Set;
import IO;

public void box1(){
	render(box());
}


public void box2(){
	render(box(shrink(0.5)));
}


public void box3(){
	render(box( fillColor("green")));
}


public void box4(){
	render(box(hshrink(0.7), vshrink(0.2),fillColor("green"), lineColor("red")));
}

public void box5(){
	render(box(box(fillColor("white")), fillColor("mediumblue"), grow(1.5)));
}


public void box6(){
	render(box(ellipse(fillColor("white")), fillColor("mediumblue"), grow(2.0)));
}

// Unsized blue outer box with black inner text

public void box7(){
	render(box(text("een label"), fillColor("mediumblue"), grow(1.2)));
}

// Unsized blue outer box with white inner text

public void box8(){
	render(box(text("een label", fontColor("white")), fillColor("mediumblue"),grow(1.2)));
}


public void box9(){
	render(box(text("een label"),  fillColor("mediumblue"), grow(2.0),shrink(0.7)));
}


public void bbc(){
	render(box(box(fillColor("green"), lineColor("red")), grow(2.0,1.5)));
}


public void bbl(){
	render(box(box(fillColor("green"), lineColor("red"),left()), grow(2.0,1.5)));
}


public void bblt(){
	render(box(box(text("label"),fillColor("green"), lineColor("red"),  left(), top(),grow(1.2)), grow(2.0,1.5),resizable(false)));
}

// Sized outer box, with bottom-aligned inner box of 100x200
public void bblb(){
	render(box(box(text("label"),fillColor("green"), lineColor("red"),  left(), bottom(),grow(1.2)), grow(2.0,1.5),resizable(false)));
}

public void bbr(){
	render(box(box(text("label"),fillColor("green"), lineColor("red"),  left(), right(),grow(1.2)), grow(2.0,1.5),vresizable(false)));
}

public void bbrt(){
	render(box(box(text("label"),fillColor("green"), lineColor("red"),  left(), right(),top(),grow(1.2)), grow(2.0,1.5)));
}

// Sized outer box, with bottom-aligned and right-aligned inner box of 100x200
public void bbrb(){
	render(box(box(text("label"),fillColor("green"), lineColor("red"),  left(), right(),grow(1.2)), grow(2.0,1.5),width(100.0),height(100.0),hresizable(false)));
}

// unsized out box, top-align and right-align inner box of 100x200, gapFactor(0.3)
public void ubbtr(){
	render(box(box(text("label"),fillColor("green"), lineColor("red"),  left(), right(),grow(1.2)), grow(2.0,1.5),width(100.0),height(300.0),resizable(false)));
}

// size out box, top-align and right-align unsize inner box of, gapFactor(0.3)
public void bubtr(){
	render(box(box( fillColor("green"), lineColor("red"), right(), bottom()), grow(1.3),size(300.0,200.0),resizable(false)));
}
