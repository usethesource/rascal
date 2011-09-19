@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::examples::Ellipse

import vis::Figure;
import vis::Render;
import Number;

import List;
import Set;
import IO;


public void e1(){
	render(ellipse());
}

// Blue ellipse that grows 3x larger than white inner box
public void e2(){
	render(ellipse( box(fillColor("white")),
	                fillColor("mediumblue"), grow(3.0)));
}

// Blue ellipse that grows 1.2x larger than white inner text
public void e3(){
	render(ellipse(text("een label", fontColor("white")),
	               fillColor("mediumblue"), grow(1.2)));
}

