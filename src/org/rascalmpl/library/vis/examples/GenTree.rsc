@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze J. van der Ploeg - atze.van.der.ploeg@cwi.nl - CWI}
module vis::examples::GenTree

import vis::Figure;
import vis::Render;

import util::Math;
import IO;


public Color arbColor(){
	return rgb(toInt(arbReal() * 255.0),toInt(arbReal() * 255.0),toInt(arbReal() * 255.0));
}

public Figure genTree(int leafChance,int minDepth,int maxDepth, int minKids, int maxKids, real minX, real minY,real maxX, real maxY){
	Figure root = box(fillColor(arbColor()),size(minX + round(arbReal() * (maxX-minX)), minY + round(arbReal() * (maxY -minY))),resizable(false));
	if(maxDepth == 0 || minDepth <= 0 && toInt(arbReal() * 100.0) <= leafChance){ return tree(root,[]); }
	int nr = arbInt(maxKids-minKids) + minKids;
	
	
	return tree(root,
		[ genTree(leafChance,minDepth-1,maxDepth-1,minKids,maxKids,minX,minY,maxX,maxY) | i <- [0..nr+1]]);	
}

public void testTree(){
	bool recompute = false;
	int hg = 10;
	int vg = 10;
	int minx = 10;
	int maxx = 10;
	int miny = 10;
	int maxy = 10;
	int minDepth = 1;
	int maxDepth = 3;
	int minKids = 1;
	int maxKids = 4;
	int leafChance = 20;
	bool man = false;
	Orientation or = topDown();
	render(
		hcat([
			scrollable(
				computeFigure(bool () { if(recompute){ recompute = false ; return true;} return false; },
					Figure () { return genTree(leafChance,minDepth,maxDepth,minKids,maxKids,toReal(minx),toReal(miny), toReal(maxx),toReal(maxy));}
					,std(gap(real () { return toReal(hg);},real () { return toReal(vg); })),std(manhattan(bool () {return man; })),
					std(orientation(Orientation () { return or; })))
				),
			vcat([grid([
				[text(str () { return "hgap: <hg>";}),scaleSlider(int() { return 0; } ,int () { return 200; } , int () { return hg; },void (int s) { hg = s; })],
				[text(str () { return "vgap: <vg>";}),scaleSlider(int() { return 0; } ,int () { return 200; } , int () { return vg; },void (int s) { vg = s; })],
				[text(str () { return "minwidth: <minx>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return minx; },void (int s) { minx = s; maxx = max(minx,maxx); })],
				[text(str () { return "maxwidth: <maxx>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return maxx; },void (int s) { maxx = s;maxx = max(minx,maxx); })],
				[text(str () { return "minheigth: <miny>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return miny; },void (int s) { miny = s; maxy = max(miny,maxy);})],
				[text(str () { return "maxheigth: <maxy>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return maxy; },void (int s) { maxy = s; maxy = max(miny,maxy);})],
				[text(str () { return "minDepth: <minDepth>";}),scaleSlider(int() { return 0; } ,int () { return 10; } , int () { return minDepth; },void (int s) { minDepth = s; maxDepth = max(maxDepth,minDepth); })],
				[text(str () { return "maxDepth: <maxDepth>";}),scaleSlider(int() { return 0; } ,int () { return 10; } , int () { return maxDepth; },void (int s) { maxDepth = s; maxDepth = max(maxDepth,minDepth); })],
				[text(str () { return "minKids: <minKids>";}),scaleSlider(int() { return 1; } ,int () { return 10; } , int () { return minKids; },void (int s) { minKids = s; maxKids = max(minKids,maxKids);})],
				[text(str () { return "maxKids: <maxKids>";}),scaleSlider(int() { return minKids; } ,int () { return 10; } , int () { return maxKids; },void (int s) { maxKids = s; maxKids = max(minKids,maxKids);})],
				[text(str () { return "leafChance: <leafChance>";}),scaleSlider(int() { return 0; } ,int () { return 100; } , int () { return leafChance; },void (int s) { leafChance = s; })],
				[space(), checkbox("Manhattan",false,void (bool b){ man = b; })]		
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


public Figure genTreeMap(int leafChance,int minDepth,int maxDepth, int minKids, int maxKids,real minArea){
	FProperties p = [fillColor(arbColor()),area((100.0 - minArea) * arbReal() + minArea)];
	if(maxDepth == 0 || minDepth <= 0 && toInt(arbReal() * 100.0) <= leafChance){ return box(p); }
	int nr = arbInt(maxKids-minKids) + minKids;

	return treemap(
		[ genTreeMap(leafChance,minDepth-1,maxDepth-1,minKids,maxKids,minArea) | i <- [0..nr+1]],p);	
}


public void testTreeMap(){
	bool recompute = false;
	int minArea = 0;
	int minDepth = 1;
	int maxDepth = 3;
	int minKids = 1;
	int maxKids = 4;
	int leafChance = 20;
	render(
		hcat([
			scrollable(
				computeFigure(bool () { if(recompute){ recompute = false ; return true;} return false; },
					Figure () { return genTreeMap(leafChance,minDepth,maxDepth,minKids,maxKids,toReal(minArea));}
				)
			),
			vcat([
				grid([
				[text(str () { return "minArea: <minArea>";}),scaleSlider(int() { return 0; } ,int () { return 100; } , int () { return minArea; },void (int s) { minArea = s; })],
				[text(str () { return "minDepth: <minDepth>";}),scaleSlider(int() { return 0; } ,int () { return 10; } , int () { return minDepth; },void (int s) { minDepth = s; maxDepth = max(maxDepth,minDepth); })],
				[text(str () { return "maxDepth: <maxDepth>";}),scaleSlider(int() { return 0; } ,int () { return 10; } , int () { return maxDepth; },void (int s) { maxDepth = s; maxDepth = max(maxDepth,minDepth); })],
				[text(str () { return "minKids: <minKids>";}),scaleSlider(int() { return 1; } ,int () { return 10; } , int () { return minKids; },void (int s) { minKids = s; maxKids = max(minKids,maxKids);})],
				[text(str () { return "maxKids: <maxKids>";}),scaleSlider(int() { return minKids; } ,int () { return 10; } , int () { return maxKids; },void (int s) { maxKids = s; maxKids = max(minKids,maxKids);})],
				[text(str () { return "leafChance: <leafChance>";}),scaleSlider(int() { return 0; } ,int () { return 100; } , int () { return leafChance; },void (int s) { leafChance = s; })]
				]),
				button("Generate!",void() {recompute = true;},vshrink(0.1))
			],hshrink(0.15))
		])
	);
}
