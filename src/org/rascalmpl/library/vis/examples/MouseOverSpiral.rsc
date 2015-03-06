@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module vis::examples::MouseOverSpiral

import vis::Figure;
import vis::Render;
import util::Math;

public Figure mouseOverSpiral(int n,real radius, real increase,real radiusIncrease,real curAngle){
	list[FProperty] props = (n == 0) ? 
		[] : 
		[mouseOver(mouseOverSpiral(n-1,radius + radiusIncrease,increase,radiusIncrease,curAngle+increase))];
	r = max(0.5,radius);
	h = sin(curAngle) * radius + 0.5;
	v = cos(curAngle) * radius + 0.5;
	return ellipse(text("<n>"), [*props, halign(h), valign(v), resizable(false), size(100), fillColor(arbColor())]);
}
	
public void doMouseOverSpiral(){
	spiral = mouseOverSpiral(200,0.1,0.15,0.001,0.0);
	e = ellipse(text("Move mouse over me!"),fillColor("red"),shrink(0.5),mouseOver(spiral));
	render(e);
}
