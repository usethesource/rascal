@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze J. van der Ploeg - atze.van.der.ploeg@cwi.nl - CWI}
module vis::examples::GenGraph

import vis::Figure;
import vis::Render;

import util::Math;
import IO;


public Color arbColor(){
	return rgb(toInt(arbReal() * 255.0),toInt(arbReal() * 255.0),toInt(arbReal() * 255.0));
}

public Figure genGraph(int nNodes, int nEdges, real minX, real maxX, real minY, real maxY, real hg, real vg, Orientation or){
   nodes = [box(id("<i>"), fillColor(arbColor()), size(minX + round(arbReal() * (maxX-minX)), minY + round(arbReal() * (maxY -minY))),resizable(false)) | i <- [1 .. nNodes+1]];
   edges = [edge("<1+arbInt(nNodes)>", "<1+arbInt(nNodes)>") |  i <- [1 .. nEdges+1]];
   return graph(nodes, edges, std(gap(hg,vg)), orientation(or));
}

public void testGraph(){
	bool recompute = false;
	int hg = 10;
	int vg = 10;
	int minx = 20;
	int maxx = 20;
	int miny = 20;
	int maxy = 20;
	int nNodes = 2;
	int nEdges = 2;
	Figure currentGraph;
	Orientation or = topDown();
	render(
		hcat([
			scrollable(
				computeFigure(bool () { if(recompute){ recompute = false ; return true;} return false; },
					Figure () { currentGraph = genGraph(nNodes, nEdges, toReal(minx),toReal(maxx), toReal(miny),toReal(maxy), toReal(hg), toReal(vg), or); println(currentGraph); return currentGraph;}
				)),
			vcat([grid([
				[text(str () { return "hgap: <hg>";}),scaleSlider(int() { return 0; } ,int () { return 200; } , int () { return hg; },void (int s) { hg = s; })],
				[text(str () { return "vgap: <vg>";}),scaleSlider(int() { return 0; } ,int () { return 200; } , int () { return vg; },void (int s) { vg = s; })],
				[text(str () { return "minwidth: <minx>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return minx; },void (int s) { minx = s; maxx = max(minx,maxx); })],
				[text(str () { return "maxwidth: <maxx>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return maxx; },void (int s) { maxx = s;maxx = max(minx,maxx); })],
				[text(str () { return "minheigth: <miny>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return miny; },void (int s) { miny = s; maxy = max(miny,maxy);})],
				[text(str () { return "maxheigth: <maxy>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return maxy; },void (int s) { maxy = s; maxy = max(miny,maxy);})],
				[text(str () { return "nNodes: <nNodes>";}),scaleSlider(int() { return 0; } ,int () { return 100; } , int () { return nNodes; },void (int s) { nNodes = s; })],
				[text(str () { return "nEdges: <nEdges>";}),scaleSlider(int() { return 0; } ,int () { return 100; } , int () { return nEdges; },void (int s) { nEdges = s; })]		
			])
			,
			choice(["topDown","downTop","leftRight","rightLeft"],void (str s){
						switch(s){
							case "topDown" : or = topDown();
							case "downTop" : or = downTop();
							case "leftRight" : or = leftRight();
							case "rightLeft" : or = rightLeft();
						}
					},vshrink(0.25)),
			button("Generate!",void() {recompute = true;},vshrink(0.1))],hresizable(false))
		])
	);
}
