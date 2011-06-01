@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

module vis::examples::Resize

import vis::Figure;
import vis::Render;
import List;



public void grid(int rows,int cols){
	elems = for(i <- [1..rows]){
		append for(j <- [1..cols]){
			append box(text("test"),grow(2.0),fillColor("red"));
		}
	}
	render(newgrid(elems));
}


public void nestedGrid(int row,int cols, int depth){
	
	Figure nest(int d) {
		if(d == 0) return box(text("atze"),fillColor("red"));
		elems = for(i <- [1..row]){
			append for(j <- [1..cols]){
				append nest(d-1);
			}
		};
		return newgrid(elems,grow(1.05));
	}
	render(nest(depth));
}

public void testShrink(int cols){
	render(hcat([box(fillColor("red"),hshrink(1.0/((i+1.0)*(i+1.0)))) | i <- [1..cols]],hgrow(1.5)));
}

public void testShrink2(int rows,int cols){
	elems = for(i <- [1..rows]){
		append for(j <- [1..cols]){
			append box(hshrink(1.0/((j+1.0)*(j+1.0))),vshrink(1.0/((i+1.0)*(i+1.0))));
		}
	}
	render(newgrid(elems,grow(1.5),stdFillColor("red")));
}

public void testNonResizable(int n){
	render(hcat([box(text("jada"),resizable(false),grow(1.5)) | i <- [1..n]]));
}

public void testHalfNonResizable(int n){
	render(hcat([box(fillColor("red")) | i <- [1..n]] + [box(text("jada"),resizable(false),grow(1.5)) | i <- [1..n]]));
}

public void testHalfNonResizable2(int n){
	render(hcat([box(fillColor("green"),hshrink(0.3))] + [box(fillColor("red")) | i <- [1..n]] + [box(text("jada"),resizable(false),grow(1.5)) | i <- [1..n]]));
}

public void recursiveThing(){
	list[str] underMouse = [];
	allColors = ["red","green","blue","orange","yellow","purple","brown"];
	int depth = 4;
	void popColor() { underMouse = tail(underMouse); }

	public Figure makeRecThing(list[str] cl){
		void pushColor(){ underMouse= [head(cl)] + underMouse; }
		if(size(cl) == 0) return space();
		return ellipse(newgrid([[makeRecThing(tail(cl)),makeRecThing(tail(cl))],
		[makeRecThing(tail(cl)),makeRecThing(tail(cl))]]),grow(1.3),fillColor(color(head(cl))));
	}
	
	
	colorStack = computeFigure(Figure () { return hcat( [box(text(c),grow(2.0),resizable(false),fillColor(color(c))) | c <- reverse(underMouse)],hgrow(1.2),vshrink(0.2));});
	/*makeRecThing([color(s) | s <- ["red","green","blue"]])*/
	render(makeRecThing(slice(allColors,0,depth)));
}





