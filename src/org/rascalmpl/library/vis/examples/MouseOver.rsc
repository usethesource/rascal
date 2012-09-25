@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::examples::MouseOver
import vis::Figure;
import vis::Render; 
import Set;
import List;
import Real;
import util::Math;




public FProperty popup(str s){
	return mouseOver(box(text(s),grow(1.2),resizable(false)));
}

public void simple(){
	render(box(popup("Hallo!!"),shrink(0.5)));
}

public void mouseOverLeft(){
	render(ellipse(mouseOver(ellipse(shrink(0.3),left()))));
}

public Figure tunnel(real hal, real val){
	
	return ( box(fillColor("black")) 
	       | box(mouseOver(it),shrink(0.95),fillColor(rrgba(toReal(i)/50.0,0.0,0.0,1.0)),align(hal,val)) 
	       | i <- [1..50]);
}

public void straightTunnel(){
	render(tunnel(0.5,0.5));
}


public void leftUpTunnel(){
	render(tunnel(0.1,0.3));
}

public void increaseSize(){
	render( box(( box(fillColor("black")) 
	       | box(mouseOver(it),hshrink(0.95),vshrink(1.05),fillColor(rrgba(toReal(i)/50.0,0.0,0.0,1.0))) 
	       | i <- [1..50]),vshrink(0.1),fillColor("red")));
}
  



